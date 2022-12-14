package com.example.accountingX.Controller;

import com.example.accountingX.Entity.*;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
            var billingNumber =  Integer.parseInt(billingNoTextField.getText());
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

            var costForm = (CostForm) SessionFactoryManager.getCostForm();

            mainScreenController.getService().addCostFlowTable(costForm);

            clear();
            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Masraf Formu ba??ar??yla eklendi!", ButtonType.OK);
        }
        catch (Exception ex)
        {
            String str = "Tarih, Fatura a????klamas??, Gider t??r??, Toplam fatura, Harcama yap??lan " +
                    "yer bilgilerini girmek zorunludur. Ayr??ca giri??i zorunlu olanlar?? sadece bo??luk ile giremezsiniz";
            UtilFX.alertScreen(Alert.AlertType.ERROR, str, ButtonType.OK);
        }
    }

    private void clear()
    {
        datePicker.getEditor().clear();
        datePicker.setValue(null);
        billingNoTextField.clear();
        descriptionTextArea.clear();
        invoiceAmountTextField.clear();
        totalInvoiceTextField.clear();
        companyTextField.clear();
        isVoucherRadioButton.setSelected(false);
    }

}
