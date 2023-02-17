package com.example.accountx.Controller;

import com.example.accountx.Entity.CostType;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class SumOfTotalCostWithTypes
{
    @FXML private TextField sumOfBankFlowTextField;
    @FXML private TextField sumOfBothTextField;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private TextField costTypeTextField;
    @FXML private TextField sumOfCostFormsTextField;

    @FXML
    private void initialize()
    {
        SessionFactoryManager.getCostTypes().stream().map(CostType::getCostType).forEach(choiceBox.getItems()::add);

        var sumOfBankFlows = SessionFactoryManager.getSumofAllTypesBankFlow();
        sumOfBankFlows = sumOfBankFlows == null ? BigDecimal.ZERO : sumOfBankFlows;

        var sumOfCostForms = SessionFactoryManager.getSumOfAllTypes();
        sumOfCostForms = sumOfCostForms == null ? BigDecimal.ZERO : sumOfCostForms;

        var sumOfTotal = sumOfCostForms.add(sumOfBankFlows);

        sumOfBankFlowTextField.setText(getFormattedNumber(sumOfBankFlows)  + " ₺");
        sumOfCostFormsTextField.setText(getFormattedNumber(sumOfCostForms) + " ₺");
        sumOfBothTextField.setText(getFormattedNumber(sumOfTotal)          + " ₺");
    }
    @FXML
    private void clickGetButton()
    {
        var type = choiceBox.getSelectionModel().getSelectedItem();
        var cf = SessionFactoryManager.getSumOfTypes(type);
        var bankFlow = SessionFactoryManager.getSumOfTypesBankFlow(type);

        cf = cf == null ? BigDecimal.ZERO : cf;
        bankFlow = bankFlow == null ? BigDecimal.ZERO : bankFlow;
        //bankFlow = bankFlow.compareTo(BigDecimal.ZERO) < 0 ? bankFlow : bankFlow.negate();

        var sum = cf.add(bankFlow);

        costTypeTextField.setText(getFormattedNumber(sum) + " ₺");
    }
}
