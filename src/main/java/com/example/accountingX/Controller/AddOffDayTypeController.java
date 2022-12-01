package com.example.accountingX.Controller;

import com.example.accountingX.Entity.OffDayType;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddOffDayTypeController
{
    @FXML
    private TextField offDayTypeTextField;
    @FXML
    private void pressAddOffDayTypeButton(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickAddOffDayType();
    }
    @FXML
    private void clickAddOffDayType()
    {
        try
        {
            if (!StringControl.isValid(offDayTypeTextField.getText()))
                throw new Exception();

            var type = offDayTypeTextField.getText();
            SessionFactoryManager.add(new OffDayType(type));

            offDayTypeTextField.clear();
            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"İzin Günü Tipi Başarıyla Eklendi!", ButtonType.OK);
        }
        catch (Exception ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen Geçerli bir yazı giriniz!", ButtonType.OK);
        }
    }
}
