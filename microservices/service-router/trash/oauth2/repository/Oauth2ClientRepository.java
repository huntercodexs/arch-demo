package com.huntercodexs.archdemo.router.config.oauth2.repository;

import com.huntercodexs.archdemo.router.config.oauth2.model.Oauth2ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientRepository extends JpaRepository<Oauth2ClientEntity, Long> {
    Oauth2ClientEntity findByAccessCode(String s);
}
