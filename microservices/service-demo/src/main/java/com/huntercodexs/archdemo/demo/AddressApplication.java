package com.huntercodexs.archdemo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class AddressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressApplication.class, args);
	}

	/**
	 * TESTES
	 *
	 * UNITARIOS
	 * ok - AddressResponseMapper: mapperInitialResponseDto
	 * ok - AddressResponseMapper: mapperFinalResponseDto
	 * ok - AddressResponseMapper: mapperFinalResponseDto
	 * ok - SyncService: runAddressSync
	 * ok - SyncService: saveAddress
	 * ok - ResponseExceptionHandler: handler
	 *
	 * INTEGRAÇÃO
	 * - RulesService: isRulesValid
	 * - AddressService: getAddress SYNC
	 * - AddressService: getAddress SYNC (DATA NOT FOUND)
	 * - AddressService: getAddress ASYNC
	 * - AddressService: getAddress ASYNC (DATA NOT FOUND)
	 * - AddressClient: addressSearch
	 *
	 * */

}
