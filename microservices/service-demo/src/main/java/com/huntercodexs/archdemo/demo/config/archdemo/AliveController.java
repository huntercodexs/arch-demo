package com.huntercodexs.archdemo.demo.config.archdemo;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
@Hidden
public class AliveController {

    @Autowired
    AliveService aliveService;

    @Operation(hidden = true)
    @GetMapping(path = "/address/arch-demo-status")
    @ResponseBody
    public String alive(HttpServletRequest request) {
        return aliveService.alive(request);
    }

}
