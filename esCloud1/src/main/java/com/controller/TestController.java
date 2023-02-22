package com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ming.li
 * @date 2023/2/22 9:02
 */
@RestController
public class TestController {
    @PostMapping("/test3")
    public String test3(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/t1")
    public String t1(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
}
