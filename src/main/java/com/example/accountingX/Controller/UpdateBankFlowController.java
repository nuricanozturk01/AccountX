package com.example.accountingX.Controller;

import com.example.accountingX.Entity.BankFlow;
import com.example.accountingX.Entity.CostType;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;

public class UpdateBankFlowController
{
    @FXML private ChoiceBox<CostType> costTypeChoiceBox;
    @FXML private TextField amountTextField;
    @FXML private TextArea descriptionText;
    @FXML private DatePicker datePicker;
    @FXML private TextField billingNumberTextField;
    private MainScreenController controller;
    private BankFlow existingBankFlow;

    @FXML
    private void initialize() {
        SessionFactoryManager.getCostTypes().forEach(costTypeChoiceBox.getItems()::add);
    }
    private void setFields() {
        datePicker.setValue(existingBankFlow.getDate());
        billingNumberTextField.setText(existingBankFlow.getBillingNumber());
        descriptionText.setText(existingBankFlow.getCaseFlow());
        amountTextField.setText(existingBankFlow.getCost().toString());
    }

    public void setController(MainScreenController controller) {
        this.controller = controller;
    }

    public void setBankFlow(BankFlow existingBankFlow) {
        this.existingBankFlow = existingBankFlow;
        setFields();
    }

    @FXML
    @SuppressWarnings("all")
    private void clickSaveButton()
    {
        try
        {
            var date = datePicker.getValue();
            var billingNumber = billingNumberTextField.getText();
            var description = descriptionText.getText();
            var costType = costTypeChoiceBox.getSelectionModel().getSelectedItem();
            var amountString = amountTextField.getText();

            if (!StringControl.isValid(billingNumber, description, amountString))
                throw new Exception();

            var amount = new BigDecimal(amountString);

            existingBankFlow.setDate(date);
            existingBankFlow.setCaseFlow(description);
            existingBankFlow.setCostType(costType.getCostType());
            existingBankFlow.setBillingNumber(billingNumber);
            existingBankFlow.setCost(amount);

            SessionFactoryManager.update(existingBankFlow);

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Banka Hareket Formu başarı ile güncellendi!", ButtonType.OK);
            controller.getService().initBankFlowTable();
        }
        catch (NumberFormatException numberFormatException) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen geçerli bir sayı giriniz!", ButtonType.OK);
        }
        catch (Exception ex) {
            var msg = "Lütfen geçerli yazı giriniz... Cümle başı ve sonunda boşluk olamaz!";
            UtilFX.alertScreen(Alert.AlertType.ERROR, msg, ButtonType.OK);
        }
    }
}
