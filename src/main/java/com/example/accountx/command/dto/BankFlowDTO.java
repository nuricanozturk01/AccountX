package com.example.accountx.command.dto;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.command.types.CommandName;
import com.example.accountx.command.types.CommandType;
import lombok.Getter;
import lombok.Setter;

import static com.example.accountx.command.types.CommandName.BANK_FLOW;

@Getter
public class BankFlowDTO
{
    @Setter
    private BankFlow bankFlow;
    private final CommandName commandName = BANK_FLOW;
    private final CommandType commandType;

    public BankFlowDTO(BankFlow bankFlow, CommandType commandType)
    {
        this.bankFlow = bankFlow;
        this.commandType = commandType;
    }

    @Override
    public String toString()
    {
        return "UPDATED: " + bankFlow.toString() + "\n------------------------------------------\n" ;
    }
}
