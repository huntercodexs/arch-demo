package com.huntercodexs.archdemo.demo.utils;

import java.util.UUID;

public class Helpers {

    public static String tcnGen(String tcn) {
        if (tcn == null || tcn.equals("")) {
            return UUID.randomUUID().toString();
        }
        return tcn;
    }

}
