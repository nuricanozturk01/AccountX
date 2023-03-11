package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.accountx.util.Constant.*;
@SuppressWarnings("all")
@Table(name = "off_days")
@Entity
@Getter
@Setter
public class OffDay
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "off_day_pk_id")
    private int off_day_pk_id;
    @ManyToOne
    @JoinColumn(name = "user_pk_id")
    private User user;
    @Column(name = "off_day_date")
    private LocalDate off_day_date;
    @Column(name = "off_day_type")
    private String off_day_type;

    public OffDay() {}
    public OffDay(LocalDate date, String offDateType)
    {
        this(null, date, offDateType);
    }
    public OffDay(User user, LocalDate date, String offDateType)
    {
        this.user = user;
        this.off_day_date = date;
        this.off_day_type = offDateType;
    }

    public String ExcelFormat() {

        return off_day_date.format(DATE_TIME_FORMATTER) + DELIMITER + off_day_type;
    }
    @Override
    public String toString()
    {
        if (user == null)
            return getEmptyMessage();
        return off_day_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " [" + off_day_type + "]";
    }
    public String getEmptyMessage()
    {
        return "SONUÇ BULUNAMADI!";
    }
}
