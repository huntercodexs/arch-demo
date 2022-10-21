package com.huntercodexs.archdemo.router.config.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "oauth2_client_router")
public class Oauth2ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String accessCode;

    @Column
    private String client;

    @Column
    private String secret;

    @Column
    private String grantType;

    @Column
    private String urlCheckToken;

    @Column
    private int status;

    @Column
    private String createdAt;

    @Column
    private String updatedAt;

    @Column
    private String deletedAt;

}
