package com.huntercodexs.archdemo.router.config.filter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "access_control_router")
public class AccessControlRouterEntity {

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
    private String basicAuth;

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
