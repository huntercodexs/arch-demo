package codexstester.abstractor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Oauth2RequestCheckTokenDto {
    String url;
    String authorization;
    String token;
    String addtionalName;
    String addtionalValue;
}
