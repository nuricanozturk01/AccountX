package com.example.accountingX.Entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@SuppressWarnings("all")
@Entity
@Table(name = "Bank_flow")
@Getter
@Setter
public class BankFlow
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_flow_pk_id")
    private int bank_flow_pk_id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @Column(name = "caseFlow")
    private String caseFlow;
    @Column
    private String billingNumber;
    @Column(name = "costType")
    private String costType;
    @Column(name = "cost")
    private BigDecimal cost;

    public BankFlow() {}
    public BankFlow(LocalDate date, LocalTime time, String caseFlow, String costType, BigDecimal cost, String billingNumber)
    {
        this.date = date;
        this.time = time;
        this.caseFlow = caseFlow;
        this.costType = costType;
        this.cost = cost;
        this.billingNumber = billingNumber;
    }
}
