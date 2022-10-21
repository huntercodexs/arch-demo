package com.huntercodexs.archdemo.authorizator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class SampleController {

    @GetMapping(path = "/huntercodexs/arch-demo/service-authorizator/api/test")
    @ResponseBody
    public ResponseEntity<?> admin(HttpServletRequest headers) {
        return ResponseEntity.ok().body("Test is running on SERVICE-AUTHORIZATOR from ARCH-DEMO");
    }

}
