package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.accountx.util.Constant.*;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

@SuppressWarnings("all")
@Table(name = "advance_amount")
@Entity
@Getter
@Setter
public class AdvanceAmount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advance_pk_id")
    private int advance_pk_id;
    @ManyToOne
    @JoinColumn(name = "user_pk_id")
    private User user;
    @Column(name = "amount")
    private BigDecimal amount;

    /*@Column(name = "transfer")
    private boolean transfer;*/

    @Column(name = "amount_date")
    private LocalDate amount_date;

    public AdvanceAmount() {}
    public AdvanceAmount(User user, BigDecimal amount, LocalDate amount_date)
    {
        this.user = user;
        this.amount = amount;
        this.amount_date = amount_date;
    }

    @Override
    public String toString() {

        var sb = new StringBuilder();
        sb.append(user.getName() + " " + user.getSurname());
        sb.append(" [" + DATE_TIME_FORMATTER.format(amount_date) + "] ");
        sb.append(" [" + getFormattedNumber(amount) + " ₺ ]");
        return  sb.toString();
    }
}
