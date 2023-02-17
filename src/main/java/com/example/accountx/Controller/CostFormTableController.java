package com.example.accountx.Controller;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.CostForm;
import com.example.accountx.Entity.Months_TR;
import com.example.accountx.Entity.User;
import com.example.accountx.Excel.ExportCostForm;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.Constant;
import com.example.accountx.util.UtilFX;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CostFormTableController
{
    private List<CostForm> list;
    private User user;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<CostForm> costFormTable;
    @FXML
    private TableColumn<CostForm, LocalDate> dateColumnColumn;
    @FXML
    private TableColumn<CostForm, Integer> billingNoColumn;
    @FXML
    private TableColumn<CostForm, String> descriptionColumn;
    @FXML
    private TableColumn<CostForm, String> costTypeColumn;
    @FXML
    private TableColumn<CostForm, BigDecimal> invoiceAmountColumn;
    @FXML
    private TableColumn<CostForm, Double> kdvColumn;
    @FXML
    private TableColumn<CostForm, BigDecimal> totalInvoiceColumn;
    @FXML
    private TableColumn<CostForm, String> officerColumn;
    @FXML
    private TableColumn<CostForm, String> companyColumn;

    private BigDecimal sum;

    @FXML
    private void initialize()
    {
        // set of CellValueFactory
        dateColumnColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        billingNoColumn.setCellValueFactory(new PropertyValueFactory<>("BillingNumber"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        costTypeColumn.setCellValueFactory(new PropertyValueFactory<>("CostType"));
        invoiceAmountColumn.setCellValueFactory(new PropertyValueFactory<>("InvoiceAmount"));
        totalInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalInvoice"));
        kdvColumn.setCellValueFactory(new PropertyValueFactory<>("Kdv"));
        officerColumn.setCellValueFactory(new PropertyValueFactory<>("ExpenditureOfficer"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("Company"));
        // Title of listview formatted date
        dateColumnColumn.setCellFactory((column) -> getFormattedDate());
    }

    public void setList(List<CostForm> list)
    {
        this.list = list;
        fillTable();
    }
    public void setUser(User user)
    {
        this.user = user;
    }
    private TableCell<CostForm, LocalDate> getFormattedDate()
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
    private BigDecimal getSumOfCost()
    {
        return list.stream().map(CostForm::getTotalInvoice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private void fillTable()
    {
        sum = getSumOfCost();

        var stage = (Stage) rootPane.getScene().getWindow();

        stage.setTitle(stage.getTitle() + "     Toplam: " + sum.toString() + " ₺");

        costFormTable.setItems(FXCollections.observableList(list));
    }
    @FXML
    private void clickExportExcel()
    {
        var year = list.get(0).getDate().getYear();
        var month = list.get(0).getDate().getMonthValue();
        var advanceAmount = SessionFactoryManager.getAdvanceAmounts(user.getUser_pk_id(), year, month);

        var fileName = user.getName() + "_" + user.getSurname() + "[" + year + " - " + Months_TR.values()[month] +"]";


        var fileChooser = new FileChooser();
        fileChooser.setTitle("Nereye Kaydedilecek?");
        fileChooser.setInitialFileName(fileName + ".xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "xlsx"));
        var selectedFile = fileChooser.showSaveDialog(new Stage());

        if (advanceAmount == null) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen önce avans miktarını giriniz!", ButtonType.OK);
            return;
        }

        var advance_amount = advanceAmount.stream().map(AdvanceAmount::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        var excel = new ExportCostForm(fileName, Constant.EXCEL_COST_FORM_TITLES, sum, advance_amount, advanceAmount,selectedFile.getParent(),list.get(0).getDate());

        list.stream().map(CostForm::generateExcelFormat).forEach(excel::buffer);

        excel.export();
    }
}
