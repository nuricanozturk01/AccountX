package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.example.accountx.util.Constant.DELIMITER;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

@SuppressWarnings("all")
@Entity
@Table(name = "user")
public class User implements Cloneable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk_id")
    @Getter
    @Setter
    private int user_pk_id;
    @Column(name = "name")
    @Getter
    @Setter
    private String name;
    @Column(name = "surname")
    @Getter
    @Setter
    private String surname;

    @Column(name = "amount")
    @Getter
    @Setter
    private BigDecimal amount;
    public User() {}
    public User(String name, String surname, BigDecimal amount)
    {
        this.name = name;
        this.surname = surname;
        this.amount = amount;
    }
    @Override
    public String toString()
    {
        return name + " " + surname;
    }

    public String generateExcelFormat()
    {
        var sb = new StringBuilder();
        sb.append(name); sb.append(DELIMITER);
        sb.append(surname); sb.append(DELIMITER);
        sb.append(getFormattedNumber(amount)); sb.append(DELIMITER);
        return sb.toString();
    }

    public User clone()
    {
        var usr = new User(name, surname, amount);
        usr.user_pk_id = user_pk_id;
        return usr;
    }
}
