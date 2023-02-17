package com.example.accountx.Controller;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.*;
import static com.example.accountx.util.Constant.START_YEAR;
import static com.example.accountx.util.Constant.STOP_YEAR;
public class AdvanceAmountYearPersonController
{
    @FXML
    private ChoiceBox<User> userChoiceBox;
    @FXML
    private ChoiceBox<Integer> yearChoiceBox;
    @FXML
    private ListView<AdvanceAmount> advanceAmountListView;
    @FXML
    private Label sumAdvanceAmountYearLabel;
    @FXML
    private void initialize()
    {
        getUserList().forEach(userChoiceBox.getItems()::add);
        IntStream.range(START_YEAR, STOP_YEAR).forEach(yearChoiceBox.getItems()::add);
    }
    @FXML
    private void clickGetButton()
    {
        var user = userChoiceBox.getSelectionModel().getSelectedItem();
        var year = yearChoiceBox.getSelectionModel().getSelectedItem();
        var list = getAdvanceAmountsByYear(user.getUser_pk_id(), year);

        advanceAmountListView.getItems().clear();

        if (list == null)
        {
            advanceAmountListView.getItems().add(new AdvanceAmount(user, BigDecimal.ZERO, LocalDate.now()));
            sumAdvanceAmountYearLabel.setText("0 ₺");
        }
        else
        {
            list.forEach(advanceAmountListView.getItems()::add);
            sumAdvanceAmountYearLabel.setText(list.stream().map(AdvanceAmount::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add) + " ₺");
        }
    }
}
