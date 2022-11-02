package com.huntercodexs.archdemo.demo.address.datasource;

import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import org.springframework.http.ResponseEntity;

public class AddressSourcesTest {

    public static final String routerPort = "33001";

    public static final String endpointUri = "/huntercodexs/arch-demo/service-demo/api/address";
    public static final String webhookUrl = "http://your-domain.com/api/1.1/receptor";

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
