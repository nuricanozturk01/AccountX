package com.example.accountingX.Controller;

import com.example.accountingX.Entity.CostType;
import com.example.accountingX.Exceptions.EmptyFieldException;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UpdateCostTypeController {
    @FXML private TextField costTypeTextField;
    private CostType existingCostType;
    private MainScreenController controller;

    @FXML
    private void pressKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickSaveButton();
    }

    @FXML
    private void clickSaveButton()
    {
       try
       {
           var type = costTypeTextField.getText();

           if (!StringControl.isValid(type))
               throw new EmptyFieldException("Lütfen Alanı Doldurunuz...");

           if (!type.equals(existingCostType.getCostType())) {
               existingCostType.setCostType(type);
               SessionFactoryManager.update(existingCostType);
               UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Gider Türü Başarı ile Güncellendi...", ButtonType.OK);
               controller.getService().initCostTypeTable();
               controller.getService().fillCostTypeChoiceBox();
           }
           else UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Girdiğiniz Gider Türü Öncekiyle Aynı...", ButtonType.OK);
           //throw new UnsupportedOperationException();


       }
       /*catch (UnsupportedOperationException ex) {
           UtilFX.alertScreen(Alert.AlertType.ERROR, "Bu Özellik Diğer sürümde Eklenecek...", ButtonType.OK);
       }*/
       catch (EmptyFieldException ex) {
           UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Lütfen Alanı Doldurunuz...", ButtonType.OK);
       }

    }

    public void setExistingCostType(CostType type) {
        this.existingCostType = type;
        costTypeTextField.setText(existingCostType.getCostType());
    }

    public void setController(MainScreenController mainScreenController) {
        this.controller = mainScreenController;
    }
}
