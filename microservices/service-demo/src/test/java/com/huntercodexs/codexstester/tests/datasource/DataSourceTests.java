package com.huntercodexs.codexstester.tests.datasource;

import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.codexstester.abstractor.dto.Oauth2RequestTokenDto;
import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class DataSourceTests {

    public static final String routerPort = "33001";
    public static final String endpointUri = "/huntercodexs/arch-demo/service-demo/api/address";
    public static final String webhookUrl = "http://your-domain.com/api/1.1/receptor";

    public static Oauth2RequestTokenDto dataSourceOAuth2Token() {
        Oauth2RequestTokenDto oauth2RequestTokenDto = new Oauth2RequestTokenDto();
        oauth2RequestTokenDto.setUrl("http://localhost:33001/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token");
        oauth2RequestTokenDto.setAuth("Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==");
        oauth2RequestTokenDto.setGrant("password");
        oauth2RequestTokenDto.setUser("OAUTH2DEMO_USER");
        oauth2RequestTokenDto.setPass("1234567890");
        return oauth2RequestTokenDto;
    }

    public static JSONObject dataSourceAddressRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.appendField("rulesCode", "XYZ12345");
        jsonObject.appendField("postalCode", "12090002");
        jsonObject.appendField("webhook", "");
        return jsonObject;
    }

    public static AddressRequestDto dataSourceAddressRequestDto() {
        AddressRequestDto addressRequestDto = new AddressRequestDto();
        addressRequestDto.setRulesCode("XYZ12345");
        addressRequestDto.setPostalCode("12090002");
        addressRequestDto.setWebhook("");
        return addressRequestDto;
    }

    public static AddressResponseDto dataSourceMapperFinalResponseDto() {
        return new AddressResponseDto();
    }

    public static AddressEntity dataSourceAddressEntityEmpty() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCep("");
        addressEntity.setLogradouro("");
        addressEntity.setComplemento("");
        addressEntity.setBairro("");
        addressEntity.setLocalidade("");
        addressEntity.setUf("");
        addressEntity.setIbge("");
        addressEntity.setGia("");
        addressEntity.setDdd("");
        addressEntity.setSiafi("");
        return addressEntity;
    }

    public static AddressEntity dataSourceAddressEntityFill() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCep("12090000");
        addressEntity.setLogradouro("Avenida Dom Pedro I");
        addressEntity.setComplemento("de 2612/2613 a 3634/3635");
        addressEntity.setBairro("Campos Elíseos");
        addressEntity.setLocalidade("Taubaté");
        addressEntity.setUf("SP");
        addressEntity.setIbge("3554102");
        addressEntity.setGia("6889");
        addressEntity.setDdd("12");
        addressEntity.setSiafi("7183");
        return addressEntity;
    }

    public static ResponseEntity<AddressResponseDto> dataSourceAddressEntityResponse() {
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setCep("12099999");
        addressResponseDto.setLogradouro("Avenida Test");
        addressResponseDto.setComplemento("Complement Test");
        addressResponseDto.setBairro("Bairro Test");
        addressResponseDto.setLocalidade("Localidade Test");
        addressResponseDto.setUf("TT");
        addressResponseDto.setIbge("1234567");
        addressResponseDto.setGia("1234");
        addressResponseDto.setDdd("12");
        addressResponseDto.setSiafi("1234");
        return ResponseEntity.ok().body(addressResponseDto);
    }

}
