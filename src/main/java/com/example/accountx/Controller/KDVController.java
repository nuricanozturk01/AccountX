package com.example.accountx.Controller;

import com.example.accountx.Entity.KDV;
import com.example.accountx.Exceptions.AlreadyExistsKDVException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
public class KDVController
{
    @FXML
    private TextField kdvTextField;

    @FXML
    private void pressAddKDVButton(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickAddKDV();
    }
    @FXML
    private void clickAddKDV()
    {
       try
       {
           if (!StringControl.isValid(kdvTextField.getText()))
               throw new Exception();

           var kdv = Double.parseDouble(kdvTextField.getText());

           if (SessionFactoryManager.getKDVs().stream().anyMatch(k -> k.getKdv().equals(kdv)))
               throw new AlreadyExistsKDVException();

           SessionFactoryManager.add(new KDV(kdv));

           kdvTextField.clear();

           UtilFX.alertScreen(Alert.AlertType.INFORMATION,"KDV değeri başarıyla eklendi!", ButtonType.OK);
       }
       catch (AlreadyExistsKDVException alreadyExistsKDVException)
       {
           UtilFX.alertScreen(Alert.AlertType.ERROR,"KDV değeri zaten mevcut!", ButtonType.OK);
           kdvTextField.clear();
       }
       catch (Exception ex)
       {
           UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen Geçerli bir kdv değeri giriniz!", ButtonType.OK);
           kdvTextField.clear();
       }
    }
}
