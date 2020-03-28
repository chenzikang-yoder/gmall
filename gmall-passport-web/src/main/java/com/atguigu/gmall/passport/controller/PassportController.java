package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;

import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.HttpclientUtil;
import com.atguigu.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @Reference
    UserService userService;

    @RequestMapping("vlogin")
    public String vlogin(String code, HttpServletRequest request) {
        String url = "https://api.weibo.com/oauth2/access_token?";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", "1190749188");
        paramMap.put("client_secret", "f633d34390e0d9103e13517f2f9aaf51");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", "http://passport.gmall.com:8085/vlogin");
        paramMap.put("code", code);
        String access_token_json = HttpclientUtil.doPost(url, paramMap);
        Map<String, Object> access_map = JSON.parseObject(access_token_json, Map.class);
        String uid = (String) access_map.get("uid");
        String access_token = (String) access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String, Object> user_map = JSON.parseObject(user_json, Map.class);
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        umsMember.setAccessToken(access_token);
        umsMember.setSourceUid((String) user_map.get("idstr"));
        umsMember.setAccessCode(code);
        umsMember.setCity((String) user_map.get("location"));
        umsMember.setGender(user_map.get("gender") == "m" ? "1" : "2");
        umsMember.setNickname((String) user_map.get("screen_name"));
        UmsMember umsCheck = new UmsMember();
        umsCheck.setSourceUid(umsMember.getSourceUid());
        UmsMember umsMemberCheck = userService.checkOauthUser(umsCheck);
        if (umsMemberCheck == null) {
            userService.addOutUser(umsMember);
        }else {
            umsMember=umsMemberCheck;
        }
        String memberId = umsMember.getId();
        String nickname = umsMember.getNickname();
        String token = Gettoken(memberId, nickname, request);
        return "redirect:http://search.gmall.com:8083/index?token="+token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp) {
        Map<String, String> modelMap = new HashMap<>();
        Map<String, Object> decode = null;
        try {
            decode = JwtUtil.decode(token, "2019gmall110105", currentIp);
        } catch (Exception e) {

        }
        if (decode != null) {
            modelMap.put("status", "success");
            modelMap.put("memberId", (String) decode.get("memberId"));
            modelMap.put("nickname", (String) decode.get("nickname"));
        } else {
            modelMap.put("status", "fail");
        }
        return JSON.toJSONString(modelMap);
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request) {
        String token = "";
        UmsMember umsMemberLogin = userService.login(umsMember);
        if (umsMemberLogin != null) {
            String memberId = umsMemberLogin.getId();
            String nickname = umsMemberLogin.getNickname();
            token=Gettoken(memberId,nickname,request);
        } else {
            token = "fail";
        }
        return token;
    }

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        if (StringUtils.isNotBlank(ReturnUrl)) {
            modelMap.put("ReturnUrl", ReturnUrl);
        }
        return "index";
    }
    private String Gettoken(String memberId,String nickname,HttpServletRequest request ){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("memberId", memberId);
        userMap.put("nickname", nickname);

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
        }
        String token = JwtUtil.encode("2019gmall110105", userMap, ip);
        userService.addUserToken(token, memberId);
        return token;
    }
}
