package com.huntercodexs.codexstester.abstractor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestDto {
    String uri;
    String id;
    String dataRequest;
    String expetecdMessage;
    int expectedCode;
}
