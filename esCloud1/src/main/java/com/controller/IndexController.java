package com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ming.li
 * @date 2023/2/22 9:02
 */
@RestController
public class IndexController {
    @PostMapping("/index1")
    public String index1(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index2")
    public String index2(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index3")
    public String index3(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index5")
    public String index4(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index7")
    public String index7(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index9")
    public String index9(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index4")
    public String index4(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index6")
    public String index6(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index8")
    public String index8(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
    @PostMapping("/index10")
    public String index10(HttpServletRequest request){
        System.out.println("TestController");
        String s = request.getHeader("s");
        return s;
    }
}
