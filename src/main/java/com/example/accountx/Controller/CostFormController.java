package com.example.accountx.Controller;

import com.example.accountx.Entity.*;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.commandcontroller.CommandController;
import com.example.accountx.command.dto.CostFormDTO;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.accountx.command.types.CommandName.COST_FORM;
import static com.example.accountx.command.types.CommandType.APPLY;

public class CostFormController
{
    private MainScreenController mainScreenController;
    @FXML private DatePicker datePicker;
    @FXML private TextField billingNoTextField;
    @FXML private RadioButton isVoucherRadioButton;
    @FXML private TextField invoiceAmountTextField;
    @FXML private TextField totalInvoiceTextField;
    @FXML private TextField companyTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private ChoiceBox<String> officerChoiceBox;
    @FXML private ChoiceBox<CostType> costTypeChoiceBox;
    @FXML private ChoiceBox<String> kdvChoiceBox;

    @FXML
    private void initialize()
    {
        // set today date in date picker
        datePicker.setValue(LocalDate.now());
        // fill the officerChoiceBox
        SessionFactoryManager.getUserList().forEach(user -> officerChoiceBox.getItems().add(user.getName() + "_" + user.getSurname()));
        // fill the kdvChoiceBox
        SessionFactoryManager.getKDVs().forEach(kdv -> kdvChoiceBox.getItems().add(Double.toString(kdv.getKdv())));
        // fill the costTypeChoiceBox
        SessionFactoryManager.getCostTypes().forEach(costTypeChoiceBox.getItems()::add);
    }

    // Dependency Injection
    public void setMainScreenController(MainScreenController mainScreenController)
    {
        this.mainScreenController = mainScreenController;
    }


    // When you pressed the Enter in Company TextField, program deal like click on the button
    @FXML
    private void pressEnterOnLocation(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickSaveButton();
    }

    // Calculate the invoiceAmount used with KDV and total Invoice Amount
    private String getKDV()
    {
        return new BigDecimal(totalInvoiceTextField.getText())
                .subtract(BigDecimal.valueOf(Double.parseDouble(kdvChoiceBox.getSelectionModel().getSelectedItem()) / 100.)
                        .multiply(new BigDecimal(totalInvoiceTextField.getText()))).toString();
    }

    // When you pressed the Enter key on TotalInvoiceAmount, show the invoice amount automatically.
    @FXML
    private void onTypedTotalInvoice(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            invoiceAmountTextField.setText(getKDV());
    }

    private User getUser(String name, String surname)
    {
       return SessionFactoryManager.getUserList().stream()
               .filter(u -> u.getName().equals(name) && u.getSurname().equals(surname))
               .findFirst().orElse(null);
    }
    @FXML
    private void clickSaveButton()
    {
        try
        {
            var date = datePicker.getValue();
            var description = descriptionTextArea.getText();
            var costType = costTypeChoiceBox.getSelectionModel().getSelectedItem();
            var company = companyTextField.getText();
            var isVoucher = isVoucherRadioButton.isSelected();
            var billingNumber =  billingNoTextField.getText();
            var totalInvoice = new BigDecimal(totalInvoiceTextField.getText());

            if (totalInvoice.compareTo(BigDecimal.ZERO) > 0)
                totalInvoice = totalInvoice.negate();

            var officer = officerChoiceBox.getSelectionModel().getSelectedItem().split("_");

            var user = getUser(officer[0], officer[1]);

            if ((!StringControl.isValid(totalInvoiceTextField.getText(),company,description,date.toString())))
                throw new Exception();

            if (user == null)
                throw new Exception();

            var form = new CostForm.Builder()
                    .setDate(date)
                    .setTime(LocalTime.now())
                    .setCostType(costType.getCostType())
                    .setBillingNumber(billingNumber)
                    .setTDescription(description)
                    .setInvoiceAmount(new BigDecimal(invoiceAmountTextField.getText()))
                    .setKDV(Double.parseDouble(kdvChoiceBox.getSelectionModel().getSelectedItem()))
                    .setTotalInvoice(totalInvoice)
                    .setExpenditureOfficer(officer[0] + " " + officer[1])
                    .setCompany(company)
                    .setIsVoucher(isVoucher)
                    .setUser(user)
                    .build();

            SessionFactoryManager.add(form);

            CommandController.getInstance().acceptApply(COST_FORM, new CostFormDTO(form, APPLY)); // FOR UNDO

            var costForm = (CostForm) SessionFactoryManager.getCostForm();

            mainScreenController.getService().addCostFlowTable(costForm);

            user.setAmount(user.getAmount().add(totalInvoice));

            SessionFactoryManager.update(user);
            clear();
            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Masraf Formu başarıyla eklendi!", ButtonType.OK);
        }
        catch (Exception ex)
        {
            String str = "Tarih, Fatura açıklaması, Gider türü, Toplam fatura, Harcama yapılan " +
                    "yer bilgilerini girmek zorunludur. Ayrıca girişi zorunlu olanları sadece boşluk ile giremezsiniz";
            UtilFX.alertScreen(Alert.AlertType.ERROR, str, ButtonType.OK);
        }
    }

    /*private void addAdvanceAmount(User user)
    {
        var date = datePicker.getValue();
        int year, month;

        if (date.getMonth() == Month.JANUARY)
        {
            year = date.getYear() - 1;
            month = 12;
        }

        else
        {
            year = date.getYear();
            month = date.getMonthValue() - 1;
        }

        var prevAdvanceAmounts = SessionFactoryManager.getAdvanceAmounts(user.getUser_pk_id(), year, month);

        if (prevAdvanceAmounts == null || prevAdvanceAmounts.isEmpty() || prevAdvanceAmounts.get(0).isTransfer())
            return;

        var totalAmount = prevAdvanceAmounts.stream().map(AdvanceAmount::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var cost = SessionFactoryManager.getSumOfCostsofPersonWithMonth(user.getUser_pk_id(), year, month);

        cost = cost == null ? BigDecimal.ZERO : cost;

        var r = cost.add(totalAmount);
        var advanceAmount = new AdvanceAmount(user, r, LocalDate.of(date.getYear(), date.getMonth(), 1));

        prevAdvanceAmounts.forEach(i -> i.setTransfer(true));
        prevAdvanceAmounts.forEach(SessionFactoryManager::update);

        SessionFactoryManager.add(advanceAmount);
    }*/

    private void clear()
    {
        datePicker.setValue(LocalDate.now());
        billingNoTextField.clear();
        descriptionTextArea.clear();
        invoiceAmountTextField.clear();
        totalInvoiceTextField.clear();
        companyTextField.clear();
        isVoucherRadioButton.setSelected(false);
    }
}
