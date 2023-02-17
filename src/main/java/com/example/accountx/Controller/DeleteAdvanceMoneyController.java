package com.example.accountx.Controller;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class DeleteAdvanceMoneyController
{
    @FXML private ChoiceBox<User> personChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField existingAmountTextField;

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
    private void clickDelete()
    {
        if (existingValue != null)
        {
            SessionFactoryManager.delete(existingValue);
            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Avans miktarı başarı ile güncellendi!...", ButtonType.OK);
        }
    }
}
