package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {
    public static String getCode() {
        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=1190749188&response_type" +
            "=code&redirect_uri=http://passport.gmall.com:8085/vlogin");
        System.out.println(s1);
        String s2 = "http://passport.gmall.com:8085/vlogin?code=7c2e2297e9bd14381871215dc99c366b";
        return null;
    }

    public static String getaccess_token() {
        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", "1190749188");
        paramMap.put("client_secret", "f633d34390e0d9103e13517f2f9aaf51");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", "http://passport.gmall.com:8085/vlogin");
        paramMap.put("code", "5997b0ba517efcf53f47575ecef08ecd");
        String access_token = HttpclientUtil.doPost(s3, paramMap);
        Map<String, String> access_map = JSON.parseObject(access_token, Map.class);
        return access_map.get("access_token");
    }

    public static Map<String, String> getuser_json(String access_token) {
        String s4 = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid=1";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String, String> user_map = JSON.parseObject(user_json, Map.class);
        return user_map;
    }

    public static void main(String[] args) {

//        getCode();
        getuser_json(getaccess_token());


    }
}
