package com.example.accountingX.Controller;

import com.example.accountingX.App;
import com.example.accountingX.Entity.*;
import com.example.accountingX.Excel.ExportManagerReport;
import com.example.accountingX.Excel.ReadExcel;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.Service.MainScreenService;
import com.example.accountingX.util.Constant;
import com.example.accountingX.util.UtilFX;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MainScreenController
{
    @FXML
    private TableColumn<CostForm, Boolean> voucherTableColumn;
    @FXML
    private TableColumn<BankFlow, Integer> idColumnBankFlow;
    @FXML
    private Tab costFormTab;
    @FXML
    private Tab bankFlowTab;
    @FXML
    private Tab userTab;
    @FXML
    private Tab costTypeTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button filterButtonDate;
    @FXML
    private Button filterButtonCostType;
    @FXML
    private Button searchNameButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TableColumn<CostType, String> costTypeColumnCostTypeTable;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private ChoiceBox<String> costTypeChoiceBox;
    @FXML
    private TableColumn<CostForm, LocalDate> dateColumn;
    @FXML
    private TableColumn<CostForm,String> caseFlowColumn;
    @FXML
    private TableColumn<CostForm,String> costTypeColumn;
    @FXML
    private TableColumn<CostForm,BigDecimal> costColumn;
    @FXML
    private TableColumn<BankFlow, LocalDate> dateColumnBank;
    @FXML
    private TableColumn<BankFlow,String> caseFlowColumnBank;
    @FXML
    private TableColumn<BankFlow,String> costTypeColumnBank;
    @FXML
    private TableColumn<BankFlow,BigDecimal> costColumnBank;
    @FXML
    private TableColumn<User,String> nameColumn;
    @FXML
    private TableColumn<User,String> surnameColumn;
    @FXML
    private TableView<CostForm> costFormTable;
    @FXML
    private TableView<BankFlow> bankFlowTable;
    @FXML
    private TableView<User> UsersTable;
    @FXML
    private TableView<CostType> costTypeTable;
    @FXML
    private TableColumn<CostForm,Integer> noColumn;
    private MainScreenService service;

    @FXML
    private void initialize()
    {
        service = new MainScreenService.ServiceBuilder()
                .setBankFlowTable(bankFlowTable).setCaseFlowColumn(caseFlowColumn)
                .setCaseFlowColumnBank(caseFlowColumnBank).setCostColumn(costColumn).setCostColumnBank(costColumnBank)
                .setCostFlowTable(costFormTable).setCostTypeChoiceBox(costTypeChoiceBox)
                .setCostTypeColumn(costTypeColumn).setCostTypeColumnBank(costTypeColumnBank)
                .setFinishDatePicker(finishDatePicker).setDateColumn(dateColumn)
                .setIdColumnBankFlow(idColumnBankFlow).setTabPane(tabPane)
                .setFilterButtonDate(filterButtonDate).setFilterButtonCostType(filterButtonCostType)
                .setSearchNameButton(searchNameButton).setNameTextField(nameTextField)
                .setCostTypeColumnCostTypeTable(costTypeColumnCostTypeTable).setStartDatePicker(startDatePicker)
                .setDateColumnBank(dateColumnBank).setNameColumn(nameColumn)
                .setSurnameColumn(surnameColumn).setUsersTable(UsersTable)
                .setCostTypeTable(costTypeTable).setNoColumn(noColumn)
                .setVoucherTableColumn(voucherTableColumn)
                .build();

        service.init();
    }

    public MainScreenService getService()
    {
        return service;
    }

    /**
     * FILTER
     */
    @FXML
    private void filterSearchName()
    {
        var name = nameTextField.getText();

        if (costFormTab.isSelected())
            costFormTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameCostForms(name)));

        if (userTab.isSelected())
            UsersTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameUser(name)));

        if (costTypeTab.isSelected())
            costTypeTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameCostType(name)));

        if (bankFlowTab.isSelected())
            bankFlowTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameBankFlows(name)));
    }
    @FXML
    private void filterByDate()
    {
        if (costFormTab.isSelected())
            costFormTable.setItems(FXCollections.observableList
                    (SessionFactoryManager.filterDateCostForms(startDatePicker.getValue(), finishDatePicker.getValue())));

        if (bankFlowTab.isSelected())
            bankFlowTable.setItems(FXCollections.observableList
                    (SessionFactoryManager.filterDateBankFlows(startDatePicker.getValue(), finishDatePicker.getValue())));
    }
    @FXML
    private void clickFilterCostType()
    {
        var filterType = costTypeChoiceBox.getSelectionModel().getSelectedItem();

        if (costFormTab.isSelected())
            costFormTable.setItems(FXCollections.
                    observableList(SessionFactoryManager.filterCostTypeCostFlows(filterType)));

        if (bankFlowTab.isSelected())
            bankFlowTable.setItems(FXCollections.
                    observableList(SessionFactoryManager.filterCostTypeBankFlows(filterType)));
    }
    @FXML
    private void resetTableButton()
    {
        service.initCostFlowTable();
        service.initUsersTable();
        service.initCostTypeTable();
        service.initBankFlowTable();
        startDatePicker.setValue(LocalDate.now());
        finishDatePicker.setValue(LocalDate.now());
        nameTextField.setText(null);
    }
    /**
     * Open Form methods
     * @throws IOException
     */
    @FXML
    private void clickCostForms() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("masraf_form_list.fxml"));
        UtilFX.initStage("Masraf Formu Kişi Listesi", new Stage(), new Scene(loader.load()),false, Constant.ICON);
    }
    @FXML
    private void clickAddCostType() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("gider_türü_ekleme_formu.fxml"));
        UtilFX.initStage("Gider Türü Ekleme Formu", new Stage(), new Scene(loader.load()),false, Constant.ICON);
        CostTypeController controller = loader.getController();
        controller.setMainScreenController(this);
    }
    @FXML
    @SuppressWarnings("all")
    private void clickAddKDV() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("kdv_ekleme_formu.fxml"));
        UtilFX.initStage("KDV Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    private void clickAddOfDayTypeMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("izin_günü_tipi_ekleme_form.fxml"));
        UtilFX.initStage("İzin Günü Tipi Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    private void clickWorkingDaysMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("calisilan_günler_form.fxml"));
        UtilFX.initStage("Çalışılan Günler", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    private void clickAddOfDayMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("izin_günü_ekleme_formu.fxml"));
        UtilFX.initStage("İzin Günü Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    @SuppressWarnings("all")
    private void clickAddBankForm() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("banka_hareket_form.fxml"));
        UtilFX.initStage("Banka Hareketleri", new Stage(), new Scene(root),false, Constant.ICON);
    }

    @FXML
    @SuppressWarnings("all")
    private void clickImportBankFlows() throws IOException
    {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        var selectedFile = fileChooser.showOpenDialog(new Stage());
        var path = selectedFile.getAbsolutePath();

        var insertToDB = new ReadExcel(path);
        insertToDB.read();
        System.out.println("Finish!");
    }

    @FXML
    private void clickAddCostForm() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("masraf_ekleme_form.fxml"));
        UtilFX.initStage("Masraf Ekleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
        CostFormController controller = loader.getController();
        controller.setMainScreenController(this);

    }

    @FXML
    private void clickAddPerson() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("kisi_ekle.fxml"));
        //Parent root  = FXMLLoader.load(App.class.getResource("kisi_ekle.fxml"));

        UtilFX.initStage("Kişi Ekle", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        AddPersonController controller = loader.getController();

        controller.setMainScreenController(this);
    }
    @FXML
    private void clickAdvanveMoney() throws IOException{
        var loader = new FXMLLoader(App.class.getResource("avans_verme_güncelleme_form.fxml"));
        UtilFX.initStage("Avans Verme - Güncelleme", new Stage(), new Scene(loader.load()),false, Constant.ICON);
    }
    @FXML
    private void clickSumOfTotalCost() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("toplam_gider.fxml"));

        UtilFX.initStage("Toplam Gider", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }
    @FXML
    private void clickUpdateCostType() throws IOException
    {
        var type =  costTypeTable.getSelectionModel().getSelectedItem();

        if (type == null)
            return;

        var loader = new FXMLLoader(App.class.getResource("gider_türü_güncelleme_form.fxml"));
        UtilFX.initStage("Gider Türü Güncelleme", new Stage(), new Scene(loader.load()), false, Constant.ICON);
        UpdateCostTypeController controller = loader.getController();
        controller.setExistingCostType(SessionFactoryManager.getCostType(type.getCostType()));
        controller.setController(this);

    }

    @FXML
    private void clickCancelOffDay() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("izin_iptal_form.fxml"));

        UtilFX.initStage("İzin İptal Etme Formu ", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }


    @FXML
    private void clickSumOfPersonWithYear() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("kisi_yıl_toplam.fxml"));

        UtilFX.initStage("Kişi - Yıl", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }
   /* @FXML
    private void clickdeletePerson()
    {
       try {
           var user = service.getUsersTable().getSelectionModel().getSelectedItem();

           if (user == null)
               throw new Exception();

           SessionFactoryManager.delete(user);
           service.updateUserTable();
       }
       catch (Exception ignored) {
       }
    }*/
    /**
     * Update Operations
     */
    @FXML
    private void clickUpdatePerson(){
       try
       {
           if (UsersTable.getSelectionModel().getSelectedItem() == null)
               throw new Exception();

           var loader = new FXMLLoader(App.class.getResource("kişi_güncelleme_form.fxml"));
           UtilFX.initStage("Kişi Güncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

           UpdatePersonController controller = loader.getController();
           controller.setMainScreenController(this);
       }
       catch (Exception ignore) {

       }
    }
    @FXML
    private void clickUpdateCostFlow()
    {
        try {
            if (costFormTable.getSelectionModel().getSelectedItem() == null)
                throw new Exception();

            var loader = new FXMLLoader(App.class.getResource("masraf_güncelleme_form.fxml"));
            UtilFX.initStage("Masraf Güncelleme Formu", new Stage(), new Scene(loader.load()),false, Constant.ICON);


            var form = costFormTable.getSelectionModel().getSelectedItem();

            UpdateCostFormController controller = loader.getController();
            controller.setMainScreenController(this);
            controller.setCostForm(form);

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void clickUpdateBankFlow() throws Exception
    {
        if (bankFlowTable.getSelectionModel().getSelectedItem() == null)
            throw new Exception();

        var loader = new FXMLLoader(App.class.getResource("banka_hareket_form.fxml"));

        UtilFX.initStage("Banka Hareketi Güncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        var flow = bankFlowTable.getSelectionModel().getSelectedItem();

        UpdateBankFlowController updateBankFlowController = loader.getController();
        updateBankFlowController.setController(this);
        updateBankFlowController.setBankFlow(SessionFactoryManager.get(BankFlow.class, flow.getBank_flow_pk_id()));
    }

    @FXML
    private void clickAddManagerReport() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("yönetici_özeti_girdi_ekleme_form.fxml"));

        UtilFX.initStage("Yönetici Özeti Girdi Ekleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }


    @FXML
    private void clickExportReport() {
        var localDate = LocalDate.now();
        var fileName = localDate.getYear() + "-" + Months_TR.values()[localDate.getMonthValue()] + " Yönetici Özeti";
        var file = UtilFX.fileChoose(fileName, "Excel", ".xlsx");

        var titles = new String[] {"Tarih", "Açıklama", "Tutar", "Döviz Kuru", "Tutar(₺)"};

        var export = new ExportManagerReport(fileName, 5, titles, file.getParent());
        export.export();



    }
    @FXML
    private void clickManagerSummaryTable() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("yönetici_özeti_tablo_form.fxml"));

        UtilFX.initStage("Yönetici Özeti Tablosu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickSumOfPersonWithMonth() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("kisi_ay_toplam_form.fxml"));

        UtilFX.initStage("Kişi - Ay Toplam Harcama", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickcostTypeBankFlow() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("gider_türü_banka_hareketleri.fxml"));

        UtilFX.initStage("Gider Türü Banka Hareketleri", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

}