package com.atguigu.gmall.interceptors;


import com.atguigu.gmall.annotations.LosinRequired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        LosinRequired methodAnnotation = hm.getMethodAnnotation(LosinRequired.class);
        if (methodAnnotation == null) {
            return true;
        }
        boolean loginSuccess = methodAnnotation.loginSuccess();

        return true;
    }
}

