package com.example.controller;

import com.example.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitController {

//    @Autowired
//    private InitService initService;
//    @GetMapping("/admin")
//    public String initAdmin(){
//        initService.initAdmin();
//        return "DONE";
//    }

}
