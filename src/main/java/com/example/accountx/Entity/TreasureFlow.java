package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.accountx.util.Constant.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

@SuppressWarnings("all")
@Entity
@Table(name = "treasure_flow")
@Getter
@Setter
public class TreasureFlow
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treasure_flow_pk_id")
    private int treasure_flow_pk_id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "debt")
    private BigDecimal debt;

    @Column(name = "exchangeRate")
    private double exchangeRate;

    @Column(name = "resultAmount")
    private BigDecimal resultAmount;

    @Column(name = "currency")
    private String currency;

    public TreasureFlow() {}

    public TreasureFlow(LocalDate date, String description, BigDecimal debt, double exchangeRate, BigDecimal resultAmount, String currency)
    {
        this.date = date;
        this.description = description;
        this.debt = debt;
        this.exchangeRate = exchangeRate;
        this.resultAmount = resultAmount;
        this.currency = currency;
    }
    public String getExcelFormat() {
        var builder = new StringBuilder();
        builder.append(DATE_TIME_FORMATTER.format(date));builder.append(DELIMITER);
        builder.append(description); builder.append(DELIMITER);
        builder.append(getFormattedNumber(debt)); builder.append(DELIMITER);
        builder.append(Double.toString(exchangeRate).replace(".",",") + "(" + currency.toString().replace(".",",") + ")"); builder.append(DELIMITER);
        builder.append(getFormattedNumber(resultAmount));
        return  builder.toString();
    }
}
