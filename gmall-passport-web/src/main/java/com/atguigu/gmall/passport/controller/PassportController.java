package com.atguigu.gmall.passport.controller;

import com.atguigu.gmall.bean.UmsMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PassportController {
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token) {

        return "token";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember) {

        return "token";
    }

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        if (StringUtils.isNotBlank(ReturnUrl)) {
            modelMap.put("ReturnUrl", ReturnUrl);
        }
        return "index";
    }
}
