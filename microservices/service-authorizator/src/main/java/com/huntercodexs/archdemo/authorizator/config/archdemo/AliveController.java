package com.huntercodexs.archdemo.authorizator.config.archdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${oauth.server.custom.endpoint}")
public class AliveController {

    @Autowired
    AliveService aliveService;

    @GetMapping(path = "/token/arch-demo-status")
    @ResponseBody
    public String token(HttpServletRequest request) {
        return aliveService.alive(request);
    }

    @GetMapping(path = "/check_token/arch-demo-status")
    @ResponseBody
    public String checkToken(HttpServletRequest request) {
        return aliveService.alive(request);
    }

}
