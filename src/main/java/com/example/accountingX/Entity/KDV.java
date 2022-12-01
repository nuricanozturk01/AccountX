package com.example.accountingX.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@SuppressWarnings("all")
@Entity
@Table(name = "kdv")
@Getter
@Setter
public class KDV
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kdv_pk_id")
    private int kdv_pk_id;
    @Column(name = "kdv")
    private Double kdv;

    public KDV() {}
    public KDV(Double kdv) {
        this.kdv = kdv;
    }
}
