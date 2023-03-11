package com.example.accountx.command.commands;

import com.example.accountx.Controller.MainScreenController;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.ICommand;
import com.example.accountx.command.dto.MultipleBankFlowDTO;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.EmptyStackException;
import java.util.Stack;

public class CommandMultipleBankFlow implements ICommand<MultipleBankFlowDTO>
{
    private final Stack<MultipleBankFlowDTO> m_costFormDTOS;
    private final MainScreenController m_screenController;
    public CommandMultipleBankFlow(MainScreenController mainScreenController)
    {
        m_costFormDTOS = new Stack<>();
        m_screenController = mainScreenController;
    }
    @Override
    public MultipleBankFlowDTO applyAdd(MultipleBankFlowDTO item)
    {
        return m_costFormDTOS.push(item);
    }
    @Override
    public MultipleBankFlowDTO applyRemove(MultipleBankFlowDTO item)
    {
        return m_costFormDTOS.push(item);
    }
    @Override
    public MultipleBankFlowDTO applyUpdate(MultipleBankFlowDTO item)
    {
        throw new UnsupportedOperationException("UNSUPPORTED OPERATION...");
    }

    @Override
    public MultipleBankFlowDTO undo()
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
               //case UPDATE -> updateItem();
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
        var item = m_costFormDTOS.pop();
        SessionFactoryManager.updateAll(item.getBankFlows());
    }
    private void removeItem()
    {
        var item = m_costFormDTOS.pop();
        var bankFlows = item.getBankFlows();

        SessionFactoryManager.addAll(bankFlows);

        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Başarı ile geri alındı....", ButtonType.OK);

    }
    private void applyItem()
    {
        var item = m_costFormDTOS.pop();
        var bankFlows = item.getBankFlows();
        SessionFactoryManager.removeAll(bankFlows);
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Başarı ile geri alındı....", ButtonType.OK);
    }
}