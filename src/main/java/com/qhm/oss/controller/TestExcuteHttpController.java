package com.qhm.oss.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExcuteHttpController {

    @RequestMapping("/sleep")
    public Object  ExcuteSleep(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Map<String,String> result = new HashMap<>();
        //result.put("返回结果","线程休眠五秒后返回结果！");
        return "线程休眠五秒后返回结果!!!";

    }
}
