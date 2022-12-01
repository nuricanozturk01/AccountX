package com.example.accountingX.Controller;

import com.example.accountingX.App;
import com.example.accountingX.Entity.Months_TR;
import com.example.accountingX.Entity.User;
import com.example.accountingX.Entity.DateMapper;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.Constant;
import com.example.accountingX.util.UtilFX;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class CostFormListController
{
    @FXML
    private ChoiceBox<User> userChoiceBox;
    @FXML
    private ListView<DateMapper> costFormListView;
    private User user;
    @FXML
    private void initialize()
    {
        // add the users to choice box
        SessionFactoryManager.getUserList().forEach(userChoiceBox.getItems()::add);
        // When you click on the item in listview, this method get the cost form table
        costFormListView.getSelectionModel().selectedItemProperty().addListener(this::listViewListener);
        // String format of ListView
        costFormListView.setCellFactory(e -> getListCell());
    }

    private void listViewListener(ObservableValue<? extends DateMapper> changed, DateMapper oldVal, DateMapper date)
    {
        try
        {
            // If when you clicked the non-date area, program did not get the table.
            if (date == null)
                throw new Exception();

            var loader = new FXMLLoader(App.class.getResource("masraf_form.fxml"));

            UtilFX.initStage("Masraf Formu",new Stage(),new Scene(loader.load()),false, Constant.ICON);
            CostFormTableController controller = loader.getController();

            var choice = SessionFactoryManager.getCostFormsWithMonth(user.getUser_pk_id(), date);

            controller.setList(choice);
            controller.setUser(user);
        }
        catch (Exception ignored) {
        }
    }
    private ListCell<DateMapper> getListCell()
    {
        var months = Months_TR.values();
        return new ListCell<>() {
            @Override
            public void updateItem(DateMapper item, boolean empty) {
                super.updateItem(item, empty);
                setText(item != null ? months[item.getMonth()] + " " + item.getYear() : null);
            }
        };
    }

    @FXML
    @SuppressWarnings("all")
    private void clickGetButton()
    {
        SessionFactoryManager.cleanDateMapper();

        // Get User by name and surname
        user = userChoiceBox.getSelectionModel().getSelectedItem();

        var dates = SessionFactoryManager.sortCostFormsByDate(user.getUser_pk_id());

        costFormListView.getItems().clear();

        dates.forEach(costFormListView.getItems()::add);
    }
}
