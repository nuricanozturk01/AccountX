package com.example.accountx.Controller;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.User;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.update;
import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.add;
public class AdvanceMoneyController
{
    @FXML
    private Label netAdvanceAmountLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<AdvanceAmount> advanceAmountListView;
    @FXML
    private TextField amountTextField;
    @FXML
    private ChoiceBox<User> personChoiceBox;

    private List<AdvanceAmount> existAmounts;

    @FXML
    private void initialize()
    {
        amountTextField.setEditable(false);
        SessionFactoryManager.getUserList().forEach(personChoiceBox.getItems()::add);
        datePicker.setValue(LocalDate.now());
    }
    @FXML
    private void clickGet()
    {
        advanceAmountListView.getItems().clear();
        var date = datePicker.getValue();
        var year = date.getYear();
        var user = personChoiceBox.getSelectionModel().getSelectedItem();
        var month = date.getMonthValue();

        existAmounts = SessionFactoryManager.getAdvanceAmounts(user.getUser_pk_id(), year, month);

        if (existAmounts != null)
            existAmounts.forEach(advanceAmountListView.getItems()::add);

        amountTextField.setEditable(true);

        netAdvanceAmountLabel.setText(user.getAmount() + " ₺");
    }
    @FXML
    private void clickSave()
    {
        try
        {
            var date = datePicker.getValue();
            var amount = new BigDecimal(amountTextField.getText());
            var user = personChoiceBox.getSelectionModel().getSelectedItem();

            if (amount.compareTo(BigDecimal.ZERO) < 0)
                throw new NumberFormatException();

            var advanceAmount = new AdvanceAmount(user, amount, date);

            user.setAmount(user.getAmount().add(amount));

            add(advanceAmount);
            update(user);

            UtilFX.alertScreen(Alert.AlertType.INFORMATION,"Avans miktarı başarı ile girildi", ButtonType.OK);
            advanceAmountListView.getItems().add(advanceAmount);
        }

        catch (NumberFormatException ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR,"Lütfen Geçerli Bir sayı giriniz!", ButtonType.OK);
        }
    }
}
