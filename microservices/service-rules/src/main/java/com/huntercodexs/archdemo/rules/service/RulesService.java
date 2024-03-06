package com.huntercodexs.archdemo.rules.service;

import com.huntercodexs.archdemo.rules.database.model.RulesEntity;
import com.huntercodexs.archdemo.rules.database.model.RulesSettingsEntity;
import com.huntercodexs.archdemo.rules.database.repository.RulesRepository;
import com.huntercodexs.archdemo.rules.database.repository.RulesSettingsRepository;
import com.huntercodexs.archdemo.rules.dto.RulesRequestDto;
import com.huntercodexs.archdemo.rules.dto.RulesResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RefreshScope
@Service
@Slf4j
public class RulesService {

    @Autowired
    RulesRepository rulesRepository;

    @Autowired
    RulesSettingsRepository rulesSettingsRepository;

    public RulesResponseDto checkRules(RulesRequestDto rulesRequestDto) {

        RulesResponseDto rulesResponseDto = new RulesResponseDto();

        try {

            RulesEntity rules = rulesRepository.findByServiceIdAndRulesCode(rulesRequestDto.getServiceId(), rulesRequestDto.getRulesCode());
            RulesSettingsEntity rulesSettings = rulesSettingsRepository.findByRulesId(rules.getId());

            /*Rules Sample*/
            if (rules.getStatus() == 1 && rulesSettings.getStatus() == 1) {
                rulesResponseDto.setStatus(true);
                rulesResponseDto.setMessage("Rules OK");
            } else {
                rulesResponseDto.setStatus(false);
                rulesResponseDto.setMessage("Rules NOK");
            }

        } catch (RuntimeException re) {
            rulesResponseDto.setStatus(false);
            rulesResponseDto.setMessage("Rules Exception: " + re.getMessage());
        }

        return rulesResponseDto;

    }
}
