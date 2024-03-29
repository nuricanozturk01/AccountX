package com.example.accountx.Controller;

import com.example.accountx.Entity.Months_TR;
import com.example.accountx.Entity.User;
import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.example.accountx.util.Constant.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class SumOfPersonWithMonth {
    @FXML private ChoiceBox<User> personChoiceBox;
    @FXML private TextField sumTextField;
    @FXML private ChoiceBox<Months_TR> monthChoiceBox;
    @FXML private ChoiceBox<Integer> yearChoiceBox;

    @FXML
    private void initialize()
    {
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        Arrays.stream(Months_TR.values()).forEach(monthChoiceBox.getItems()::add);
        IntStream.range(START_YEAR, STOP_YEAR).forEach(yearChoiceBox.getItems()::add);
    }
    @FXML
    private void clickGetButton()
    {
        try
        {
            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            var month = monthChoiceBox.getSelectionModel().getSelectedItem();
            var year = yearChoiceBox.getSelectionModel().getSelectedItem();

            if (user == null || month == null || year == null)
                throw new EmptyFieldException("Tüm alanları doldurmalısınız...");

            var sum = SessionFactoryManager.getSumOfCostsofPersonWithMonth(user.getUser_pk_id(), year, month.ordinal());

            sumTextField.setText(sum == null ? "0 ₺" : getFormattedNumber(sum) + " ₺");
        }
        catch (EmptyFieldException e)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }
}
