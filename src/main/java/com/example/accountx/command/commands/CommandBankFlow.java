package com.example.accountx.command.commands;

import com.example.accountx.Controller.MainScreenController;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.ICommand;
import com.example.accountx.command.dto.BankFlowDTO;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.EmptyStackException;
import java.util.Stack;

import static com.example.accountx.HibernateConfiguration.SessionFactoryManager.*;

public class CommandBankFlow implements ICommand<BankFlowDTO>
{
    private final Stack<BankFlowDTO> m_bankFlows;
    private final MainScreenController m_screenController;

    public CommandBankFlow(MainScreenController mainScreenController)
    {
        m_bankFlows = new Stack<>();
        m_screenController = mainScreenController;
    }
    @Override
    public BankFlowDTO applyAdd(BankFlowDTO item)
    {
        var clone = item.getBankFlow().clone();
        item.setBankFlow(clone);
        return m_bankFlows.push(item);
    }


    @Override
    public BankFlowDTO applyRemove(BankFlowDTO item)
    {
        return m_bankFlows.push(item);
    }

    @Override
    public BankFlowDTO applyUpdate(BankFlowDTO item)
    {
        var clone = item.getBankFlow().clone();
        item.setBankFlow(clone);
        return m_bankFlows.push(item);
    }

    @Override
    public BankFlowDTO undo()
    {
       try
       {
           var item = m_bankFlows.peek();

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
            var item = m_bankFlows.pop(); //updated item

            update(item.getBankFlow());

            m_screenController.update(item.getBankFlow());

            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Güncellendi, No: " + item.getBankFlow().getBank_flow_pk_id(), ButtonType.OK);
        }
        catch (Exception ex)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Daha önce sildiğiniz için güncellenemiyor...", ButtonType.OK);
        }
    }
    private void removeItem()
    {
       try
       {
           var item = m_bankFlows.pop();
           var bankFlow = item.getBankFlow();

           var beforeAdd = bankFlow.clone();

           add(bankFlow);

           var afterAdd = SessionFactoryManager.getBankFlow();

           for (BankFlowDTO bankFlowDTO : m_bankFlows)
           {
               var bf = bankFlowDTO.getBankFlow();

               if (bf.getBank_flow_pk_id() == beforeAdd.getBank_flow_pk_id())
                   bf.setBank_flow_pk_id(afterAdd.getBank_flow_pk_id());
           }

           m_screenController.update(afterAdd);
           UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Eklendi, No: " + afterAdd.getBank_flow_pk_id(), ButtonType.OK);
       }
       catch (Exception ex)
       {
           UtilFX.alertScreen(Alert.AlertType.ERROR, "Daha önce güncelleme yaptığınız için silinemiyor...", ButtonType.OK);
       }
    }
    private void applyItem()
    {
        var item = m_bankFlows.pop();

        remove(item.getBankFlow());

        m_screenController.getService().initBankFlowTable();

        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Silindi, No: " + item.getBankFlow().getBank_flow_pk_id(), ButtonType.OK);
    }
}