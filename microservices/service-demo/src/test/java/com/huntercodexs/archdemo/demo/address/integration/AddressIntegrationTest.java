package com.huntercodexs.archdemo.demo.address.integration;

import com.huntercodexs.archdemo.demo.abstractor.IntegrationAbstractTest;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.huntercodexs.archdemo.demo.address.datasource.AddressSourcesTest.*;

@SpringBootTest
public class AddressIntegrationTest extends IntegrationAbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Account Opening
     */

//    @Test
//    public void whenRequestToEkataAccountOpeningUsingWrongTokenSync_RetrieveConflict_409() throws Exception {
//        /*NOTE: Before Change the application.properties in ekata.basic-authorization with a wrong token*/
//        JSONObject ekataRequestJsonTest = ekataRequestJsonTest();
//
//        try {
//            conflictByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("409 Null", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenRequestToEkataAccountOpeningAsync_RetrieveAccepted_202() throws Exception {
//        JSONObject ekataRequestJsonTest = ekataRequestJsonTest();
//        ekataRequestJsonTest.put("webhookUrl", webhookUrl);
//
//        try {
//            acceptedByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("202", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenRequestToEkataAccountOpeningWithoutBody_RetrieveBadRequest_400() throws Exception {
//        BdvRequestToEkataDTO bdvRequestToEkataDTO = new BdvRequestToEkataDTO();
//        try {
//            badRequestByHttpPost(endpointUri, "", bdvRequestToEkataDTO.toString());
//        } catch (Exception e) {
//            assertIntegration("400 Bad Request", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenRequestToEkataAccountOpeningWithoutEmailAddress_RetrieveBadRequest_400() throws Exception {
//
//        JSONObject ekataRequestJsonTest = ekataRequestJsonTest();
//        ekataRequestJsonTest.put("emailAddress", "");
//
//        try {
//            badRequestByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("400 Bad Request", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenRequestToEkataAccountOpeningWithoutPhone_RetrieveBadRequest_400() throws Exception {
//
//        JSONObject ekataRequestJsonTest = ekataRequestJsonTest();
//        ekataRequestJsonTest.put("phone", "");
//
//        try {
//            badRequestByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("400 Bad Request", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenRequestToEkataAccountOpeningWithoutSerialNumber_RetrieveBadRequest_400() throws Exception {
//
//        JSONObject ekataRequestJsonTest = ekataRequestJsonTest();
//        ekataRequestJsonTest.put("serialNumber", "");
//
//        try {
//            badRequestByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("400 Bad Request", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenCorrectRequestToEkataAccountOpeningSync_RetrieveUserCreated_201() throws Exception {
//
//        JSONObject ekataRequestJsonTest = realEkataRequestJsonTest();
//
//        try {
//            createdByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("201 Created", e.getMessage());
//        }
//    }
//
//    @Test
//    public void whenCorrectRequestToEkataAccountOpeningASync_RetrieveUserAccepted_202() throws Exception {
//
//        JSONObject ekataRequestJsonTest = realEkataRequestJsonTest();
//        ekataRequestJsonTest.put("webhookUrl", webhookUrl);
//
//        try {
//            acceptedByHttpPost(endpointUri, "", ekataRequestJsonTest.toString());
//        } catch (Exception e) {
//            assertIntegration("202 Accepted", e.getMessage());
//        }
//    }
}
