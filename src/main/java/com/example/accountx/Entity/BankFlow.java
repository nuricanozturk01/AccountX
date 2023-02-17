package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.accountx.util.Constant.DATE_TIME_FORMATTER;
import static com.example.accountx.util.Constant.DELIMITER;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

@SuppressWarnings("all")
@Entity
@Table(name = "Bank_flow")
@Getter
@Setter
public class BankFlow implements Comparable<BankFlow>
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
    @Transient
    private boolean mark = false;
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

    @Override
    public int hashCode()
    {
        return bank_flow_pk_id;
    }

    @Override
    public boolean equals(Object obj)
    {
        return bank_flow_pk_id == ((BankFlow) obj).bank_flow_pk_id;
    }

    @Override
    public String toString()
    {
        return "BankFlow{" +
                "bank_flow_pk_id=" + bank_flow_pk_id +
                ", date=" + date +
                ", time=" + time +
                ", caseFlow='" + caseFlow + '\'' +
                ", billingNumber='" + billingNumber + '\'' +
                ", costType='" + costType + '\'' +
                ", cost=" + cost +
                ", mark=" + mark +
                '}';
    }

    @Override
    public int compareTo(BankFlow o)
    {
        if (o.getBank_flow_pk_id() == bank_flow_pk_id)
            return 0;

        else if (bank_flow_pk_id > o.getBank_flow_pk_id())
            return 1;

        return -1;
    }
    public String generateExcelFormat()
    {
        var sb = new StringBuilder();
        sb.append(date.format(DATE_TIME_FORMATTER)); sb.append(DELIMITER);
        sb.append(billingNumber); sb.append(DELIMITER);
        sb.append(caseFlow); sb.append(DELIMITER);
        sb.append(costType); sb.append(DELIMITER);
        sb.append(getFormattedNumber(cost)); sb.append(DELIMITER);
        return sb.toString();
    }
}
