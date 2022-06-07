package com.gxzd.gxzd.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxzd.gxzd.constant.WechatConstant;
import com.gxzd.gxzd.entity.AccessTokenResponse;
import com.gxzd.gxzd.enums.CommonEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * AccessToken工厂类
 */
@Component
public class AccessTokenFactory {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取一个AccessToken
     * @return AccessToken
     */
    public String getAccessToken(){
        // 从redis中拿
        String key = "accessToken";
        Object accessToken = redisTemplate.opsForValue().get(key);
        if (accessToken == null){
            // redis没有，请求一个，并放入redis
            synchronized(AccessTokenFactory.class){
                accessToken = redisTemplate.opsForValue().get("accessToken");
                if (accessToken == null){
                    AccessTokenResponse accessTokenResponse = generateAccessToken();
                    if (accessTokenResponse == null){
                        throw new RuntimeException("获取accessToken异常");
                    }
                    accessToken = accessTokenResponse.getAccess_token();
                    // 将accessToken放入redis
                    Integer expiresTime = accessTokenResponse.getExpires_in();
                    redisTemplate.opsForValue().set(key,accessToken);
                    // 设置accessToken的过期时间
                    redisTemplate.expire(key,expiresTime, TimeUnit.SECONDS);
                }
            }
        }
        return accessToken.toString();
    }

    /**
     * 请求微信api获取accessToken
     * @return
     */
    private AccessTokenResponse generateAccessToken(){
        String accessToken = null;
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ WechatConstant.APP_ID
                +"&secret=" + WechatConstant.SECRET;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCodeValue() != 200){
            // 获取凭证失败
            ExceptionUtils.error(CommonEnum.ERROR);
        }
        String json = responseEntity.getBody();
        // 解析出token
        ObjectMapper objectMapper = new ObjectMapper();
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = objectMapper.readValue(json, AccessTokenResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return accessTokenResponse;
    }

}
