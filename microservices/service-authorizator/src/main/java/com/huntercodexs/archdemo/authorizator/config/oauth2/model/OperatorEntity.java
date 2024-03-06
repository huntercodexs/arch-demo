package com.huntercodexs.archdemo.authorizator.config.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "operator_authorizator")
public class OperatorEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String role;

    @Column
    public String email;

    @Column
    public int deleted;

    @Column
    public int status;

}
