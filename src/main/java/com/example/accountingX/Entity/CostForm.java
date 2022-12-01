package com.example.accountingX.Entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static com.example.accountingX.util.Constant.*;
@SuppressWarnings("all")
@Entity
@Table(name = "Cost_form")
@Getter
@Setter
public class CostForm
{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cost_form_pk_id")
    private int cost_form_pk_id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @Column(name = "costType", nullable = false)
    private String costType;
    @Column(name = "billingNumber")
    private Integer billingNumber;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "invoiceAmount",nullable = false)
    private BigDecimal invoiceAmount;
    @Column(name = "kdv", nullable = false)
    private double kdv;
    @Column(name = "totalInvoice")
    private BigDecimal totalInvoice;
    @Column(name = "expenditureOfficer", nullable = false)
    private String expenditureOfficer;
    @Column(name = "company")
    private String company;
    @Column(name = "isVoucher")
    private boolean isVoucher;
    @ManyToOne
    @JoinColumn(name = "user_pk_id")
    private User user;


    private CostForm(){}
    public static class Builder
    {
        private final CostForm costForm;
        public Builder()
        {
            costForm = new CostForm();
        }
        public Builder setDate(LocalDate localDate)
        {
            costForm.date = localDate;
            return this;
        }
        public Builder setTime(LocalTime localTime)
        {
            costForm.time = localTime;
            return this;
        }
        public Builder setCostType(String costType)
        {
            costForm.costType = costType;
            return this;
        }
        public Builder setBillingNumber(Integer billingNumber)
        {
            costForm.billingNumber = billingNumber;
            return this;
        }
        public Builder setTDescription(String description)
        {
            costForm.description = description;
            return this;
        }
        public Builder setInvoiceAmount(BigDecimal invoiceAmount)
        {
            costForm.invoiceAmount = invoiceAmount;
            return this;
        }
        public Builder setKDV(double kdv)
        {
            costForm.kdv = kdv;
            return this;
        }
        public Builder setTotalInvoice(BigDecimal totalInvoice)
        {
            costForm.totalInvoice = totalInvoice;
            return this;
        }
        public Builder setExpenditureOfficer(String officer)
        {
            costForm.expenditureOfficer = officer;
            return this;
        }
        public Builder setCompany(String company)
        {
            costForm.company = company;
            return this;
        }
        public Builder setIsVoucher(boolean isVoucher)
        {
            costForm.isVoucher = isVoucher;
            return this;
        }
        public Builder setUser(User user)
        {
            costForm.user = user;
            return this;
        }
        public CostForm build()
        {
            return costForm;
        }
    }

    public boolean getIsVoucher() {
        return isVoucher;
    }
    public String generateExcelFormat()
    {

        final String DATE_FORMAT = "dd/MM/yyyy";
        var sb = new StringBuilder();
        sb.append(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))); sb.append(DELIMITER);
        sb.append(billingNumber); sb.append(DELIMITER);
        sb.append(description); sb.append(DELIMITER);
        sb.append(costType); sb.append(DELIMITER);
        sb.append(invoiceAmount); sb.append(DELIMITER);
        sb.append(kdv); sb.append(DELIMITER);
        sb.append(totalInvoice); sb.append(DELIMITER);
        sb.append(expenditureOfficer); sb.append(DELIMITER);
        sb.append(company);
        return sb.toString();
    }
}
