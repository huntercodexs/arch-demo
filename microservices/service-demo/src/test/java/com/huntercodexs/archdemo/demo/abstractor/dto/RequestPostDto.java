package com.huntercodexs.archdemo.demo.abstractor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPostDto {
    String uri;
    String id;
    String dataRequest;
    String authKey;
    String authType;
}
