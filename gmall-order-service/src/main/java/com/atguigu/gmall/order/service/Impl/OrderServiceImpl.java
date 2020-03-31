package com.atguigu.gmall.order.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String tradeKey = "user:" + memberId + ":tradeCode";
            String tradeCodeFromCache = jedis.get(tradeKey);
            if (StringUtils.isNotBlank(tradeCodeFromCache) && tradeCodeFromCache.equals(tradeCode)) {
                if (jedis.exists(tradeKey)) {
                    jedis.del(tradeKey);
                }
                return "success";
            } else {
                return "fail";
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public String genTradeCode(String memberId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String tradeKey = "user:" + memberId + ":tradeCode";
            String tradeCode = UUID.randomUUID().toString();
            jedis.setex(tradeKey, 60 * 15, tradeCode);
            return tradeCode;
        } finally {
            jedis.close();
        }

    }
}
