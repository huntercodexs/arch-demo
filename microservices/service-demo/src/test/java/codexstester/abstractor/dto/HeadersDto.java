package codexstester.abstractor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersDto {
    String contentType;
    String httpMethod;
    String statusCode;
    String crossOrigin;
    String origin;
    String hostname;
    String ip;
    String osname;
    String authorizationBasic;
    String authorizationBearer;
    String apiKeyToken;
    String apiKeyAppName;
    String apiKeySecret;
    String apiKeyValue;
    String apiKeyGeneric;
    String addtionalName;
    String addtionalValue;
}
