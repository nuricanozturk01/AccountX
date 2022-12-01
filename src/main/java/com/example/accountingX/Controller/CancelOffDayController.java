package com.example.accountingX.Controller;

import com.example.accountingX.Entity.User;
import com.example.accountingX.Exceptions.EmptyFieldException;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;

public class CancelOffDayController {
    @FXML private DatePicker startDatePicker;
    @FXML private ChoiceBox<User> personChoiceBox;
    @FXML private DatePicker finishDatePicker;

    @FXML
    private void initialize() {
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        startDatePicker.setValue(LocalDate.now());
        finishDatePicker.setValue(LocalDate.now());
    }
    @FXML
    private void clickSaveButton() {

        try {
            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            var startDate = startDatePicker.getValue();
            var finishDate = finishDatePicker.getValue();

            if (user == null)
                throw new EmptyFieldException("Lütfen Kişiyi Seçiniz...");
            if (startDate.isAfter(finishDate))
                throw new IllegalAccessException();

            if (SessionFactoryManager.removeOffDays(user.getUser_pk_id(), startDate, finishDate)) {
                UtilFX.alertScreen(Alert.AlertType.INFORMATION, "İzin Günü Başarıyla Silindi...", ButtonType.OK);
            }
            else UtilFX.alertScreen(Alert.AlertType.INFORMATION, "İzin Günü Bulunamadı...", ButtonType.OK);
        }
        catch (IllegalAccessException ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen geçerli bir tarih aralığı seçiniz...", ButtonType.OK);
        }
        catch (EmptyFieldException e) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }
    @FXML
    private void pressSave(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickSaveButton();
    }

}
