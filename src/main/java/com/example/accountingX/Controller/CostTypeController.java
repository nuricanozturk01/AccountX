package com.example.accountingX.Controller;

import com.example.accountingX.Entity.CostType;
import com.example.accountingX.Exceptions.AlreadyInsertedCostTypeException;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.StringControl;
import com.example.accountingX.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CostTypeController
{
    @FXML
    private TextField costTypeTextField;
    private MainScreenController mainScreenController;

    @FXML
    private void pressKey(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickSaveButton();
    }

    // Dependency Injection
    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    private void addToDatabase(CostType costType)
    {
        SessionFactoryManager.add(costType);
        mainScreenController.getService().addCostTypeTable(costType);
    }
    @FXML
    private void clickSaveButton()
    {
        try
        {
            if (!StringControl.isValid(costTypeTextField.getText()))
                throw new Exception();

            var costTypeString = costTypeTextField.getText();
            var costType = new CostType(costTypeString);

            var isExists = SessionFactoryManager.getCostTypes().stream()
                    .map(CostType::getCostType)
                    .anyMatch(type -> type.equals(costTypeString));

            if (isExists)
                throw new AlreadyInsertedCostTypeException();
            addToDatabase(costType);

            clear();

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Gider türü başarıyla eklendi!", ButtonType.OK);
        }
        catch (AlreadyInsertedCostTypeException ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Bu gider türü zaten mevcut! ", ButtonType.OK);
        }

        catch (Exception ignore)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen boş bırakmayınız!", ButtonType.OK);
        }
    }

    private void clear()
    {
        costTypeTextField.clear();
    }
}
