package com.example.accountingX.Controller;

import com.example.accountingX.Entity.Months_TR;
import com.example.accountingX.Entity.OffDay;
import com.example.accountingX.Entity.User;
import com.example.accountingX.Excel.ExportOffDayTable;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.accountingX.util.Constant.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
public class WorkingDayController
{
    @FXML
    private Label sumOfOffDay;
    @FXML
    private ChoiceBox<User> personChoiceBox;
    @FXML
    private ChoiceBox<Integer> yearChoiceBox;
    @FXML
    private ChoiceBox<Months_TR> monthChoiceBox;
    @FXML
    private ListView<OffDay> costFormListView;
    @FXML
    private Label sumOfWorkingDay;



    @FXML
    private void initialize()
    {
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        Arrays.stream(Months_TR.values()).forEach(monthChoiceBox.getItems()::add);
        IntStream.range(START_YEAR, STOP_YEAR).forEach(yearChoiceBox.getItems()::add);
    }

    @FXML
    private void clickSaveButton()
    {
        try {

            var user = personChoiceBox.getSelectionModel().getSelectedItem();
            int year = yearChoiceBox.getSelectionModel().getSelectedItem();
            var month = monthChoiceBox.getSelectionModel().getSelectedItem();

            List<OffDay> list;
            if (month == null || month == Months_TR.NONE)
                list = SessionFactoryManager.getOffDaysByYearAndUser(user.getUser_pk_id(), year);

            else list = SessionFactoryManager.getOffDays(user.getUser_pk_id(), year, month);

            costFormListView.getItems().clear();

            if (list == null || list.size() == 0) {
                costFormListView.getItems().add(new OffDay());
                sumOfWorkingDay.setText("N/A");
                sumOfOffDay.setText("N/A");
                return;
            }

            list.forEach(costFormListView.getItems()::add);

            var workingDays = list.get(0).getOff_day_date().getMonth().maxLength() - list.size();

            if (month != null && month != Months_TR.NONE) {
                sumOfWorkingDay.setText("" + workingDays);
                sumOfOffDay.setText("" + list.size());
            }


        }
        catch (Exception ignored) {

        }
    }


    @FXML
    private void clickExportOffDay()
    {
        var month = monthChoiceBox.getSelectionModel().getSelectedItem();
        if (month != null && month != Months_TR.NONE)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Y??ll??k ??zin ????kt??s?? almak i??in Ay'?? Se??memelisiniz yada NONE olarak se??melisiniz....", ButtonType.OK);
            return;
        }
        var user = personChoiceBox.getSelectionModel().getSelectedItem();
        var userName = user.getName() + " " + user.getSurname();
        var year = yearChoiceBox.getSelectionModel().getSelectedItem();
        var fileName = user.getName() + "_" + user.getSurname() + " [" + year + "]";
        var file = UtilFX.fileChoose(fileName, "Excel", ".xlsx");

        var export = new ExportOffDayTable(fileName, 2, new String[]{"Tarih","??zin Tipi"},file.getParent(),
                SessionFactoryManager.getOffDaysByYearAndUser(user.getUser_pk_id(), year), userName);
        export.export();
    }
}
