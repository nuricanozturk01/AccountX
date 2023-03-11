package com.example.accountx.Controller;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateAdvanceMoneyController
{
    @FXML private ChoiceBox<User> personChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField existingAmountTextField;
    @FXML private TextField newAmountTextField;

    private AdvanceAmount existingValue;

    @FXML
    private void initialize()
    {
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        datePicker.setValue(LocalDate.now());
    }


    @FXML
    private void clickGet()
    {
        var user = personChoiceBox.getSelectionModel().getSelectedItem();
        var amount = SessionFactoryManager.getAdvanceAmountByUserByDate(user.getUser_pk_id(), datePicker.getValue().getYear(),
                datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth());
        if (amount != null)
        {
            existingValue = amount;
            existingAmountTextField.setText(amount.getAmount() + " ₺");
        }
        else existingAmountTextField.setText("Avans Miktarı Bulunamadı....");
    }

    @FXML
    private void clickUpdate()
    {
        try
        {
            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            var newAmount = new BigDecimal(newAmountTextField.getText());
            user.setAmount(newAmount.subtract(existingValue.getAmount()).add(user.getAmount()));
            existingValue.setAmount(newAmount);
            SessionFactoryManager.update(existingValue);
            SessionFactoryManager.update(user);
            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Avans miktarı başarı ile güncellendi!...", ButtonType.OK);
        }
        catch (NumberFormatException ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen sayı giriniz....", ButtonType.OK);
        }
    }
}
