package codexstester.test.unitary;

import codexstester.setup.SetupUnitaryDataSourceTests;
import codexstester.setup.datasource.DataSourceTests;
import codexstester.util.HelperTests;
import com.huntercodexs.archdemo.demo.client.AddressClient;
import com.huntercodexs.archdemo.demo.config.codexsresponser.exception.CodexsResponserException;
import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.database.repository.AddressRepository;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.rules.RulesService;
import com.huntercodexs.archdemo.demo.service.AddressService;
import com.huntercodexs.archdemo.demo.service.SyncService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.huntercodexs.archdemo.demo.config.codexsresponser.settings.CodexsResponserSettings.codexsResponserExpectedErrors.SERVICE_ERROR_TEST;
import static com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper.mapperFinalResponseDtoByEntity;
import static com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper.mapperInitialResponseDto;

public class UnitaryTestsDataSourceTests extends SetupUnitaryDataSourceTests {

    @Autowired
    AddressService addressService;
    @Autowired
    SyncService syncService;
    @Autowired
    RulesService rulesService;
    @Autowired
    AddressClient addressClient;
    @Autowired
    AddressRepository addressRepository;

    @Test
    public void whenMapperInitialResponseDtoTest_FromAddressResponseMapper_AssertExact() {
        AddressResponseDto result = mapperInitialResponseDto();
        codexsTesterAssertExact(HelperTests.md5(result.toString()), HelperTests.md5(new AddressResponseDto().toString()));
    }

    @Test
    public void whenMapperFinalResponseDtoTest_FromAddressResponseMapper_AssertExact() {
        AddressResponseDto addressResponseDto = DataSourceTests.dataSourceMapperFinalResponseDto();
        AddressResponseDto result = mapperFinalResponseDtoByEntity(addressResponseDto);
        codexsTesterAssertExact(HelperTests.md5(result.toString()), HelperTests.md5(new AddressResponseDto().toString()));
    }

    @Test
    public void whenMapperFinalResponseDtoByEntityTest_FromAddressResponseMapper_AssertBoolean() {
        AddressEntity addressEntity = DataSourceTests.dataSourceAddressEntityEmpty();
        mapperFinalResponseDtoByEntity(addressEntity);
        codexsTesterAssertBool(true, true);
    }

    @Test
    public void whenRunAddressSyncTest_FromSyncService_AssertExact() {
        AddressEntity addressEntity = DataSourceTests.dataSourceAddressEntityFill();
        AddressResponseDto result = syncService.runAddressSync(addressEntity.getCep());
        codexsTesterAssertExact(result.getCep().replaceAll("[^0-9]", ""), addressEntity.getCep());
    }

    @Test
    public void whenRunAddressSyncUsingNewCepTest_FromSyncService_AssertExact() {
        String cepTest = "12070020";
        AddressResponseDto result = syncService.runAddressSync(cepTest);
        System.out.println(result);
        codexsTesterAssertExact(result.getCep().replaceAll("[^0-9]", ""), cepTest);
    }

    @Test
    public void whenRunAddressSyncUsingInvalidCepTest_FromSyncService_AssertText() {
        String cepTest = "930706800";
        try {
            AddressResponseDto result = syncService.runAddressSync(cepTest);
        } catch (Exception ex) {
            codexsTesterAssertExact(ex.getMessage(), "Address not found");
        }
    }

    @Test
    public void whenRunAddressSyncUsingWrongCepTest_FromSyncService_AssertExact() {
        String cepTest = "62090004";
        try {
            AddressResponseDto result = syncService.runAddressSync(cepTest);
        } catch (Exception ex) {
            codexsTesterAssertExact(ex.getMessage(), "Address not found");
        }
    }

    @Rollback
    @Transactional
    public void whenSaveAddressTest_FromSyncService_AssertTrue_Windows() {
        System.out.println(System.getProperty("os.name"));
        ResponseEntity<AddressResponseDto> dataFake = DataSourceTests.dataSourceAddressEntityResponse();
        syncService.saveAddress(dataFake);
        AddressEntity result = addressRepository.findByCep(dataFake.getBody().getCep());
        codexsTesterAssertExact(result.getCep(), dataFake.getBody().getCep());
    }

    public void whenSaveAddressTest_FromSyncService_AssertTrue_Linux() {
        System.out.println(System.getProperty("os.name"));
        ResponseEntity<AddressResponseDto> dataFake = DataSourceTests.dataSourceAddressEntityResponse();
        syncService.saveAddress(dataFake);
        AddressEntity result = addressRepository.findByCep(dataFake.getBody().getCep());
        addressRepository.deleteById(result.getId());
        codexsTesterAssertExact(result.getCep(), dataFake.getBody().getCep());
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
            throw new CodexsResponserException(SERVICE_ERROR_TEST);
        } catch (Exception ex) {
            codexsTesterAssertExact(ex.getMessage(), SERVICE_ERROR_TEST.getMessage());
        }
    }

    /**
     * @apiNote Before run this test check if Server Rules is down
     * */
    @Test
    public void whenRunRulesServerButItIsDownTest_AssertText() throws Exception {
        try {
            rulesService.isRulesValid("XYZ-123", "SERVICE-NAME-TEST");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            codexsTesterAssertText("Access Denied", ex.getMessage());
        }
    }

    @Test
    public void whenRunAddressSearchTest_FromAddressClient_AssertTrue() throws Exception {
        ResponseEntity<AddressResponseDto> response = addressClient.addressSearch("12090002");
        codexsTesterAssertExact("12090002", response.getBody().getCep().replaceAll("[^0-9]", ""));
    }

}
