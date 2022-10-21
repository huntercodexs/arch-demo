package com.huntercodexs.archdemo.rules.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "rules_settings")
public class RulesSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column
    public Long rulesId;

    @Column
    public int status;

    @Column
    public int connections;

    @Column
    public String category;

    @Column
    public String expiredAt;

    @Column
    public String createdAt;

    @Column
    public String updatedAt;

}
