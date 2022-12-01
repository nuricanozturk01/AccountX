package com.example.accountingX.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@SuppressWarnings("all")
@Entity
@Table(name = "user")
public class User
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
    public User() {}
    public User(String name, String surname)
    {
        this.name = name;
        this.surname = surname;
    }
    @Override
    public String toString()
    {
        return name + " " + surname;
    }
}
