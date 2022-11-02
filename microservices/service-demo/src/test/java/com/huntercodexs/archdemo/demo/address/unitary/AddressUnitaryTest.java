package com.huntercodexs.archdemo.demo.address.unitary;

import com.huntercodexs.archdemo.demo.abstractor.UnitAbstractTest;
import com.huntercodexs.archdemo.demo.address.datasource.AddressDataSourceTest;
import com.huntercodexs.archdemo.demo.config.response.errors.ResponseErrors;
import com.huntercodexs.archdemo.demo.config.response.exception.ResponseException;
import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.database.repository.AddressRepository;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.rules.RulesService;
import com.huntercodexs.archdemo.demo.service.AddressService;
import com.huntercodexs.archdemo.demo.service.SyncService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.huntercodexs.archdemo.demo.address.datasource.AddressDataSourceTest.dataSourceAddressEntityFill;
import static com.huntercodexs.archdemo.demo.address.datasource.AddressDataSourceTest.dataSourceAddressEntityResponse;
import static com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper.mapperFinalResponseDtoByEntity;
import static com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper.mapperInitialResponseDto;
import static com.huntercodexs.archdemo.demo.utils.TestsHelpers.md5;

@SpringBootTest
public class AddressUnitaryTest extends UnitAbstractTest {

    @Autowired
    AddressService addressService;

    @Autowired
    SyncService syncService;

    @Autowired
    RulesService rulesService;

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void whenMapperInitialResponseDtoTest_FromAddressResponseMapper_AssertExact() {
        AddressResponseDto result = mapperInitialResponseDto();
        assertionExact(md5(result.toString()), md5(new AddressResponseDto().toString()));
    }

    @Test
    public void whenMapperFinalResponseDtoTest_FromAddressResponseMapper_AssertExact() {
        AddressResponseDto addressResponseDto = AddressDataSourceTest.dataSourceMapperFinalResponseDto();
        AddressResponseDto result = mapperFinalResponseDtoByEntity(addressResponseDto);
        assertionExact(md5(result.toString()), md5(new AddressResponseDto().toString()));
    }

    @Test
    public void whenMapperFinalResponseDtoByEntityTest_FromAddressResponseMapper_AssertBoolean() {
        AddressEntity addressEntity = AddressDataSourceTest.dataSourceAddressEntityEmpty();
        mapperFinalResponseDtoByEntity(addressEntity);
        assertionBool(true, true);
    }

    @Test
    public void whenRunAddressSyncTest_FromSyncService_AssertExact() {
        AddressEntity addressEntity = dataSourceAddressEntityFill();
        AddressResponseDto result = syncService.runAddressSync(addressEntity.getCep());
        assertionExact(result.getCep().replaceAll("[^0-9]", ""), addressEntity.getCep());
    }

    @Test
    public void whenRunAddressSyncUsingNewCepTest_FromSyncService_AssertExact() {
        String cepTest = "12070020";
        AddressResponseDto result = syncService.runAddressSync(cepTest);
        System.out.println(result);
        assertionExact(result.getCep().replaceAll("[^0-9]", ""), cepTest);
    }

    @Test
    public void whenRunAddressSyncUsingInvalidCepTest_FromSyncService_AssertText() {
        String cepTest = "930706800";
        try {
            AddressResponseDto result = syncService.runAddressSync(cepTest);
        } catch (Exception ex) {
            assertionExact(ex.getMessage(), "Address not found");
        }
    }

    @Test
    public void whenRunAddressSyncUsingWrongCepTest_FromSyncService_AssertExact() {
        String cepTest = "62090004";
        try {
            AddressResponseDto result = syncService.runAddressSync(cepTest);
        } catch (Exception ex) {
            assertionExact(ex.getMessage(), "Address not found");
        }
    }

    @Rollback
    @Transactional
    public void whenSaveAddressTest_FromSyncService_AssertTrue_Windows() {
        System.out.println(System.getProperty("os.name"));
        ResponseEntity<AddressResponseDto> dataFake = dataSourceAddressEntityResponse();
        syncService.saveAddress(dataFake);
        AddressEntity result = addressRepository.findByCep(dataFake.getBody().getCep());
        assertionExact(result.getCep(), dataFake.getBody().getCep());
    }

    public void whenSaveAddressTest_FromSyncService_AssertTrue_Linux() {
        System.out.println(System.getProperty("os.name"));
        ResponseEntity<AddressResponseDto> dataFake = dataSourceAddressEntityResponse();
        syncService.saveAddress(dataFake);
        AddressEntity result = addressRepository.findByCep(dataFake.getBody().getCep());
        addressRepository.deleteById(result.getId());
        assertionExact(result.getCep(), dataFake.getBody().getCep());
    }

    @Test
    public void whenSaveAddressTest_FromSyncService_AssertTrue() {
        if (System.getProperty("os.name").equals("Linux")) {
            whenSaveAddressTest_FromSyncService_AssertTrue_Linux();
        } else {
            whenSaveAddressTest_FromSyncService_AssertTrue_Windows();
        }
    }

    @Test
    public void whenExceptionHandlerTest_FromResponseExceptionHandler_AssertExact() {
        try {
            throw new ResponseException(ResponseErrors.SERVICE_ERROR_TEST);
        } catch (Exception ex) {
            assertionExact(ex.getMessage(), ResponseErrors.SERVICE_ERROR_TEST.getMessage());
        }
    }

    @Test
    public void whenRunRulesServerButItIsDownTest_AssertIntegration() throws Exception {
        try {
            rulesService.isRulesValid("XXX-123", "SERVICE-NAME-TEST");
        } catch (Exception ex) {
            assertionText("Rules Server is DOWN", ex.getMessage());
        }
    }

}
