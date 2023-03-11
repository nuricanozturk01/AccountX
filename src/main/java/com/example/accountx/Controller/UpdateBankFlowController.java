package com.example.accountx.Controller;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.Entity.CostType;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.commandcontroller.CommandController;
import com.example.accountx.command.dto.BankFlowDTO;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;

import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.update;
import static com.example.accountx.command.types.CommandName.BANK_FLOW;
import static com.example.accountx.command.types.CommandType.UPDATE;

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
    private void initialize()
    {
        costTypeChoiceBox.getItems().addAll(SessionFactoryManager.getCostTypes());
    }
    private void setFields()
    {
        costTypeChoiceBox.getSelectionModel().select(SessionFactoryManager.getCostType(existingBankFlow.getCostType()));
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


            CommandController.getInstance().acceptUpdate(BANK_FLOW, new BankFlowDTO(existingBankFlow, UPDATE));

            var amount = new BigDecimal(amountString);

            existingBankFlow.setDate(date);
            existingBankFlow.setCaseFlow(description);
            existingBankFlow.setCostType(costType.getCostType());
            existingBankFlow.setBillingNumber(billingNumber);
            existingBankFlow.setCost(amount);


            update(existingBankFlow);

            controller.update(existingBankFlow);

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Banka Hareket Formu başarı ile güncellendi!", ButtonType.OK);
        }
        catch (NumberFormatException numberFormatException) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen geçerli bir sayı giriniz!", ButtonType.OK);
        }
        catch (Exception ex) {
            var msg = "Lütfen geçerli yazı giriniz... Cümle başı ve sonunda boşluk olamaz!";
            UtilFX.alertScreen(Alert.AlertType.ERROR, msg, ButtonType.OK);
            System.out.println(ex.getMessage());
        }
    }
}
