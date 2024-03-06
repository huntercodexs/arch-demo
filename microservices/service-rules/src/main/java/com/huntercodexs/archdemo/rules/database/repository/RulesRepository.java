package com.huntercodexs.archdemo.rules.database.repository;

import com.huntercodexs.archdemo.rules.database.model.RulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesRepository extends JpaRepository<RulesEntity, Long> {

    @Query(value = "SELECT * FROM rules WHERE serviceId = ?1 AND rulesCode = ?2 AND status = 1", nativeQuery = true)
    RulesEntity findByServiceIdAndRulesCode(String serviceId, String rulesCode);
}
