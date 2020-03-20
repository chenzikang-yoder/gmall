package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {
    @RequestMapping("addToCart")
    public String addToCart(){
        return "redirect:/success";
    }
    @RequestMapping("success")
    public String success(){
        return "success";
    }
}
