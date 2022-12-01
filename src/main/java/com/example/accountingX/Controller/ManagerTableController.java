package com.example.accountingX.Controller;

import com.example.accountingX.App;
import com.example.accountingX.Entity.FundFlow;
import com.example.accountingX.Entity.OtherFlow;
import com.example.accountingX.Entity.TreasureFlow;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.Constant;
import com.example.accountingX.util.UtilFX;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("all")
public class ManagerTableController <T> {
    @FXML private TableView<T> table;
    @FXML private TableColumn<T, LocalDate> dateTableColumn;
    @FXML private TableColumn<T, String> descriptionTableColumn;
    @FXML private TableColumn<T, BigDecimal> debtTableColumn;
    @FXML private TableColumn<T, Double> exchangeRateTableColumn;
    @FXML private TableColumn<T, BigDecimal> resultAmountTableColumn;
    @FXML private RadioButton fundFLowRadioButton;
    @FXML private RadioButton otherFlowRadioButton;
    @FXML private RadioButton treasureRadioButton;

    @FXML
    private void initialize() {

        var group = new ToggleGroup();
        fundFLowRadioButton.setToggleGroup(group);
        otherFlowRadioButton.setToggleGroup(group);
        treasureRadioButton.setToggleGroup(group);
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateTableColumn.setCellFactory((column) -> getFormattedDate());
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        debtTableColumn.setCellValueFactory(new PropertyValueFactory<>("Debt"));
        exchangeRateTableColumn.setCellValueFactory(new PropertyValueFactory<>("ExchangeRate"));
        resultAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("ResultAmount"));
        group.selectedToggleProperty().addListener(this::groupListener);
    }

    private void groupListener(Observable observable)
    {
        if (fundFLowRadioButton.isSelected())
            table.setItems((ObservableList<T>) FXCollections.observableArrayList(SessionFactoryManager.getFundFlows()));
        else if (treasureRadioButton.isSelected())
            table.setItems((ObservableList<T>) FXCollections.observableArrayList(SessionFactoryManager.getTreasureFlows()));
        else if (otherFlowRadioButton.isSelected())
            table.setItems((ObservableList<T>) FXCollections.observableArrayList(SessionFactoryManager.getOtherFlows()));
    }

    @FXML
    private void clickUpdate() throws IOException {
        var selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
            return;

        var item = selectedItem;

        if (fundFLowRadioButton.isSelected())
            item = (T) SessionFactoryManager.get(FundFlow.class, ((FundFlow)selectedItem).getFund_flow_pk_id());
        else if (treasureRadioButton.isSelected())
            item = (T) SessionFactoryManager.get(TreasureFlow.class, ((TreasureFlow)selectedItem).getTreasure_flow_pk_id());

        else if (otherFlowRadioButton.isSelected())
            item = (T) SessionFactoryManager.get(OtherFlow.class, ((OtherFlow)selectedItem).getOther_flow_pk_id());

        var loader = new FXMLLoader(App.class.getResource("yönetici_özeti_güncelleme_form.fxml"));

        UtilFX.initStage("Yönetici Özeti Güncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        UpdateManagerReportController updateManagerReportController = loader.getController();
        updateManagerReportController.setSelectedItem(item);
        updateManagerReportController.setController(this);
    }

    public<T> TableCell<T, LocalDate> getFormattedDate()
    {
        return new TableCell<>()
        {
            @Override
            protected void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? null : DateTimeFormatter.ofPattern("dd/MM/yyyy").format(item));
            }
        };
    }

    public void updateTable() {
        initialize();
    }
}
