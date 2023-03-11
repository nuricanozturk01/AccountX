package com.example.accountx.command.commands;

import com.example.accountx.Controller.MainScreenController;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.ICommand;
import com.example.accountx.command.dto.CostFormDTO;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.EmptyStackException;
import java.util.Stack;

import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.*;

public class CommandCostForm implements ICommand<CostFormDTO>
{
    private final Stack<CostFormDTO> m_costFormDTOS;

    private final MainScreenController m_screenController;
    public CommandCostForm(MainScreenController mainScreenController)
    {
        m_costFormDTOS = new Stack<>();
        m_screenController = mainScreenController;
    }
    @Override
    public CostFormDTO applyAdd(CostFormDTO item)
    {
       return m_costFormDTOS.push(item);
    }
    @Override
    public CostFormDTO applyRemove(CostFormDTO item)
    {
        return m_costFormDTOS.push(item);
    }

    @Override
    public CostFormDTO applyUpdate(CostFormDTO item)
    {
        item.setCostForm(item.getCostForm().clone());
        item.getCostForm().setUser(item.getCostForm().getUser().clone());

        return m_costFormDTOS.push(item);
    }
    @Override
    public CostFormDTO undo()
    {
       try
       {
           var item = m_costFormDTOS.peek();

           if (item == null)
               return null;

           switch (item.getCommandType())
           {
               case APPLY -> applyItem();
               case REMOVE -> removeItem();
               case UPDATE -> updateItem();
           }

           return item;
       }
       catch (EmptyStackException ex)
       {
           UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Daha önce ekleme, çıkarma veya güncelleme yapmadığınız için işlem başarısız.", ButtonType.OK);
       }
       return null;
    }
    private void updateItem()
    {
        try
        {
            var item = m_costFormDTOS.pop();

            var before = item.getCostForm();

            var cf = item.getCostForm();

            System.out.println("BEFORE: " + before.getCostType());

            cf.setCostType(before.getCostType());
            cf.setUser(before.getUser());
            cf.setExpenditureOfficer(before.getUser().getName() + " " + before.getUser().getSurname());
            cf.setDate(before.getDate());
            cf.setDescription(before.getDescription());
            cf.setCompany(before.getCompany());
            cf.setVoucher(before.getIsVoucher());
            cf.setBillingNumber(before.getBillingNumber());
            cf.setTotalInvoice(before.getTotalInvoice());
            cf.setInvoiceAmount(before.getInvoiceAmount());
            cf.setKdv(before.getKdv());

            update(cf);
            update(cf.getUser());


            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Güncellendi, No: " + cf.getCost_form_pk_id(), ButtonType.OK);

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Daha önce sildiğiniz için güncellenemiyor...", ButtonType.OK);
        }
    }
    private void removeItem()
    {
        try
        {
            var item = m_costFormDTOS.pop();
            var cf = item.getCostForm();

            var beforeAdd = cf.clone();

            add(cf);
            cf.getUser().setAmount(cf.getUser().getAmount().abs().add(cf.getTotalInvoice()));
            update(cf.getUser());

            var lastItem = SessionFactoryManager.getCostForm();

            for (CostFormDTO costFormDTO : m_costFormDTOS)
            {
                var costForm = costFormDTO.getCostForm();

                if (costForm.getCost_form_pk_id() == beforeAdd.getCost_form_pk_id())
                    costForm.setCost_form_pk_id(lastItem.getCost_form_pk_id());
            }

            m_screenController.update(cf);

            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Eklendi, No: " + cf.getCost_form_pk_id(), ButtonType.OK);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Daha önce güncelleme yaptığınız için silinemiyor...", ButtonType.OK);
        }
    }
    private void applyItem()
    {
       try
       {
           var item = m_costFormDTOS.pop();
           var cf = item.getCostForm();

           remove(cf);
           cf.getUser().setAmount(cf.getUser().getAmount().subtract(cf.getTotalInvoice()));
           update(cf.getUser());

           //m_screenController.update(cf);
           m_screenController.getService().initCostFlowTable();
           UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Silindi, No: " + cf.getCost_form_pk_id(), ButtonType.OK);
       }
       catch (Exception ex)
       {
           UtilFX.alertScreen(Alert.AlertType.ERROR, "Silinirken Sorun yaşandı!!!", ButtonType.OK);
       }
    }
}