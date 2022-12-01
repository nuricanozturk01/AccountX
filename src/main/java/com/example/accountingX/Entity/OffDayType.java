package com.example.accountingX.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@SuppressWarnings("all")
@Table(name = "off_day_types")
@Entity
@Getter
@Setter
public class OffDayType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "off_day_types_pk_id")
    private int off_day_types_pk_id;
    @Column(name = "type", unique = true)
    private String type;
    public OffDayType(){}
    public OffDayType(String type) {
        this.type = type;
    }
    @Override
    public String toString()
    {
        return type;
    }
}
