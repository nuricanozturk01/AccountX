package com.example.accountx.Controller;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.Entity.CostType;
import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

import java.util.TreeSet;

public class MultipleSelectCostTypeController
{
    public ChoiceBox<CostType> costTypeChoiceBox;

    private TreeSet<BankFlow> m_bankFlows;
    private TableView<BankFlow> bankFlowTable;

    @FXML
    private void initialize()
    {
        SessionFactoryManager.getCostTypes().forEach(costTypeChoiceBox.getItems()::add);
    }

    public void setBankFlows(TreeSet<BankFlow> bankFlows)
    {
        m_bankFlows = bankFlows;
    }
    @FXML
    private void clickUpdate()
    {
        try
        {
            var type = costTypeChoiceBox.getSelectionModel().getSelectedItem();

            if (m_bankFlows.isEmpty())
                throw new EmptyFieldException("Banka Hareketi Seçmediniz....");

            m_bankFlows.forEach(bf -> bf.setCostType(type.getCostType()));
            SessionFactoryManager.updateAll(m_bankFlows);

            bankFlowTable.refresh();

            m_bankFlows.forEach(bf -> {bf.setMark(false);});
            m_bankFlows.clear();
        }
        catch (EmptyFieldException e)
        {
            UtilFX.alertScreen(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
        }
    }

    public void setTable(TableView<BankFlow> bankFlowTable)
    {
        this.bankFlowTable = bankFlowTable;
    }
}
