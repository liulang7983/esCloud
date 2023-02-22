package com.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ming.li
 * @date 2022/11/17 9:33
 */
@RestController
public class JobFullController {

    @PostMapping("/test")
    public String test(HttpServletRequest request){
        System.out.println("我是es66");
        String s = request.getHeader("s");
        return s;
    }


    @PostMapping("/test1")
    public String test1(HttpServletRequest request){
        System.out.println("我是es66");
        String s = request.getHeader("s");
        return s;
    }

    @PostMapping("/t2")
    public String t2(HttpServletRequest request){
        System.out.println("我是es66");
        String s = request.getHeader("s");
        return s;
    }

    @PostMapping("/t5")
    public String t5(HttpServletRequest request){
        System.out.println("我是es66");
        String s = request.getHeader("s");
        return s;
    }
}
