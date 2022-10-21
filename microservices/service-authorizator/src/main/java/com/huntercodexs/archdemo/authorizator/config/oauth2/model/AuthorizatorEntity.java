package com.huntercodexs.archdemo.authorizator.config.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "client_authorizator")
public class AuthorizatorEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Column
    public String client;

    @Column
    public String secret;

    @Column
    public String scope;

    @Column
    public int accessTokenValiditySeconds;

    @Column
    public int refreshTokenValiditySeconds;
    
}
