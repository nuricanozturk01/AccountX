package com.example.accountx.Controller;

import com.example.accountx.Entity.FundFlow;
import com.example.accountx.Entity.OtherFlow;
import com.example.accountx.Entity.TreasureFlow;
import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateManagerReportController <T>
{
    @FXML
    private  RadioButton USD;
    @FXML
    private  RadioButton EUR;
    @FXML
    private  RadioButton TL;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField debtAmount;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField exchangeRate;
    @FXML
    private TextField resultAmount;
    @FXML
    private RadioButton fundFlow;
    @FXML
    private RadioButton otherFlow;
    @FXML
    private RadioButton treasureFlow;
    private ManagerTableController<T> controller;
    private T selectedItem;


    @FXML
    private void initialize() {
        var exchangeGroup = new ToggleGroup();
        fundFlow.setDisable(true);
        otherFlow.setDisable(true);
        treasureFlow.setDisable(true);

        EUR.setToggleGroup(exchangeGroup);
        TL.setToggleGroup(exchangeGroup);
        USD.setToggleGroup(exchangeGroup);
    }

    public void setController(ManagerTableController<T> controller) {
        this.controller = controller;
    }
    public void setSelectedItem(T item)
    {
        selectedItem = item;

        if (selectedItem instanceof FundFlow)
            initFundFlow();
        if (selectedItem instanceof TreasureFlow)
            initTreasureFlow();
        if (selectedItem instanceof OtherFlow)
            initOtherFlow();
    }
    private void initFundFlow()
    {
        var item = (FundFlow) selectedItem;
        fundFlow.setSelected(true);
        fillExistingFields(item.getCurrency(), item.getDate(), item.getExchangeRate(), item.getDebt(), item.getResultAmount(), item.getDescription());
    }

    private void initOtherFlow() {
        var item = (OtherFlow) selectedItem;
        otherFlow.setSelected(true);
        fillExistingFields(item.getCurrency(), item.getDate(), item.getExchangeRate(), item.getDebt(), item.getResultAmount(), item.getDescription());
    }

    private void initTreasureFlow() {
        var item = (TreasureFlow) selectedItem;
        treasureFlow.setSelected(true);
        fillExistingFields(item.getCurrency(), item.getDate(), item.getExchangeRate(), item.getDebt(), item.getResultAmount(), item.getDescription());
    }

    private void fillExistingFields(String currency, LocalDate date, double exchange, BigDecimal debt, BigDecimal result, String description)
    {
        if (currency.equals("EUR"))
            EUR.setSelected(true);
        if (currency.equals("USD"))
            USD.setSelected(true);
        if (currency.equals("TL"))
            TL.setSelected(true);

        datePicker.setValue(date);
        exchangeRate.setText(String.valueOf(exchange));
        debtAmount.setText(String.valueOf(debt));
        resultAmount.setText(String.valueOf(result));
        descriptionTextArea.setText(description);
    }

    @FXML
    private void calculateResult() {
        resultAmount.setText("" + new BigDecimal(debtAmount.getText()).multiply(new BigDecimal(exchangeRate.getText())));
    }


    private void updateFundFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currency)
    {
        ((FundFlow)selectedItem).setCurrency(currency);
        ((FundFlow)selectedItem).setDebt(debt);
        ((FundFlow)selectedItem).setDate(date);
        ((FundFlow)selectedItem).setExchangeRate(exchange);
        ((FundFlow)selectedItem).setResultAmount(result);
        ((FundFlow)selectedItem).setDescription(description);
        SessionFactoryManager.update(selectedItem);
    }

    private void updateTreasureFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currency)
    {
        ((TreasureFlow)selectedItem).setCurrency(currency);
        ((TreasureFlow)selectedItem).setDebt(debt);
        ((TreasureFlow)selectedItem).setDate(date);
        ((TreasureFlow)selectedItem).setExchangeRate(exchange);
        ((TreasureFlow)selectedItem).setResultAmount(result);
        ((TreasureFlow)selectedItem).setDescription(description);
        SessionFactoryManager.update(selectedItem);
    }

    private void updateOtherFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currency)
    {
        ((OtherFlow)selectedItem).setCurrency(currency);
        ((OtherFlow)selectedItem).setDebt(debt);
        ((OtherFlow)selectedItem).setDate(date);
        ((OtherFlow)selectedItem).setExchangeRate(exchange);
        ((OtherFlow)selectedItem).setResultAmount(result);
        ((OtherFlow)selectedItem).setDescription(description);
        SessionFactoryManager.update(selectedItem);
    }
    @FXML
    private void clickAddButton()
    {
        try {
            var date = datePicker.getValue();
            var debt = new BigDecimal(debtAmount.getText());
            var description = descriptionTextArea.getText();
            var exchange = Double.parseDouble(exchangeRate.getText());
            var result = BigDecimal.valueOf(exchange).multiply(debt);
            var currency = getCurrency();

            if (!StringControl.isValid(description))
                throw new EmptyFieldException("Açıklama boş geçilemez....");

            if (fundFlow.isSelected())
               updateFundFlow(date, debt, description, exchange, result, currency);

            if (treasureFlow.isSelected())
                updateTreasureFlow(date, debt, description, exchange, result, currency);

            if (otherFlow.isSelected())
                updateOtherFlow(date, debt, description, exchange, result, currency);

            controller.updateTable();

            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Yönetici Özeti Tablosu Başarı ile Güncellendi...", ButtonType.OK);
        }
        catch (NumberFormatException e) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Sayı girilecek olan yerlere sayı giriniz...", ButtonType.OK);
        }
        catch (EmptyFieldException ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        }
    }
    private String getCurrency()
    {
        if (EUR.isSelected())
            return "EUR";
        if (USD.isSelected())
            return "USD";
        if (TL.isSelected())
            return "TL";
        return null;
    }
}
