package com.example.accountingX.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@SuppressWarnings("all")
@Entity
@Table(name = "cost_type")
@Getter
@Setter
public class CostType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cost_type_pk_id")
    private int cost_type_pk_id;
    @Column(name = "costType")
    private String costType;

    public CostType() {}
    public CostType(String costType) {
        this.costType = costType;
    }
    @Override
    public String toString()
    {
        return costType;
    }
}
