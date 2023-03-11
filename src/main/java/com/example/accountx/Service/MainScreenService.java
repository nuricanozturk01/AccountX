package com.example.accountx.Service;

import com.example.accountx.Entity.*;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.accountx.command.types.CommandName.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class MainScreenService
{
    private TableColumn<BankFlow, Integer> idColumnBankFlow;
    private TabPane tabPane;
    private TextField nameTextField;
    private TableColumn<CostType, String> costTypeColumnCostTypeTable;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;
    private ChoiceBox<CostType> costTypeChoiceBox;
    private TableColumn<CostForm, LocalDate> dateColumn;
    private TableColumn<CostForm,String> caseFlowColumn;
    private TableColumn<CostForm,String> costTypeColumn;
    private TableColumn<CostForm, BigDecimal> costColumn;
    private TableColumn<BankFlow, LocalDate> dateColumnBank;
    private TableColumn<BankFlow,String> caseFlowColumnBank;
    private TableColumn<BankFlow,String> costTypeColumnBank;
    private TableColumn<BankFlow,BigDecimal> costColumnBank;
    private TableColumn<BankFlow, Boolean> markTableColumn;

    private TableColumn<User,String> nameColumn;
    private TableColumn<User,String> surnameColumn;
    private TableView<CostForm> costFlowTable;
    private TableView<BankFlow> bankFlowTable;
    private TableView<User> UsersTable;
    private TableView<CostType> costTypeTable;
    private TableColumn<CostForm,Integer> noColumn;
    private TableColumn<CostForm, Boolean> voucherTableColumn;
    private ChoiceBox<User> userChoiceBox;
    private RadioButton selectAllRadioButton;
    private TableColumn<User, BigDecimal> advanceAmountTableColumn;



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


        public ServiceBuilder setUserChoiceBox(ChoiceBox<User> userChoiceBox) {
            service.userChoiceBox = userChoiceBox;
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

        public ServiceBuilder setCostTypeChoiceBox(ChoiceBox<CostType> costTypeChoiceBox) {
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

        public ServiceBuilder setBankFlowMarkColumn(TableColumn<BankFlow, Boolean> markTableColumn)
        {
            service.markTableColumn = markTableColumn;
            return this;
        }
        public ServiceBuilder setRadioButton(RadioButton selectAllRadioButton)
        {
            service.selectAllRadioButton = selectAllRadioButton;
            return this;
        }

        public ServiceBuilder setUserAdvanceAmountColumn(TableColumn<User, BigDecimal> advanceAmountTableColumn)
        {
            service.advanceAmountTableColumn = advanceAmountTableColumn;
            return this;
        }

        public MainScreenService build()
        {
            return service;
        }
    }
    public void init()
    {
        // CostFlow Table
        noColumn.setCellValueFactory(new PropertyValueFactory<>("Cost_form_pk_id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateColumn.setCellFactory((column) -> getFormattedDate());
        caseFlowColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        costTypeColumn.setCellValueFactory(new PropertyValueFactory<>("costType"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("TotalInvoice"));
        costColumn.setCellFactory((column) -> getFormattedTotalInvoice());
        voucherTableColumn.setCellValueFactory(new PropertyValueFactory<>("IsVoucher"));
        voucherTableColumn.setCellFactory((column) -> getVoucherStatus());
        //BankFlow Table
        idColumnBankFlow.setCellValueFactory(new PropertyValueFactory<>("Bank_flow_pk_id"));
        dateColumnBank.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateColumnBank.setCellFactory((column) -> getFormattedDateBankFlow());
        caseFlowColumnBank.setCellValueFactory(new PropertyValueFactory<>("CaseFlow"));
        costTypeColumnBank.setCellValueFactory(new PropertyValueFactory<>("CostType"));
        costColumnBank.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        costColumnBank.setCellFactory((column) -> getFormattedCost());
        markTableColumn.setCellValueFactory(new PropertyValueFactory<>("Mark"));
        markTableColumn.setCellFactory((column) -> getFormattedMarkColumnBankFlow());
        //Users Table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        advanceAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        advanceAmountTableColumn.setCellFactory((column) -> getFormattedAdvanceAmountColumnUser());

        // CostTypeTable Table
        costTypeColumnCostTypeTable.setCellValueFactory(new PropertyValueFactory<>("CostType"));
        // TabPane Listener method reference
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabPaneListener);

        updateUserChoiceBox();

        fillCostTypeChoiceBox();
        fillTable();

        dateColumnBank.setSortType(TableColumn.SortType.DESCENDING);
        bankFlowTable.getSortOrder().add(dateColumnBank);


    }

    public void updateUserChoiceBox()
    {
        userChoiceBox.getItems().clear();
        SessionFactoryManager.getUserList().forEach(userChoiceBox.getItems()::add);
    }
    private TableCell<CostForm, Boolean> getVoucherStatus()
    {
        return new TableCell<>(){
            @Override
            protected void updateItem(Boolean item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Var" : "Yok");
            }
        };
    }

    private TableCell<User, BigDecimal> getFormattedAdvanceAmountColumnUser()
    {
        return new TableCell<>()
        {
            @Override
            protected void updateItem(BigDecimal item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? null : getFormattedNumber(item));
            }
        };
    }

    private TableCell<BankFlow, BigDecimal> getFormattedCost()
    {
        return new TableCell<>()
        {
            @Override
            protected void updateItem(BigDecimal item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? null : getFormattedNumber(item));
            }
        };
    }

    private TableCell<CostForm, BigDecimal> getFormattedTotalInvoice()
    {
        return new TableCell<>()
        {
            @Override
            protected void updateItem(BigDecimal item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? null : getFormattedNumber(item));
            }
        };
    }

    public void tabPaneListener(Observable obv, Tab t, Tab t1 )
    {
        if (t1.getText().equals("Masraf Formları"))
            setDisable(false,false,false, false, true);

        if (t1.getText().equals("Banka Hareketleri"))
            setDisable(false,false,false, true, false);

        if (t1.getText().equals("Kişiler"))
            setDisable(true,false,true, true, true);

        if (t1.getText().equals("Gider Türleri"))
            setDisable(true,false,true, true, true);
    }

    public void setDisable(boolean date, boolean name, boolean costType, boolean user, boolean bankFlowRadioButton)
    {
        // choice box
        costTypeChoiceBox.setDisable(costType);

        // date picker
        finishDatePicker.setDisable(date);
        startDatePicker.setDisable(date);
        // name text field
        nameTextField.setDisable(name);
        // User choice box
        userChoiceBox.setDisable(user);

        selectAllRadioButton.setDisable(bankFlowRadioButton);
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

    public TableCell<BankFlow, Boolean> getFormattedMarkColumnBankFlow()
    {
        return new TableCell<>()
        {
            @Override
            protected void updateItem(Boolean item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty ? "" : item ? "X" : "");
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
            costTypeList.forEach(costTypeChoiceBox.getItems()::add);
    }

    public void initCostFlowTable()
    {
        var costFlowList = SessionFactoryManager.getCostFormList();

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
        bankFlowTable.getItems().clear();

        var bankFlows = SessionFactoryManager.getBankFlows();

        if (bankFlows != null && !bankFlows.isEmpty())
            bankFlowTable.setItems(FXCollections.observableArrayList(bankFlows));

        bankFlowTable.refresh();

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
        costTypeChoiceBox.getItems().add(costType);
    }
    public void updateUserTable()
    {
        initUsersTable();
    }
    public void updateCotFlowTable()
    {
        initCostFlowTable();
    }
    public void removeItemFromCostFormTable(CostForm cf)
    {
        costFlowTable.getItems().remove(cf);
    }
}
