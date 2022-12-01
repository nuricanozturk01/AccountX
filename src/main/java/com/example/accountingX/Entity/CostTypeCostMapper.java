package com.example.accountingX.Entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.example.accountingX.util.Constant.*;
@Getter
public class CostTypeCostMapper
{
    private final LocalDate date;
    private final String m_costType;
    private final BigDecimal m_costForm;
    private final BigDecimal m_cost;
    private final BigDecimal m_bankFlow;

    private final BigDecimal m_thisMonthCost;

    public CostTypeCostMapper(LocalDate date, String m_costType, BigDecimal m_costForm, BigDecimal m_cost, BigDecimal m_bankFlow, BigDecimal thisMonthCost)
    {
        this.date = date;
        this.m_thisMonthCost = thisMonthCost;
        this.m_costType = m_costType;
        this.m_costForm = m_costForm;
        this.m_cost = m_cost;
        this.m_bankFlow = m_bankFlow;
    }

    public String getExcelFormat()
    {

        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date) + DELIMITER +
                m_costType + DELIMITER +
                m_cost + DELIMITER +
                m_bankFlow + DELIMITER +
                m_costForm + DELIMITER +
                m_thisMonthCost + DELIMITER ;
    }
}
