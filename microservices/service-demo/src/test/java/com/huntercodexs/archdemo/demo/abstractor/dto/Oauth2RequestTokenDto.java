package com.huntercodexs.archdemo.demo.abstractor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Oauth2RequestTokenDto {
    String url;
    String auth;
    String grant;
    String user;
    String pass;
}
