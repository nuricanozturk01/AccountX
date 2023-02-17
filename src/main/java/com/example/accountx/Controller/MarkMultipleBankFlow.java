package com.example.accountx.Controller;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.TreeSet;

public class MarkMultipleBankFlow
{
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;

    private TableView<BankFlow> bankFlowTable;
    private TreeSet<BankFlow> markedBankFlowTreeSet;


    public void setBankFlowTable(TableView<BankFlow> bankFlowTable)
    {
        this.bankFlowTable = bankFlowTable;
    }

    @FXML
    private void clickMark()
    {
        try
        {
            var startDate = startDatePicker.getValue();
            var finishDate = finishDatePicker.getValue();

            if (startDate == null || finishDate == null)
                throw new EmptyFieldException("Lütfen tarihleri giriniz...");

            if (startDate.isAfter(finishDate))
                throw new EmptyFieldException("Lütfen tarihleri kontrol ediniz...");

           bankFlowTable.getItems().stream()
                   .filter(bf -> filter(bf, startDate, finishDate))
                   .toList()
                   .forEach(this::mark);

           bankFlowTable.refresh();
        }
        catch (EmptyFieldException e)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
        catch (Exception ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Silinirken bir hata oluştu...", ButtonType.OK);
        }
    }

    private void mark(BankFlow bankFlow)
    {
        if (bankFlow.isMark()) {
            bankFlow.setMark(false);
            markedBankFlowTreeSet.remove(bankFlow);
        } else {
            bankFlow.setMark(true);
            markedBankFlowTreeSet.add(bankFlow);
        }
    }

    private boolean filter(BankFlow bf, LocalDate startDate, LocalDate finishDate)
    {
        return (bf.getDate().isAfter(startDate) || bf.getDate().isEqual(startDate))
                                                 &&
                (bf.getDate().isBefore(finishDate) || bf.getDate().isEqual(finishDate));
    }

    public void setBankFlows(TreeSet<BankFlow> markedBankFlowTreeSet)
    {
        this.markedBankFlowTreeSet = markedBankFlowTreeSet;
    }
}
