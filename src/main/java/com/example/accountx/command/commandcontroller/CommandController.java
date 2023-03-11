package com.example.accountx.command.commandcontroller;

import com.example.accountx.command.ICommand;
import com.example.accountx.command.types.CommandName;

import java.util.Hashtable;

@SuppressWarnings("all")
public class CommandController
{
    private final Hashtable<CommandName, ICommand> m_commands;
    private static final CommandController m_commandClass = new CommandController();


    public CommandController()
    {
        m_commands = new Hashtable<>();
    }

    public static CommandController getInstance()
    {
        return m_commandClass == null ? new CommandController() : m_commandClass;
    }
//----------------------------------------------------------------------------------------------------------
    public void addCommand(CommandName commandName, ICommand<?> command)
    {
        m_commands.put(commandName, command);
    }

    public <T> void acceptApply(CommandName commandName, T obj)
    {
        if (m_commands.containsKey(commandName))
        {
            var cmd = (ICommand<T>) m_commands.get(commandName);

            cmd.applyAdd(obj);
        }
    }

    public <T> void acceptRemove(CommandName commandName, T obj)
    {
        if (m_commands.containsKey(commandName))
        {
            var cmd = (ICommand<T>) m_commands.get(commandName);

            cmd.applyRemove(obj);
        }
    }
    public <T> void acceptUpdate(CommandName commandName, T existingCostForm)
    {
        if (m_commands.containsKey(commandName))
        {
            var cmd = m_commands.get(commandName);

            cmd.applyUpdate(existingCostForm);
        }
    }
    public <T> void acceptUndo(CommandName commandName)
    {
        if (m_commands.containsKey(commandName))
        {
            var cmd = m_commands.get(commandName);

            cmd.undo();
        }
    }
}
