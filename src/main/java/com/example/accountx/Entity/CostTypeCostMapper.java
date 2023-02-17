package com.example.accountx.Entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.accountx.util.Constant.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

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
        var sb = new StringBuilder();
        sb.append(DATE_TIME_FORMATTER.format(date)); sb.append(DELIMITER);
        sb.append(m_costType); sb.append(DELIMITER);
        sb.append(getFormattedNumber(m_cost)); sb.append(DELIMITER);
        sb.append(getFormattedNumber(m_bankFlow)); sb.append(DELIMITER);
        sb.append(getFormattedNumber(m_costForm)); sb.append(DELIMITER);
        sb.append(getFormattedNumber(m_thisMonthCost)); sb.append(DELIMITER);
        return sb.toString();
    }
}
