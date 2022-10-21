package com.huntercodexs.archdemo.demo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RulesResponseDto {
    public Boolean status;
    public String message;
}
