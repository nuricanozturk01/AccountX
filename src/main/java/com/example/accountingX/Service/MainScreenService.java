package com.example.accountingX.Service;
import com.example.accountingX.Entity.*;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainScreenService
{
    private TableColumn<BankFlow, Integer> idColumnBankFlow;
    private TabPane tabPane;
    private Button filterButtonDate;
    private Button filterButtonCostType;
    private Button searchNameButton;
    private TextField nameTextField;
    private TableColumn<CostType, String> costTypeColumnCostTypeTable;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;
    private ChoiceBox<String> costTypeChoiceBox;
    private TableColumn<CostForm, LocalDate> dateColumn;
    private TableColumn<CostForm,String> caseFlowColumn;
    private TableColumn<CostForm,String> costTypeColumn;
    private TableColumn<CostForm, BigDecimal> costColumn;
    private TableColumn<BankFlow, LocalDate> dateColumnBank;
    private TableColumn<BankFlow,String> caseFlowColumnBank;
    private TableColumn<BankFlow,String> costTypeColumnBank;
    private TableColumn<BankFlow,BigDecimal> costColumnBank;
    private TableColumn<User,String> nameColumn;
    private TableColumn<User,String> surnameColumn;
    private TableView<CostForm> costFlowTable;
    private TableView<BankFlow> bankFlowTable;
    private TableView<User> UsersTable;
    private TableView<CostType> costTypeTable;
    private TableColumn<CostForm,Integer> noColumn;
    private TableColumn<CostForm, Boolean> voucherTableColumn;

    public static class ServiceBuilder
    {
        private final MainScreenService service;

        public ServiceBuilder()
        {
            service = new MainScreenService();
        }

        public ServiceBuilder setIdColumnBankFlow(TableColumn<BankFlow, Integer> idColumnBankFlow)
        {
            service.idColumnBankFlow = idColumnBankFlow;
            return this;
        }


        public ServiceBuilder setTabPane(TabPane tabPane) {
            service.tabPane = tabPane;
            return this;
        }

        public ServiceBuilder setFilterButtonDate(Button filterButtonDate) {
            service.filterButtonDate = filterButtonDate;
            return this;
        }

        public ServiceBuilder setFilterButtonCostType(Button filterButtonCostType) {
            service.filterButtonCostType = filterButtonCostType;
            return this;
        }

        public ServiceBuilder setSearchNameButton(Button searchNameButton) {
            service.searchNameButton = searchNameButton;
            return this;
        }

        public ServiceBuilder setNameTextField(TextField nameTextField) {
            service.nameTextField = nameTextField;
            return this;
        }

        public ServiceBuilder setCostTypeColumnCostTypeTable(TableColumn<CostType, String> costTypeColumnCostTypeTable) {
            service.costTypeColumnCostTypeTable = costTypeColumnCostTypeTable;
            return this;
        }

        public ServiceBuilder setStartDatePicker(DatePicker startDatePicker) {
            service.startDatePicker = startDatePicker;
            return this;
        }

        public ServiceBuilder setFinishDatePicker(DatePicker finishDatePicker) {
            service.finishDatePicker = finishDatePicker;
            return this;
        }

        public ServiceBuilder setCostTypeChoiceBox(ChoiceBox<String> costTypeChoiceBox) {
            service.costTypeChoiceBox = costTypeChoiceBox;
            return this;
        }

        public ServiceBuilder setDateColumn(TableColumn<CostForm, LocalDate> dateColumn) {
            service.dateColumn = dateColumn;
            return this;
        }

        public ServiceBuilder setCaseFlowColumn(TableColumn<CostForm, String> caseFlowColumn) {
            service.caseFlowColumn = caseFlowColumn;
            return this;
        }

        public ServiceBuilder setCostTypeColumn(TableColumn<CostForm, String> costTypeColumn) {
            service.costTypeColumn = costTypeColumn;
            return this;
        }

        public ServiceBuilder setCostColumn(TableColumn<CostForm, BigDecimal> costColumn) {
            service.costColumn = costColumn;
            return this;
        }

        public ServiceBuilder setDateColumnBank(TableColumn<BankFlow, LocalDate> dateColumnBank) {
            service.dateColumnBank = dateColumnBank;
            return this;
        }

        public ServiceBuilder setCaseFlowColumnBank(TableColumn<BankFlow, String> caseFlowColumnBank) {
            service.caseFlowColumnBank = caseFlowColumnBank;
            return this;
        }

        public ServiceBuilder setCostTypeColumnBank(TableColumn<BankFlow, String> costTypeColumnBank) {
            service.costTypeColumnBank = costTypeColumnBank;
            return this;
        }

        public ServiceBuilder setCostColumnBank(TableColumn<BankFlow, BigDecimal> costColumnBank) {
            service.costColumnBank = costColumnBank;
            return this;
        }

        public ServiceBuilder setNameColumn(TableColumn<User, String> nameColumn) {
            service.nameColumn = nameColumn;
            return this;
        }

        public ServiceBuilder setSurnameColumn(TableColumn<User, String> surnameColumn) {
            service.surnameColumn = surnameColumn;
            return this;
        }

        public ServiceBuilder setCostFlowTable(TableView<CostForm> costFlowTable) {
            service.costFlowTable = costFlowTable;
            return this;
        }

        public ServiceBuilder setBankFlowTable(TableView<BankFlow> bankFlowTable) {
            service.bankFlowTable = bankFlowTable;
            return this;
        }

        public ServiceBuilder setUsersTable(TableView<User> usersTable) {
            service.UsersTable = usersTable;
            return this;
        }

        public ServiceBuilder setVoucherTableColumn(TableColumn<CostForm, Boolean> voucherTableColumn) {
            service.voucherTableColumn = voucherTableColumn;
            return this;
        }

        public ServiceBuilder setCostTypeTable(TableView<CostType> costTypeTable) {
            service.costTypeTable = costTypeTable;
            return this;
        }

        public ServiceBuilder setNoColumn(TableColumn<CostForm, Integer> noColumn) {
            service.noColumn = noColumn;
            return this;
        }
        public MainScreenService build()
        {
            return service;
        }
    }
    public void init()
    {
        // Set default day is today in DatePicker
        startDatePicker.setValue(LocalDate.now());
        finishDatePicker.setValue(LocalDate.now());
        // CostFlow Table
        noColumn.setCellValueFactory(new PropertyValueFactory<>("Cost_form_pk_id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateColumn.setCellFactory((column) -> getFormattedDate());
        caseFlowColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        costTypeColumn.setCellValueFactory(new PropertyValueFactory<>("costType"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("TotalInvoice"));
        voucherTableColumn.setCellValueFactory(new PropertyValueFactory<>("IsVoucher"));
        //BankFlow Table
        idColumnBankFlow.setCellValueFactory(new PropertyValueFactory<>("Bank_flow_pk_id"));
        dateColumnBank.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateColumnBank.setCellFactory((column) -> getFormattedDateBankFlow());
        caseFlowColumnBank.setCellValueFactory(new PropertyValueFactory<>("CaseFlow"));
        costTypeColumnBank.setCellValueFactory(new PropertyValueFactory<>("CostType"));
        costColumnBank.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        //Users Table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        // CostTypeTable Table
        costTypeColumnCostTypeTable.setCellValueFactory(new PropertyValueFactory<>("CostType"));
        // TabPane Listener method reference
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabPaneListener);

        fillCostTypeChoiceBox();
        fillTable();
    }

    public void tabPaneListener(Observable obv, Tab t, Tab t1 )
    {
        if (t1.getText().equals("Masraf Formları"))
            setDisable(false,false,false);

        if (t1.getText().equals("Kişiler"))
            setDisable(true,false,true);

        if (t1.getText().equals("Gider Türleri"))
            setDisable(true,false,true);
    }

    public void setDisable(boolean date, boolean name, boolean costType)
    {
        // choice box
        costTypeChoiceBox.setDisable(costType);
        filterButtonCostType.setDisable(costType);
        // date picker
        finishDatePicker.setDisable(date);
        startDatePicker.setDisable(date);
        filterButtonDate.setDisable(date);
        // name text field
        searchNameButton.setDisable(name);
        nameTextField.setDisable(name);
    }

    public TableCell<CostForm, LocalDate> getFormattedDate()
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

    public TableCell<BankFlow, LocalDate> getFormattedDateBankFlow()
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

    /**
     * Getter Tables
     */
    public TableView<User> getUsersTable() {
        return UsersTable;
    }

    /**
     * Fill Table Methods
     */
    public void fillCostTypeChoiceBox()
    {
        costTypeChoiceBox.getItems().clear();
        var costTypeList = SessionFactoryManager.getCostTypes();

        if (costTypeList != null && !costTypeList.isEmpty())
            costTypeList.forEach(type -> costTypeChoiceBox.getItems().add(type.getCostType()));
    }

    public void initCostFlowTable()
    {
        var costFlowList = SessionFactoryManager.getCostFormList(35);

        if (costFlowList != null && !costFlowList.isEmpty())
            costFlowTable.setItems(FXCollections.observableArrayList(costFlowList));
    }

    public void initUsersTable()
    {

        var userList = SessionFactoryManager.getUserList();

        if (userList != null && !userList.isEmpty())
            UsersTable.setItems(FXCollections.observableArrayList(userList));
    }

    public void initCostTypeTable()
    {

        var costTypeList = SessionFactoryManager.getCostTypes();

        if (costTypeList != null && !costTypeList.isEmpty())
            costTypeTable.setItems(FXCollections.observableArrayList(costTypeList));
    }

    public void initBankFlowTable()
    {
        var bankFlowList = SessionFactoryManager.getBankFlows(35);

        if (bankFlowList != null && !bankFlowList.isEmpty())
            bankFlowTable.setItems(FXCollections.observableArrayList(bankFlowList));
    }

    public void fillTable()
    {
        initCostFlowTable();
        initCostTypeTable();
        initUsersTable();
        initBankFlowTable();
    }
    public void addCostFlowTable(CostForm flow)
    {
        costFlowTable.getItems().add(flow);
    }
    public void addUserTable(User user)
    {
        UsersTable.getItems().add(user);
    }
    public void addCostTypeTable(CostType costType)
    {
        costTypeTable.getItems().add(costType);
        costTypeChoiceBox.getItems().add(costType.getCostType());
    }
    public void updateUserTable()
    {
        initUsersTable();
    }
    public void updateCotFlowTable()
    {
        initCostFlowTable();
    }

}
