package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.UserService;
import org.assertj.core.internal.cglib.asm.$Handle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Reference
    CartService cartService;
    @Reference
    UserService userService;

    @Reference
    OrderService orderService;

    @RequestMapping("submitOrder")
    @LoginRequired(loginSuccess = true)
    public String submitOrder(String receiveAddressId,BigDecimal totalAmount,String tradeCode,HttpServletRequest request,
                                HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        String success=orderService.checkTradeCode(memberId,tradeCode);
        if (success.equals("success")){

        }else {

        }
        return null;
    }

    @RequestMapping("toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        List<UmsMemberReceiveAddress> umsMemberReceiveAddress = userService.getReceiveAddressByMemberId(memberId);
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }
        modelMap.put("nickName", nickname);
        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("userAddressList", umsMemberReceiveAddress);
        modelMap.put("totalAmount", getTotalAmount(omsCartItems));
        String tradeCode=orderService.genTradeCode(memberId);
        modelMap.put("tradeCode",tradeCode);
        return "trade";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            try {
                if (omsCartItem.getIsChecked().equals("1")) {
                    totalAmount = totalAmount.add(totalPrice);
                }
            } catch (Exception e) {
            }
        }

        return totalAmount;
    }
}
