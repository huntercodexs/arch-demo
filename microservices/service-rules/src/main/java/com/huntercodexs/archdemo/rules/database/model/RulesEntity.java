package com.huntercodexs.archdemo.rules.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "rules")
public class RulesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column
    public String serviceId;

    @Column
    public String rulesCode;

    @Column
    public int status;

    @Column
    public String createdAt;

    @Column
    public String updatedAt;

}
