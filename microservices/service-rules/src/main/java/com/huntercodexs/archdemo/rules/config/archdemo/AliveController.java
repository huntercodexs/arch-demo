package com.huntercodexs.archdemo.rules.config.archdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
public class AliveController {

    @Autowired
    AliveService aliveService;

    @GetMapping(path = "/check-rules/arch-demo-status")
    @ResponseBody
    public String alive(HttpServletRequest request) {
        return aliveService.alive(request);
    }

}
