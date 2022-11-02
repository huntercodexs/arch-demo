package com.huntercodexs.archdemo.demo.mapper;

import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AddressResponseMapper {

    public static AddressResponseDto mapperInitialResponseDto() {
        return new AddressResponseDto();
    }

    public static AddressResponseDto mapperFinalResponseDtoByEntity(AddressResponseDto addressResponseDto) {
        return new AddressResponseDto();
    }

    public static AddressResponseDto mapperFinalResponseDtoByEntity(AddressEntity addressEntity) {
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setCep(addressEntity.getCep());
        addressResponseDto.setLogradouro(addressEntity.getLogradouro());
        addressResponseDto.setComplemento(addressEntity.getComplemento());
        addressResponseDto.setBairro(addressEntity.getBairro());
        addressResponseDto.setLocalidade(addressEntity.getLocalidade());
        addressResponseDto.setUf(addressEntity.getUf());
        addressResponseDto.setIbge(addressEntity.getIbge());
        addressResponseDto.setGia(addressEntity.getGia());
        addressResponseDto.setDdd(addressEntity.getDdd());
        addressResponseDto.setSiafi(addressEntity.getSiafi());
        return addressResponseDto;
    }

}
