package codexstester.setup.datasource;

import codexstester.abstractor.SecurityTests;
import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * SAMPLE DATA SOURCE
 * Use this file to create all tests source
 * */
public class DataSourceTests extends SecurityTests {

    /**
     * DEFAULT ATTRIBUTES
     * Change it as needed
     * */

    public static final String samplePort = "33001";
    public static final String sampleEndpointUri = "/huntercodexs/anny-service/api/any-resource";
    public static final String sampleWebhookUrl = "http://your-domain.com/api/1.1/receptor";
    public static final String sampleOauth2Token = "d4cd86a0-809a-40aa-a590-ef68873dcd7b";

    /**
     * SAMPLES
     * Expected Request To Match
     * */

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
