package com.example.accountingX.Controller;

import com.example.accountingX.Entity.CostForm;
import com.example.accountingX.Entity.CostType;
import com.example.accountingX.Entity.User;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
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
    private CostForm costForm;
    private LocalDate date;
    private String description;
    private CostType costType;
    private String company;
    private boolean isVoucher;
    private Integer billingNumber;
    private BigDecimal totalInvoice;
    private double kdv;
    private BigDecimal invoiceAmount;

    public void setMainScreenController(MainScreenController mainScreenController)
    {
        this.mainScreenController = mainScreenController;
    }

    public void setCostForm(CostForm costForm) {
        this.costForm = costForm;
        setTexts();
        initChoiceBoxes();
    }

    private void initChoiceBoxes()
    {
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
        date = dateDatePicker.getValue();
        description = descriptionText.getText();
        costType = costTypeChoice.getSelectionModel().getSelectedItem();
        company = companyString.getText();
        isVoucher = isVoucherRadioBtn.isSelected();
        billingNumber =  Integer.parseInt(billingNoText.getText());
        totalInvoice = new BigDecimal(totalInvoiceText.getText());
        invoiceAmount = new BigDecimal(getKDV(kdv));

        updateCostForm();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Masraf Formu Başarıyla Güncellendi!", ButtonType.OK);
    }
    private void updateCostForm()
    {
        costForm.setDate(date);
        costForm.setDescription(description);
        costForm.setCostType(costType.getCostType());
        costForm.setCompany(company);
        costForm.setVoucher(isVoucher);
        costForm.setBillingNumber(billingNumber);
        costForm.setTotalInvoice(totalInvoice);
        costForm.setInvoiceAmount(invoiceAmount);
        costForm.setKdv(kdv);

        SessionFactoryManager.update(costForm);
    }
    private void setTexts()
    {
        dateDatePicker.setValue(costForm.getDate());
        officerChoice.getSelectionModel().select(officerChoice.getSelectionModel().getSelectedItem());
        billingNoText.setText(costForm.getBillingNumber().toString());
        descriptionText.setText(costForm.getDescription());
        costTypeChoice.getSelectionModel().select(costTypeChoice.getSelectionModel().getSelectedItem());
        invoiceAmountText.setText(costForm.getInvoiceAmount().toString());
        totalInvoiceText.setText(costForm.getTotalInvoice().toString());
        kdvChoice.getSelectionModel().select(Double.toString(costForm.getKdv()));
        companyString.setText(costForm.getCompany());
        isVoucherRadioBtn.setSelected(costForm.getIsVoucher());
        officerChoice.setDisable(true);
    }
}
