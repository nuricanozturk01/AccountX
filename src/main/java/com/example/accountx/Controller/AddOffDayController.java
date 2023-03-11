package com.example.accountx.Controller;

import com.example.accountx.Entity.OffDay;
import com.example.accountx.Entity.OffDayType;
import com.example.accountx.Entity.User;
import com.example.accountx.Exceptions.MaxOffDayException;
import com.example.accountx.Exceptions.MinOffDayException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.LongStream;

import static com.example.accountx.util.Constant.MAX_OFF_DAY;
@SuppressWarnings("all")
public class AddOffDayController 
{
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ChoiceBox<User> personChoiceBox;
    @FXML
    private ChoiceBox<OffDayType> reasonChoiceBox;
    @FXML
    private DatePicker finishDatePicker;

    @FXML
    private void initialize()
    {
        startDatePicker.setValue(LocalDate.now());
        finishDatePicker.setValue(LocalDate.now());
        var user = SessionFactoryManager.getUserList();
        var reasons = SessionFactoryManager.getReasonList();

        user.forEach(personChoiceBox.getItems()::add);
        reasons.forEach(reasonChoiceBox.getItems()::add);
    }
    @FXML
    private void pressSave(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickSaveButton();
    }

    @FXML
    private void clickSaveButton()
    {
        try
        {
            var startDate = startDatePicker.getValue();
            var finishDate = finishDatePicker.getValue();
            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            var reason = reasonChoiceBox.getSelectionModel().getSelectedItem();

            var range = ChronoUnit.DAYS.between(startDate, finishDate);

            if (range < 0) throw new MinOffDayException("En az 1 gün izin verebilirsiniz!");
            if (range > MAX_OFF_DAY) throw new MaxOffDayException("En fazla " + MAX_OFF_DAY +" gün izin girebilirsiniz!!!");
            if (range == 0)  range++;

            LongStream.range(0, range).forEach(i -> enterOffDay(startDate.plusDays(i), user, reason));

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"İzin başarıyla girildi!", ButtonType.OK);
        }
        catch (MaxOffDayException max) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, max.getMessage(), ButtonType.OK);
        }
        catch (MinOffDayException min) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, min.getMessage(), ButtonType.OK);
        }
        catch (Exception ex) {
            var str = "Lütfen Geçerli Bilgiler Giriniz!!!";
            UtilFX.alertScreen(Alert.AlertType.ERROR, str, ButtonType.OK);
        }
    }
    private void enterOffDay(LocalDate date, User user, OffDayType reason) {
        SessionFactoryManager.add(new OffDay(user, date, reason.getType()));
    }
}
