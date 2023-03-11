package com.example.accountx.Controller;

import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.stream.IntStream;

import static com.example.accountx.util.Constant.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class SumOfPersonWithYear
{
    public Label sumOfTotalLabel;
    public ChoiceBox<User> choiceBox;
    public TextField costTypeTextField;
    public ChoiceBox<Integer> yearChoiceBox;
    @FXML
    private void initialize()
    {
        var users = SessionFactoryManager.getUserList();
        users.forEach(choiceBox.getItems()::add);
        IntStream.range(START_YEAR, STOP_YEAR).forEach(yearChoiceBox.getItems()::add);
    }

    @FXML
    private void clickGetButton()
    {
        var user = choiceBox.getSelectionModel().getSelectedItem();
        var year = yearChoiceBox.getSelectionModel().getSelectedItem();

        var sum = SessionFactoryManager.getSumOfCostForm(year, user.getUser_pk_id());

        costTypeTextField.setText((sum == null ? "0" : getFormattedNumber(sum) ) +  " ₺");
    }
}
