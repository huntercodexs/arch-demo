package com.huntercodexs.archdemo.router.config.filter.repository;

import com.huntercodexs.archdemo.router.config.filter.model.AccessControlRouterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessControlRouterRepository extends JpaRepository<AccessControlRouterEntity, Long> {
    AccessControlRouterEntity findByAccessCode(String code);
}
