package com.example.accountx.Controller;

import com.example.accountx.App;
import com.example.accountx.Entity.*;
import com.example.accountx.Excel.ExportManagerReport;
import com.example.accountx.Excel.ReadExcel;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.Service.MainScreenService;
import com.example.accountx.command.commandcontroller.CommandController;
import com.example.accountx.command.commands.CommandBankFlow;
import com.example.accountx.command.commands.CommandCostForm;
import com.example.accountx.command.commands.CommandMultipleBankFlow;
import com.example.accountx.command.dto.BankFlowDTO;
import com.example.accountx.command.dto.CostFormDTO;
import com.example.accountx.command.dto.MultipleBankFlowDTO;
import com.example.accountx.util.Constant;
import com.example.accountx.util.UtilFX;
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
import java.util.ArrayList;
import java.util.TreeSet;

import static com.example.accountx.command.types.CommandName.*;
import static com.example.accountx.command.types.CommandType.REMOVE;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class MainScreenController
{
    @FXML
    private TableColumn<User, BigDecimal> advanceAmountTableColumn;
    @FXML
    private Label sumLabel;
    @FXML
    private RadioButton selectAllRadioButton;
    @FXML
    private ChoiceBox<User> userChoiceBox;
    @FXML
    private TableColumn<BankFlow, Boolean> markTableColumn;
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
    private TextField nameTextField;
    @FXML
    private TableColumn<CostType, String> costTypeColumnCostTypeTable;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private ChoiceBox<CostType> costTypeChoiceBox;
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

    private final TreeSet<BankFlow> markedBankFlowTreeSet = new TreeSet<>();

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
                .setNameTextField(nameTextField)
                .setCostTypeColumnCostTypeTable(costTypeColumnCostTypeTable).setStartDatePicker(startDatePicker)
                .setDateColumnBank(dateColumnBank).setNameColumn(nameColumn)
                .setSurnameColumn(surnameColumn).setUsersTable(UsersTable)
                .setCostTypeTable(costTypeTable).setNoColumn(noColumn)
                .setVoucherTableColumn(voucherTableColumn)
                .setBankFlowMarkColumn(markTableColumn)
                .setUserChoiceBox(userChoiceBox)
                .setRadioButton(selectAllRadioButton)
                .setUserAdvanceAmountColumn(advanceAmountTableColumn)
                .build();

        service.init();

        // UNDO OPERATIONS
        var undoController = CommandController.getInstance();

        undoController.addCommand(MULTIPLE_BANK_FLOW, new CommandMultipleBankFlow(this));
        undoController.addCommand(COST_FORM, new CommandCostForm(this));
        undoController.addCommand(BANK_FLOW, new CommandBankFlow(this));
    }

    public MainScreenService getService()
    {
        return service;
    }

    @FXML
    private void clickSelectAllBankFlows()
    {

        if (selectAllRadioButton.isSelected())
        {
            bankFlowTable.getItems().forEach(item -> {
                if (item.isMark()) {
                    item.setMark(false);
                    markedBankFlowTreeSet.remove(item);
                }
                else
                {
                    item.setMark(true);
                    markedBankFlowTreeSet.add(item);
                }
            });
            bankFlowTable.refresh();
        }
        else
        {
            bankFlowTable.getItems().forEach(item -> {
                item.setMark(false);
                markedBankFlowTreeSet.remove(item);
            });
            bankFlowTable.refresh();
        }

    }

    /**
     * FILTER
     */

    private void filterCostForm()
    {
        var sb = new StringBuilder();
        sb.append("SELECT * FROM cost_form WHERE ");
        var startDate = startDatePicker.getValue();

        var finishDate = finishDatePicker.getValue();
        var word = nameTextField.getText();
        var user = userChoiceBox.getSelectionModel().getSelectedItem();
        var type = costTypeChoiceBox.getSelectionModel().getSelectedItem();
        if ((startDate != null && finishDate != null))
        {
            sb.append("date BETWEEN \'");sb.append(startDate); sb.append("\' AND \'"); sb.append(finishDate);
            sb.append("\'");
        }
        if (!word.isBlank() && !word.isEmpty())
        {
            var lower = word.toLowerCase();
            var upper = word.toUpperCase();

            if ((startDate != null && finishDate != null))
                sb.append(" AND ");
            sb.append("((\'");
            sb.append(lower);  sb.append("\' = "); sb.append("description OR \'");sb.append(upper);
            sb.append("\' = "); sb.append("description) OR (LOCATE(\'");sb.append(lower);sb.append("\',");
            sb.append("description) > 0");
            sb.append(" OR "); sb.append("LOCATE(\'");sb.append(upper);sb.append("\',");sb.append("description) > 0))");
        }
        if (user != null)
        {
            if (!word.isBlank() && !word.isEmpty() || (startDate != null && finishDate != null))
                sb.append(" AND ");
            sb.append("expenditureOfficer = \'");sb.append(user.getName()); sb.append(" "); sb.append(user.getSurname());
            sb.append("\'");
        }

        if (type != null)
        {
            if (!word.isBlank() && !word.isEmpty()|| (startDate != null && finishDate != null) || user != null)
                sb.append(" AND ");
            sb.append("costType = "); sb.append("\'");sb.append(type.getCostType());sb.append("\'");
        }
        sb.append(";");

        System.out.println(sb);

        var list = SessionFactoryManager.FilteredCostForms(sb.toString());

        if (list != null)
        {
            costFormTable.setItems(FXCollections.observableList(list));
            sumLabel.setText(getFormattedNumber(list.stream().map(CostForm::getTotalInvoice).reduce(BigDecimal.ZERO, BigDecimal::add)) + " ₺");
        }
    }
    @FXML
    private void clickFilter()
    {
        if (costFormTab.isSelected())
            filterCostForm();
        if (bankFlowTab.isSelected())
            filterBankFlow();
        if (costTypeTab.isSelected())
            costTypeTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameCostType(nameTextField.getText())));
        if (userTab.isSelected())
            UsersTable.setItems(FXCollections.observableList(SessionFactoryManager.filterNameUser(nameTextField.getText())));
    }

    private void filterBankFlow()
    {
        var sb = new StringBuilder();
        sb.append("SELECT * FROM bank_flow WHERE ");
        var startDate = startDatePicker.getValue();

        var finishDate = finishDatePicker.getValue();
        var word = nameTextField.getText();

        var type = costTypeChoiceBox.getSelectionModel().getSelectedItem();

        if ((startDate != null && finishDate != null))
        {
            sb.append("date BETWEEN \'");sb.append(startDate); sb.append("\' AND \'"); sb.append(finishDate);
            sb.append("\'");
        }

        if (!word.isBlank() && !word.isEmpty())
        {
            var lower = word.toLowerCase();
            var upper = word.toUpperCase();

            if ((startDate != null && finishDate != null))
                sb.append(" AND ");
            sb.append("((\'");
            sb.append(lower);  sb.append("\' = "); sb.append("caseFlow OR \'");sb.append(upper);
            sb.append("\' = "); sb.append("caseFlow) OR (LOCATE(\'");sb.append(lower);sb.append("\',");
            sb.append("caseFlow) > 0");
            sb.append(" OR "); sb.append("LOCATE(\'");sb.append(upper);sb.append("\',");sb.append("caseFlow) > 0))");
        }

        if (type != null)
        {
            if (!word.isBlank() && !word.isEmpty() || (startDate != null && finishDate != null))
                sb.append(" AND ");
            sb.append("costType = "); sb.append("\'");sb.append(type.getCostType());sb.append("\'");
        }
        sb.append(";");
        var list = SessionFactoryManager.FilteredBankFlows(sb.toString());

        if (list != null)
        {
            bankFlowTable.setItems(FXCollections.observableList(list));
            sumLabel.setText(getFormattedNumber(list.stream().map(BankFlow::getCost).reduce(BigDecimal.ZERO, BigDecimal::add)) + " ₺");
        }

    }
    @FXML
    private void resetTableButton()
    {
        service.initCostFlowTable();
        service.initUsersTable();
        service.initCostTypeTable();
        service.initBankFlowTable();
        startDatePicker.setValue(null);
        finishDatePicker.setValue(null);
        nameTextField.clear();
        costTypeChoiceBox.setValue(null);
        userChoiceBox.setValue(null);
        selectAllRadioButton.setSelected(false);
        bankFlowTable.getItems().forEach(item -> item.setMark(false));
        markedBankFlowTreeSet.clear();
        sumLabel.setText("N/A");
    }

    @FXML
    @SuppressWarnings("all")
    private void clickFilterExcelReport() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("filtreli_rapor_oluşturma_form.fxml"));
        UtilFX.initStage("Masraf Ekleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        var controller = (ExportFilterTableController) loader.getController();
        controller.setTables(UsersTable, costTypeTable, costFormTable, bankFlowTable);
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
    @SuppressWarnings("all")
    private void clickAddOfDayTypeMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("izin_günü_tipi_ekleme_form.fxml"));
        UtilFX.initStage("İzin Günü Tipi Ekleme Formu", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    @SuppressWarnings("all")
    private void clickWorkingDaysMenu() throws IOException
    {
        Parent root  = FXMLLoader.load(App.class.getResource("calisilan_günler_form.fxml"));
        UtilFX.initStage("Çalışılan Günler", new Stage(), new Scene(root),false, Constant.ICON);
    }
    @FXML
    @SuppressWarnings("all")
    private void clickMarkBankFlow() throws IOException
    {
        var item = bankFlowTable.getSelectionModel().getSelectedItem();

        if (item.isMark()) {
            item.setMark(false);
            markedBankFlowTreeSet.remove(item);
        } else {
            item.setMark(true);
            markedBankFlowTreeSet.add(item);
        }
        bankFlowTable.refresh();
    }
    @FXML
    @SuppressWarnings("all")
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
       catch (Exception ignore) {}
    }

    @FXML
    private void clickDeleteCostFlow()
    {
        var item = costFormTable.getSelectionModel().getSelectedItem();
        SessionFactoryManager.remove(item);
        item.getUser().setAmount(item.getUser().getAmount().subtract(item.getTotalInvoice()));
        SessionFactoryManager.update(item.getUser());
        costFormTable.refresh();
        costFormTable.getItems().remove(item);

        CommandController.getInstance().acceptRemove(COST_FORM, new CostFormDTO(item, REMOVE));
    }
    @FXML
    private void clickUpdateCostFlow()
    {
        try
        {
            if (costFormTable.getSelectionModel().getSelectedItem() == null)
                throw new Exception();

            var loader = new FXMLLoader(App.class.getResource("masraf_güncelleme_form.fxml"));
            UtilFX.initStage("Masraf Güncelleme Formu", new Stage(), new Scene(loader.load()),false, Constant.ICON);

            var form = costFormTable.getSelectionModel().getSelectedItem();

            UpdateCostFormController controller = loader.getController();
            controller.setMainScreenController(this);
            controller.setExistingCostForm(form);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void clickUpdateBankFlow() throws Exception
    {
        var item = bankFlowTable.getSelectionModel().getSelectedItem();

        if (item == null)
            throw new Exception();

        var loader = new FXMLLoader(App.class.getResource("banka_hareket_form.fxml"));

        UtilFX.initStage("Banka Hareketi Güncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);


        UpdateBankFlowController updateBankFlowController = loader.getController();
        updateBankFlowController.setController(this);
        updateBankFlowController.setBankFlow(SessionFactoryManager.get(BankFlow.class, item.getBank_flow_pk_id()));
    }
    @FXML
    private void clickDeleteBankFlow()
    {
        var item = bankFlowTable.getSelectionModel().getSelectedItem();
        SessionFactoryManager.remove(item);

        bankFlowTable.getItems().remove(item);
        bankFlowTable.refresh();

        CommandController.getInstance().acceptRemove(BANK_FLOW, new BankFlowDTO(item, REMOVE));
    }

    public void update(BankFlow existingBankFlow)
    {
        bankFlowTable.getSelectionModel().getSelectedItem().setCostType(existingBankFlow.getCostType());
        bankFlowTable.getSelectionModel().getSelectedItem().setCaseFlow(existingBankFlow.getCaseFlow());
        bankFlowTable.getSelectionModel().getSelectedItem().setDate(existingBankFlow.getDate());
        bankFlowTable.getSelectionModel().getSelectedItem().setCost(existingBankFlow.getCost());
        bankFlowTable.getSelectionModel().getSelectedItem().setBillingNumber(existingBankFlow.getBillingNumber());

        bankFlowTable.refresh();
    }

    public void update(CostForm existingCostForm)
    {
        costFormTable.getSelectionModel().getSelectedItem().setCostType(existingCostForm.getCostType());
        costFormTable.getSelectionModel().getSelectedItem().setDescription(existingCostForm.getDescription());
        costFormTable.getSelectionModel().getSelectedItem().setDate(existingCostForm.getDate());
        costFormTable.getSelectionModel().getSelectedItem().setKdv(existingCostForm.getKdv());
        costFormTable.getSelectionModel().getSelectedItem().setBillingNumber(existingCostForm.getBillingNumber());
        costFormTable.getSelectionModel().getSelectedItem().setCompany(existingCostForm.getCompany());
        costFormTable.getSelectionModel().getSelectedItem().setExpenditureOfficer(existingCostForm.getExpenditureOfficer());
        costFormTable.getSelectionModel().getSelectedItem().setInvoiceAmount(existingCostForm.getInvoiceAmount());
        costFormTable.getSelectionModel().getSelectedItem().setVoucher(existingCostForm.getIsVoucher());
        costFormTable.getSelectionModel().getSelectedItem().setTotalInvoice(existingCostForm.getTotalInvoice());
        costFormTable.refresh();
    }

    @FXML
    private void clickAddManagerReport() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("yönetici_özeti_girdi_ekleme_form.fxml"));

        UtilFX.initStage("Yönetici Özeti Girdi Ekleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickDeleteMarkedBankFlows()
    {
        var list = new ArrayList<BankFlow>(markedBankFlowTreeSet);
        list.forEach(f -> f.setMark(false));
        list.forEach(SessionFactoryManager::remove);
        CommandController.getInstance().acceptRemove(MULTIPLE_BANK_FLOW, new MultipleBankFlowDTO(list, REMOVE));
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Banka Hareketrleri Başarı ile Silindi...",
                ButtonType.OK);
        service.initCostFlowTable();
    }
    @FXML
    private void clickDeleteMultipleBankFlow() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("toplu_banka_hareketi_silme_formu.fxml"));

        UtilFX.initStage("Toplu Banka Hareketi Silme", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickDeleteCostType()
    {
        var selectedItem = costTypeTable.getSelectionModel().getSelectedItem();

        var sumOfCostType = SessionFactoryManager.getSumOfTypes(selectedItem.getCostType());
        var sumOfBankFlow = SessionFactoryManager.getSumOfTypesBankFlow(selectedItem.getCostType());

        if (sumOfCostType == null && sumOfBankFlow == null)
        {
            SessionFactoryManager.remove(selectedItem);
            costTypeTable.refresh();
            costTypeTable.getItems().remove(selectedItem);
            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Gider Türü başarı ile silindi...", ButtonType.OK);
        }

        else
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Gider Türü Silinemez! Gider Türünü İçeren Kayıtlar Mevcut", ButtonType.OK);
    }

    @FXML
    private void clickSelectMultipleCostForm() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("toplu_gider_türü_belirleme_formu.fxml"));

        UtilFX.initStage("Toplu Gider Türü Belirleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        var controller = (MultipleSelectCostTypeController)loader.getController();
        controller.setBankFlows(markedBankFlowTreeSet);
        controller.setTable(bankFlowTable);
    }

    @FXML
    private void clickSelectMultipleBankFlow() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("toplu_banka_hareketi_işaretleme_formu.fxml"));

        UtilFX.initStage("Toplu Banka Hareketleri İşaretleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);

        var controller = (MarkMultipleBankFlow) loader.getController();
        controller.setBankFlowTable(bankFlowTable);
        controller.setBankFlows(markedBankFlowTreeSet);
    }

    @FXML
    private void clickUndoCostForm()
    {
        CommandController.getInstance().acceptUndo(COST_FORM);
        costFormTable.refresh();
    }
    @FXML
    private void clickUndoBankFlow()
    {
        CommandController.getInstance().acceptUndo(BANK_FLOW);
        bankFlowTable.refresh();
    }

    @FXML
    private void clickUndoMultipleBankFlow()
    {
        CommandController.getInstance().acceptUndo(MULTIPLE_BANK_FLOW);
        bankFlowTable.refresh();
    }

    @FXML
    private void clickExportReport()
    {
        var localDate = LocalDate.now();
        var fileName = localDate.getYear() + "-" + Months_TR.values()[localDate.getMonthValue()] + " Yönetici Özeti";
        var file = UtilFX.fileChoose(fileName, "Excel", ".xlsx");

        var titles = new String[] {"Tarih", "Açıklama", "Tutar", "Döviz Kuru", "Tutar(₺)"};

        var export = new ExportManagerReport(fileName, 5, titles, file.getParent());

        export.export();
    }
    @FXML
    private void clickManagerSummaryTable() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("yönetici_özeti_tablo_form.fxml"));

        UtilFX.initStage("Yönetici Özeti Tablosu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickSumOfPersonWithMonth() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("kisi_ay_toplam_form.fxml"));

        UtilFX.initStage("Kişi - Ay Toplam Harcama", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickcostTypeBankFlow() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("gider_türü_banka_hareketleri.fxml"));

        UtilFX.initStage("Gider Türü Banka Hareketleri", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickUpdateAdvance() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("avans_güncelleme_form.fxml"));

        UtilFX.initStage("Avans Güncelleme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }

    @FXML
    private void clickDeleteAdvanveMoney() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("avans_silme_form.fxml"));

        UtilFX.initStage("Avans Silme Formu", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }
    @FXML
    private void clickUserYearAdvanceAmount() throws IOException
    {
        var loader = new FXMLLoader(App.class.getResource("avans_kullanıcı_yıl_formu.fxml"));

        UtilFX.initStage("Kişi - Yıl Avans", new Stage(), new Scene(loader.load()), false, Constant.ICON);
    }
}