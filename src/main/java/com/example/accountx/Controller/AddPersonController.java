package com.example.accountx.Controller;

import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;

public class AddPersonController
{
    private MainScreenController mainScreenController;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;

    public void setMainScreenController(MainScreenController mainScreenController)
    {
        this.mainScreenController = mainScreenController;
    }

    // When you pressed Enter the surname TextField, automatically click on the button
    @FXML
    private void keyPressed(KeyEvent keyEvent)
    {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            clickAddUserButton();
    }
    private void clear()
    {
        nameTextField.clear();
        surnameTextField.clear();
    }
    @FXML
    private void clickAddUserButton()
    {
        try
        {
            var name = nameTextField.getText();
            var surname = surnameTextField.getText();

            clear();

            if (!StringControl.isValid(name,surname) || name.endsWith(" ") || name.startsWith(" ")
                    || surname.endsWith(" ") || surname.startsWith(" "))
                throw new Exception();

            var user = new User(name, surname, BigDecimal.ZERO);

            SessionFactoryManager.add(user);
            mainScreenController.getService().addUserTable(user);
            mainScreenController.getService().updateUserChoiceBox();

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Kişi başarıyla kaydedildi!", ButtonType.OK);
        }
        catch (Exception e)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen isim ve soyismi kontrol ediniz!", ButtonType.OK);
        }
    }
}
