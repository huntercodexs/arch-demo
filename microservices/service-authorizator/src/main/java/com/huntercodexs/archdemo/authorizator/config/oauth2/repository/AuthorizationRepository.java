package com.huntercodexs.archdemo.authorizator.config.oauth2.repository;

import com.huntercodexs.archdemo.authorizator.config.oauth2.model.AuthorizatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<AuthorizatorEntity, Long> {
	AuthorizatorEntity findByClient(String client);
}
