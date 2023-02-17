package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@SuppressWarnings("all")
@Entity
@Table(name = "date_mapper")
@Getter
@Setter
public class DateMapper
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapper_pk_id")
    private int id;
    @Column(name = "m_mon", nullable = false)
    private int month;
    @Column(name = "m_year", nullable = false)
    private int year;

    public DateMapper() {}
    public DateMapper(int month, int year)
    {
        this.month = month;
        this.year = year;
    }
}
