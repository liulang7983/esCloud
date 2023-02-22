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
        String s = request.getHeader("s");
        System.out.println("我是es67");
        return s;
    }

    @PostMapping("/test4")
    public String test4(HttpServletRequest request){
        String s = request.getHeader("s");
        System.out.println("我是es67");
        return s;
    }
}
