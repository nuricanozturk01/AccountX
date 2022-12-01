package com.example.accountingX.Controller;

import com.example.accountingX.Entity.FundFlow;
import com.example.accountingX.Entity.OtherFlow;
import com.example.accountingX.Entity.TreasureFlow;
import com.example.accountingX.Exceptions.EmptyExchangeTypeException;
import com.example.accountingX.Exceptions.EmptyFieldException;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@SuppressWarnings("all")
public class ManagerController
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

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());
        var mainGroup = new ToggleGroup();
        var exchangeGroup = new ToggleGroup();
        fundFlow.setToggleGroup(mainGroup);
        otherFlow.setToggleGroup(mainGroup);
        treasureFlow.setToggleGroup(mainGroup);
        USD.setToggleGroup(exchangeGroup);
        EUR.setToggleGroup(exchangeGroup);
        TL.setToggleGroup(exchangeGroup);
    }
    private boolean isValidRadioButton()
    {
        var buttons = new RadioButton[] {treasureFlow, fundFlow, otherFlow};

        return Arrays.stream(buttons).filter(RadioButton::isSelected).count() == 1;
    }

    @FXML
    private void calculateResult() {
        resultAmount.setText("" + new BigDecimal(debtAmount.getText()).multiply(new BigDecimal(exchangeRate.getText())));

    }

    @FXML
    private void clickAddButton()
    {
        try
        {
            if (!isValidRadioButton())
                throw new EmptyFieldException("Bir tane butonu işaretlemelisiniz...");

            var date = datePicker.getValue();
            var debt = new BigDecimal(debtAmount.getText());
            var description = descriptionTextArea.getText();
            var exchange = Double.parseDouble(exchangeRate.getText());
            var result = BigDecimal.valueOf(exchange).multiply(debt);

            if (!StringControl.isValid(description))
                throw new EmptyFieldException("Açıklama boş geçilemez....");
            var currencyStr = getCurrency();

            if (currencyStr == null)
                throw new EmptyExchangeTypeException("Döviz Kuru Boş Geçilemez...");

            if (treasureFlow.isSelected())
                addTreasureFlow(date, debt, description, exchange, result, currencyStr);
            else if (fundFlow.isSelected())
                addFundFlow(date, debt, description, exchange, result, currencyStr);
            else if (otherFlow.isSelected())
                addOtherFlow(date, debt, description, exchange, result, currencyStr);

            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Başarıyla eklendi.....", ButtonType.OK);
        }
        catch (NumberFormatException ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Lütfen Sayı Girilecek Yerlere Sayı Giriniz...", ButtonType.OK);
        }
        catch (EmptyFieldException emptyFieldException) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, emptyFieldException.getMessage(), ButtonType.OK);
        }
        catch (EmptyExchangeTypeException emptyExchangeTypeException) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, emptyExchangeTypeException.getMessage(), ButtonType.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void addOtherFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currencyStr)
    {
        SessionFactoryManager.add(new OtherFlow(date, description, debt, exchange, result, currencyStr));
    }

    private void addFundFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currencyStr)
    {
        SessionFactoryManager.add(new FundFlow(date, description, debt, exchange, result, currencyStr));
    }

    private void addTreasureFlow(LocalDate date, BigDecimal debt, String description, double exchange, BigDecimal result, String currencyStr)
    {
        SessionFactoryManager.add(new TreasureFlow(date, description, debt, exchange, result, currencyStr));
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
