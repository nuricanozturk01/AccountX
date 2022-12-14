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
        UtilFX.initStage("Masraf Formu Ki??i Listesi", new Stage(), new Scene(loader.load()),false, Constant.ICON);
    }
    @FXML
    private void clickAddCostType() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("gider_t??r??_ekleme_formu.fxml"));
        UtilFX.initStage("Gider T??r?? Ekleme Formu", new Stage(), new Scene(loader.load()),false, Constant.ICON);
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
        Parent root  = FXMLLoader.load(App.class.getResource("izin_g??n??_tipi_ekleme_form.fxml"));
        UtilFX.initStage("??zin G??n?? Tipi Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    private void clickWorkingDaysMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("calisilan_g??nler_form.fxml"));
        UtilFX.initStage("??al??????lan G??nler", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    private void clickAddOfDayMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("izin_g??n??_ekleme_formu.fxml"));
        UtilFX.initStage("??zin G??n?? Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
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

        UtilFX.initStage("Ki??i Ekle", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        AddPersonController controller = loader.getController();

        controller.setMainScreenController(this);
    }
    @FXML
    private void clickAdvanveMoney() throws IOException{
        var loader = new FXMLLoader(App.class.getResource("avans_verme_g??ncelleme_form.fxml"));
        UtilFX.initStage("Avans Verme - G??ncelleme", new Stage(), new Scene(loader.load()),false, Constant.ICON);
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

        var loader = new FXMLLoader(App.class.getResource("gider_t??r??_g??ncelleme_form.fxml"));
        UtilFX.initStage("Gider T??r?? G??ncelleme", new Stage(), new Scene(loader.load()), false, Constant.ICON);
        UpdateCostTypeController controller = loader.getController();
        controller.setExistingCostType(SessionFactoryManager.getCostType(type.getCostType()));
        controller.setController(this);

    }

    @FXML
    private void clickCancelOffDay() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("izin_iptal_form.fxml"));

        UtilFX.initStage("??zin ??ptal Etme Formu ", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }


    @FXML
    private void clickSumOfPersonWithYear() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("kisi_y??l_toplam.fxml"));

        UtilFX.initStage("Ki??i - Y??l", new Stage(), new Scene(loader.load()), false, Constant.ICON);
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

           var loader = new FXMLLoader(App.class.getResource("ki??i_g??ncelleme_form.fxml"));
           UtilFX.initStage("Ki??i G??ncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

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

            var loader = new FXMLLoader(App.class.getResource("masraf_g??ncelleme_form.fxml"));
            UtilFX.initStage("Masraf G??ncelleme Formu", new Stage(), new Scene(loader.load()),false, Constant.ICON);


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

        UtilFX.initStage("Banka Hareketi G??ncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        var flow = bankFlowTable.getSelectionModel().getSelectedItem();

        UpdateBankFlowController updateBankFlowController = loader.getController();
        updateBankFlowController.setController(this);
        updateBankFlowController.setBankFlow(SessionFactoryManager.get(BankFlow.class, flow.getBank_flow_pk_id()));
    }

    @FXML
    private void clickAddManagerReport() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("y??netici_??zeti_girdi_ekleme_form.fxml"));

        UtilFX.initStage("Y??netici ??zeti Girdi Ekleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }


    @FXML
    private void clickExportReport() {
        var localDate = LocalDate.now();
        var fileName = localDate.getYear() + "-" + Months_TR.values()[localDate.getMonthValue()] + " Y??netici ??zeti";
        var file = UtilFX.fileChoose(fileName, "Excel", ".xlsx");

        var titles = new String[] {"Tarih", "A????klama", "Tutar", "D??viz Kuru", "Tutar(???)"};

        var export = new ExportManagerReport(fileName, 5, titles, file.getParent());
        export.export();



    }
    @FXML
    private void clickManagerSummaryTable() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("y??netici_??zeti_tablo_form.fxml"));

        UtilFX.initStage("Y??netici ??zeti Tablosu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickSumOfPersonWithMonth() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("kisi_ay_toplam_form.fxml"));

        UtilFX.initStage("Ki??i - Ay Toplam Harcama", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickcostTypeBankFlow() throws IOException {
        var loader = new FXMLLoader(App.class.getResource("gider_t??r??_banka_hareketleri.fxml"));

        UtilFX.initStage("Gider T??r?? Banka Hareketleri", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

}