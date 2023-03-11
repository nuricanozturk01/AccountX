package com.example.accountx.command.dto;

import com.example.accountx.Entity.CostForm;
import com.example.accountx.command.types.CommandName;
import com.example.accountx.command.types.CommandType;
import lombok.Getter;
import lombok.Setter;

import static com.example.accountx.command.types.CommandName.COST_FORM;

@Getter
public class CostFormDTO
{
    @Setter
    private CostForm costForm;
    private final CommandName commandName = COST_FORM;
    private final CommandType commandType;
    public CostFormDTO(CostForm costForm, CommandType commandType)
    {
        this.costForm = costForm;
        this.commandType = commandType;
    }
}
