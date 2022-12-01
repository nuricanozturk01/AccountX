package com.example.accountingX.Controller;

import com.example.accountingX.Entity.CostType;
import com.example.accountingX.Entity.Months_TR;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import static com.example.accountingX.util.Constant.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;

public class SumOfTotalCostForBankFlow 
{
    @FXML private ChoiceBox<CostType> costTypeChoiceBox;
    @FXML private TextField sumTextField;
    @FXML private ChoiceBox<Months_TR> monthChoiceBox;
    @FXML private ChoiceBox<Integer> yearChoiceBox;

    @FXML
    private void initialize() {
        IntStream.range(START_YEAR, STOP_YEAR).forEach(yearChoiceBox.getItems()::add);
        Arrays.stream(Months_TR.values()).forEach(monthChoiceBox.getItems()::add);
        SessionFactoryManager.getCostTypes().forEach(costTypeChoiceBox.getItems()::add);
    }

    @FXML
    private void clickGetButton()
    {
        var month  = monthChoiceBox.getSelectionModel().getSelectedItem();
        var year      = yearChoiceBox.getSelectionModel().getSelectedItem();
        var type = costTypeChoiceBox.getSelectionModel().getSelectedItem();

        var sum = SessionFactoryManager.getSumOfTypesBankFlow(type.getCostType(), year, month.ordinal());

        sum = sum == null ? BigDecimal.ZERO : sum;

        sumTextField.setText(sum + " ₺");
    }
}
