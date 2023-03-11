package com.example.accountx.Controller;

import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.commandcontroller.CommandController;
import com.example.accountx.command.dto.MultipleBankFlowDTO;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;

import static com.example.accountx.command.types.CommandName.MULTIPLE_BANK_FLOW;
import static com.example.accountx.command.types.CommandType.REMOVE;

public class RemoveMultipleBankFlow
{
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;

    @FXML
    private void clickRemove()
    {
        try
        {
            var startDate = startDatePicker.getValue();
            var finishDate = finishDatePicker.getValue();
            if (startDate == null || finishDate == null)
                throw new EmptyFieldException("Lütfen tarihleri giriniz...");

            if (startDate.isAfter(finishDate))
                throw new EmptyFieldException("Lütfen tarihleri kontrol ediniz...");

            var list = SessionFactoryManager.filterDateBankFlows(startDate, finishDate);

            CommandController
                    .getInstance()
                    .acceptRemove(MULTIPLE_BANK_FLOW, new MultipleBankFlowDTO(list, REMOVE)); // FOR UNDO

            var result = SessionFactoryManager.removeBankFlowsByDateRange(startDate, finishDate);

            if (result)
                UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Başarı ile silindi...", ButtonType.OK);
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
}
