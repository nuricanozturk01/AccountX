package com.example.accountx.Controller;

import com.example.accountx.Entity.CostType;
import com.example.accountx.Exceptions.EmptyFieldException;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.StringControl;
import com.example.accountx.util.UtilFX;
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

    public void setExistingCostType(CostType type)
    {
        this.existingCostType = type;
        costTypeTextField.setText(existingCostType.getCostType());
    }

    public void setController(MainScreenController mainScreenController)
    {
        this.controller = mainScreenController;
    }

    @FXML
    private void pressKey(KeyEvent keyEvent)
    {
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
       }
       catch (EmptyFieldException ex) {
           UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Lütfen Alanı Doldurunuz...", ButtonType.OK);
       }
    }
}
