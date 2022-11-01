package com.huntercodexs.archdemo.demo.service;

import com.huntercodexs.archdemo.demo.client.AddressClient;
import com.huntercodexs.archdemo.demo.config.response.errors.ResponseErrors;
import com.huntercodexs.archdemo.demo.config.response.exception.ResponseException;
import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.database.repository.AddressRepository;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper.mapperFinalResponseDto;

@RefreshScope
@Service
@Slf4j
public class SyncService {

    @Autowired
    AddressClient addressClient;

    @Autowired
    AddressRepository addressRepository;

    public AddressResponseDto runAddressSync(String postalCode) {

        AddressEntity address = addressRepository.findByCep(postalCode);

        if (address != null && !address.getCep().equals("")) {
            return mapperFinalResponseDto(address);
        }

        ResponseEntity<AddressResponseDto> result = addressClient.addressSearch(postalCode);

        if (result.getBody().getCep() != null && !result.getStatusCode().is4xxClientError()) {
            saveAddress(result);
            return result.getBody();
        }

        throw new ResponseException(ResponseErrors.SERVICE_ERROR_NOT_FOUND);
    }

    private void saveAddress(ResponseEntity<AddressResponseDto> result) {
        AddressEntity addressEntity = new AddressEntity();
        try {
            addressEntity.setCep(Objects.requireNonNull(result.getBody()).getCep().replace("-", ""));
            addressEntity.setLogradouro(Objects.requireNonNull(result.getBody()).getLogradouro());
            addressEntity.setComplemento(Objects.requireNonNull(result.getBody()).getComplemento());
            addressEntity.setBairro(Objects.requireNonNull(result.getBody()).getBairro());
            addressEntity.setLocalidade(Objects.requireNonNull(result.getBody()).getLocalidade());
            addressEntity.setUf(Objects.requireNonNull(result.getBody()).getUf());
            addressEntity.setIbge(Objects.requireNonNull(result.getBody()).getIbge());
            addressEntity.setGia(Objects.requireNonNull(result.getBody()).getGia());
            addressEntity.setDdd(Objects.requireNonNull(result.getBody()).getDdd());
            addressEntity.setSiafi(Objects.requireNonNull(result.getBody()).getSiafi());
            addressRepository.save(addressEntity);
        } catch (RuntimeException re) {
            System.out.println("saveAddress EXCEPTION: " + re.getMessage());
        }
    }
}
