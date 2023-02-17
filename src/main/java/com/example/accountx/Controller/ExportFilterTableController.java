package com.example.accountx.Controller;

import com.example.accountx.Entity.*;
import com.example.accountx.Excel.ExportFilterBankFlow;
import com.example.accountx.Excel.ExportFilterCostForm;
import com.example.accountx.Excel.ExportFilterCostType;
import com.example.accountx.Excel.ExportFilterUsers;
import com.example.accountx.util.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExportFilterTableController
{
    @FXML
    private RadioButton costFormRadioButton;
    @FXML
    private RadioButton bankFlowRadioButton;
    @FXML
    private RadioButton costTypeRadioButton;
    @FXML
    private RadioButton personRadioButton;
    private TableView<User> userTable;
    private TableView<CostType> costTypeTable;
    private TableView<CostForm> costFormTable;
    private TableView<BankFlow> bankFlowTable;


    @FXML
    private void initialize()
    {
        var group = new ToggleGroup();
        costTypeRadioButton.setToggleGroup(group);
        costFormRadioButton.setToggleGroup(group);
        bankFlowRadioButton.setToggleGroup(group);
        personRadioButton.setToggleGroup(group);
    }

    public void setTables(TableView<User> userTable, TableView<CostType> costTypeTable, TableView<CostForm> costFormTable, TableView<BankFlow> bankFlowTable)
    {
        this.userTable = userTable;
        this.costFormTable = costFormTable;
        this.costTypeTable = costTypeTable;
        this.bankFlowTable = bankFlowTable;
    }
    @FXML
    private void clickExport()
    {
        if (costFormRadioButton.isSelected())
            exportCostFormTable();
        if (bankFlowRadioButton.isSelected())
            exportBankFlowTable();
        if (personRadioButton.isSelected())
            exportUserTable();
        if (costTypeRadioButton.isSelected())
            exportCostTypeTable();
    }

    private String getFilePath(String fileName)
    {

        var fileChooser = new FileChooser();
        fileChooser.setTitle("Nereye Kaydedilecek?");
        fileChooser.setInitialFileName(fileName + ".xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "xlsx"));
        var selectedFile = fileChooser.showSaveDialog(new Stage());

        return selectedFile.getParent();
    }

    private String getFileName(String name)
    {
        var date = LocalDate.now();
        return name + " [" + date.format(DateTimeFormatter.ISO_LOCAL_DATE)   + "]";
    }
    private void exportCostTypeTable()
    {
       var fileName = getFileName("Filtreli Gider Türü");

       var path = getFilePath(fileName);
       var list = costTypeTable.getItems();
       var excel = new ExportFilterCostType(fileName, Constant.EXCEL_COST_TYPES_TITLES.length, Constant.EXCEL_COST_TYPES_TITLES, path);

       list.stream().map(CostType::generateExcelFormat).forEach(excel::buffer);
       excel.export();
    }

    private void exportUserTable()
    {
        var fileName = getFileName("Filtreli Kullanıcılar");
        var path = getFilePath(fileName);
        var list = userTable.getItems();
        var excel = new ExportFilterUsers(fileName, Constant.EXCEL_USERS_TITLES.length, Constant.EXCEL_USERS_TITLES, path);

        list.stream().map(User::generateExcelFormat).forEach(excel::buffer);
        excel.export();
    }

    private void exportBankFlowTable()
    {

        var fileName = getFileName("Filtreli Banka Hareketi");
        var path = getFilePath(fileName);
        var list = bankFlowTable.getItems();
        var sum = list.stream().map(BankFlow::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        var excel = new ExportFilterBankFlow(fileName, Constant.EXCEL_BANK_FLOW_TITLES.length, Constant.EXCEL_BANK_FLOW_TITLES, path, sum);

        list.stream().map(BankFlow::generateExcelFormat).forEach(excel::buffer);
        excel.export();

    }

    private void exportCostFormTable()
    {

        var fileName = getFileName("Filtreli Masraf Formu");
        var path = getFilePath(fileName);
        var list = costFormTable.getItems();
        var sum = list.stream().map(CostForm::getTotalInvoice).reduce(BigDecimal.ZERO, BigDecimal::add);
        var excel = new ExportFilterCostForm(fileName, Constant.EXCEL_COST_FORM_TITLES.length, Constant.EXCEL_COST_FORM_TITLES, path, sum);

        list.stream().map(CostForm::generateExcelFormat).forEach(excel::buffer);
        excel.export();
    }
}
