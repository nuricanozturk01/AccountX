package com.example.accountingX.Controller;

import com.example.accountingX.Entity.AdvanceAmount;
import com.example.accountingX.Entity.User;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public class AdvanceMoneyController 
{
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<AdvanceAmount> advanceAmountListView;
    @FXML
    private TextField amountTextField;
    @FXML
    private ChoiceBox<User> personChoiceBox;

    private List<AdvanceAmount> existAmounts;

    @FXML
    private void initialize() {
        amountTextField.setEditable(false);
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        datePicker.setValue(LocalDate.now());
    }
    @FXML
    private void clickGet()
    {
        advanceAmountListView.getItems().clear();
        var date = datePicker.getValue();
        var year = date.getYear();
        var user = personChoiceBox.getSelectionModel().getSelectedItem();
        var month = date.getMonthValue();

        existAmounts = SessionFactoryManager.getAdvanceAmounts(user.getUser_pk_id(), year, month);

        if (existAmounts != null)
            existAmounts.forEach(advanceAmountListView.getItems()::add);

        amountTextField.setEditable(true);
    }
    @FXML
    private void clickSave()
    {
        try
        {
            var date = datePicker.getValue();
            var amount = new BigDecimal(amountTextField.getText());
            //var year = date.getYear();
            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            //var month = Months_TR.values()[date.getMonthValue()];

            var advanceAmount = new AdvanceAmount(user, amount, date);

            SessionFactoryManager.add(advanceAmount);
            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Avans miktarı başarı ile girildi", ButtonType.OK);
            advanceAmountListView.getItems().add(advanceAmount);

        }
        catch (NumberFormatException ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen Geçerli Bir sayı giriniz!", ButtonType.OK);
        }
    }
}
