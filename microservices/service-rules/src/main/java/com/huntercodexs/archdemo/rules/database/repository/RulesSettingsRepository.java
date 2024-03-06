package com.huntercodexs.archdemo.rules.database.repository;

import com.huntercodexs.archdemo.rules.database.model.RulesSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesSettingsRepository extends JpaRepository<RulesSettingsEntity, Long> {
    @Query(value = "SELECT * FROM rules_settings WHERE rulesId = ?1 AND status = 1", nativeQuery = true)
    RulesSettingsEntity findByRulesId(Long id);
}
