package com.huntercodexs.archdemo.demo.database.repository;

import com.huntercodexs.archdemo.demo.database.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    @Query(value = "SELECT * from address_service_demo WHERE cep = ?1", nativeQuery = true)
    AddressEntity findByCep(String cep);
}
