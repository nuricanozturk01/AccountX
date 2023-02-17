package com.example.accountx.Controller;

import com.example.accountx.Entity.CostForm;
import com.example.accountx.Entity.CostType;
import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateCostFormController
{
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField billingNoText;
    @FXML
    private TextArea descriptionText;
    @FXML
    private TextField invoiceAmountText;
    @FXML
    private ChoiceBox<CostType> costTypeChoice;
    @FXML
    private ChoiceBox<String> kdvChoice;
    @FXML
    private TextField totalInvoiceText;
    @FXML
    private ChoiceBox<User> officerChoice;
    @FXML
    private RadioButton isVoucherRadioBtn;
    @FXML
    private TextField companyString;
    private MainScreenController mainScreenController;
    private CostForm existingCostForm;
    private LocalDate date;
    private String description;
    private CostType costType;
    private String company;
    private boolean isVoucher;
    private String billingNumber;
    private BigDecimal totalInvoice;
    private double kdv;
    private BigDecimal invoiceAmount;

    public void setMainScreenController(MainScreenController mainScreenController)
    {
        this.mainScreenController = mainScreenController;
    }

    public void setExistingCostForm(CostForm existingCostForm) {
        this.existingCostForm = existingCostForm;
        setTexts();
        initChoiceBoxes();
    }

    private void initChoiceBoxes()
    {
        officerChoice.setDisable(false);
        // fill the officerChoiceBox
        SessionFactoryManager.getUserList().forEach(officerChoice.getItems()::add);
        // fill the kdvChoiceBox
        SessionFactoryManager.getKDVs().forEach(
                kdv -> kdvChoice.getItems().add(Double.toString(kdv.getKdv())));
        // fill the costTypeChoiceBox
        SessionFactoryManager.getCostTypes().forEach(costTypeChoice.getItems()::add);
    }
    private String getKDV(double kdv)
    {
        return new BigDecimal(totalInvoiceText.getText()).subtract(BigDecimal.valueOf(kdv / 100.)
                        .multiply(new BigDecimal(totalInvoiceText.getText()))).toString();
    }

    @FXML
    private void pressedKeyTotalInvoice(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            kdv = Double.parseDouble(kdvChoice.getSelectionModel().getSelectedItem());
            invoiceAmountText.setText(getKDV(kdv));
        }
    }
    @FXML
    private void clickSaveButton()
    {
        var total = new BigDecimal(totalInvoiceText.getText());
        total = total.compareTo(BigDecimal.ZERO) > 0 ? total.negate() : total;

        date = dateDatePicker.getValue();
        description = descriptionText.getText();
        costType = costTypeChoice.getSelectionModel().getSelectedItem();
        company = companyString.getText();
        isVoucher = isVoucherRadioBtn.isSelected();
        billingNumber = billingNoText.getText();
        totalInvoice = total;
        invoiceAmount = new BigDecimal(getKDV(kdv));

        updateCostForm();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Masraf Formu Başarıyla Güncellendi!", ButtonType.OK);
    }
    private void updateCostForm()
    {
        if (costType.getCostType() == null)
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen Gider Türünü Girin...", ButtonType.OK);

        var user = officerChoice.getSelectionModel().getSelectedItem();
        existingCostForm.setUser(user);
        existingCostForm.setExpenditureOfficer(user.getName() + " " + user.getSurname());
        existingCostForm.setDate(date);
        existingCostForm.setDescription(description);
        existingCostForm.setCostType(costType.getCostType());
        existingCostForm.setCompany(company);
        existingCostForm.setVoucher(isVoucher);
        existingCostForm.setBillingNumber(billingNumber);
        existingCostForm.setTotalInvoice(totalInvoice);
        existingCostForm.setInvoiceAmount(invoiceAmount);
        existingCostForm.setKdv(kdv);

        SessionFactoryManager.update(existingCostForm);

        mainScreenController.update(existingCostForm);
    }
    private void setTexts()
    {
        dateDatePicker.setValue(existingCostForm.getDate());
        officerChoice.getSelectionModel().select(existingCostForm.getUser());
        billingNoText.setText(existingCostForm.getBillingNumber());
        descriptionText.setText(existingCostForm.getDescription());
        costTypeChoice.getSelectionModel().select(SessionFactoryManager.getCostType(existingCostForm.getCostType()));
        invoiceAmountText.setText(existingCostForm.getInvoiceAmount().toString());
        totalInvoiceText.setText(existingCostForm.getTotalInvoice().toString());
        kdvChoice.getSelectionModel().select(Double.toString(existingCostForm.getKdv()));
        companyString.setText(existingCostForm.getCompany());
        isVoucherRadioBtn.setSelected(existingCostForm.getIsVoucher());
        officerChoice.setDisable(true);
    }
}
