package com.huntercodexs.archdemo.rules.controller;

import com.huntercodexs.archdemo.rules.dto.RulesRequestDto;
import com.huntercodexs.archdemo.rules.dto.RulesResponseDto;
import com.huntercodexs.archdemo.rules.service.RulesService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RefreshScope
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
public class RulesController {

	@Autowired
	RulesService rulesService;
	
	@PostMapping(path = "/check-rules")
	@ResponseBody
	public RulesResponseDto checkRules(@RequestBody RulesRequestDto rulesRequestDto) {
		return rulesService.checkRules(rulesRequestDto);
	}
	
}
