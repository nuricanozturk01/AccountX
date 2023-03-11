package com.example.accountx.command.dto;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.command.types.CommandName;
import com.example.accountx.command.types.CommandType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.example.accountx.command.types.CommandName.MULTIPLE_BANK_FLOW;

@Getter
public class MultipleBankFlowDTO
{
    @Setter
    private List<BankFlow> bankFlows;

    private final CommandName commandName = MULTIPLE_BANK_FLOW;
    private final CommandType commandType;
    public MultipleBankFlowDTO(List<BankFlow> bankFlows, CommandType commandType)
    {
        this.bankFlows = bankFlows;
        this.commandType = commandType;
    }

}
