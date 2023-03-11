package com.example.accountx.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.example.accountx.util.Constant.DELIMITER;

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
    public String generateExcelFormat()
    {
        var sb = new StringBuilder();
        sb.append(costType); sb.append(DELIMITER);
        return sb.toString();
    }
}
