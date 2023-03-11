package com.example.accountx.Controller;

import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;

public class UpdatePersonController
{
    @FXML
    private TextField advanceAmountTextField;
    private MainScreenController mainScreenController;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    private User user;

    //Dependency Injection
    public void setMainScreenController(MainScreenController mainScreenController)
    {
       try
       {
           this.mainScreenController = mainScreenController;

           user = mainScreenController.getService().getUsersTable().getSelectionModel().getSelectedItem();

           if (user == null)
               throw new Exception();

           nameTextField.setText(user.getName());

           surnameTextField.setText(user.getSurname());

           advanceAmountTextField.setText(user.getAmount().toString());
       }
       catch (Exception ignore) {
       }
    }
    @FXML
    private void pressedEnterOnSurname(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            clickUpdatePerson();
    }
    @FXML
    private void clickUpdatePerson()
    {
        user.setName(nameTextField.getText());

        user.setSurname(surnameTextField.getText());

        user.setAmount(new BigDecimal(advanceAmountTextField.getText()));

        // Update in database
        SessionFactoryManager.update(user);

        // Update in list
        mainScreenController.getService().updateUserTable();

        UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Kişi başarıyla güncellendi!", ButtonType.OK);
    }
}
