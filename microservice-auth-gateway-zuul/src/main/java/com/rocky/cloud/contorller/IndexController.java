package com.rocky.cloud.contorller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rocky on 2017-11-13.
 */
@RestController
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "API-GateWay";
    }
}
