package com.example.accountingX.Controller;

import com.example.accountingX.Entity.User;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static com.example.accountingX.util.Constant.*;
import java.util.stream.IntStream;

public class SumOfPersonWithYear
{
    public Label sumOfTotalLabel;
    public ChoiceBox<User> choiceBox;
    public TextField costTypeTextField;
    public ChoiceBox<Integer> yearChoiceBox;
    private User user;
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

        costTypeTextField.setText((sum == null ? "0" : sum ) +  " ₺");
    }
}
