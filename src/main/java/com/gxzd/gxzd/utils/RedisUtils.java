package com.gxzd.gxzd.utils;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxzd.gxzd.constant.CacheInstruction;
import com.gxzd.gxzd.constant.WechatConstant;
import com.gxzd.gxzd.entity.UserCache;
import com.gxzd.gxzd.enums.WechatLoginEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author coderFu
 * @create 2021/10/3
 */
@ApiModel("redis工具类")
@Component
public class RedisUtils {


    @Autowired
    private RedisCommonUtils redisCommonUtils;



    /**
     * 根据请求头获取redis中用户的信息
     * @param request 请求头
     * @return 用户信息 userMessage
     */
    public UserCache getRedisUserMessage(HttpServletRequest request){
        //检查Token的有效性
        boolean flag = checkTokenStatus(request);
        if (flag){
            String userToken = getUserToken(request);
            Object userId = redisCommonUtils.get(userToken);
            return (UserCache) redisCommonUtils.hget(CacheInstruction.USER_CACHE, userId);
        }
        ExceptionUtils.error(WechatLoginEnum.INVALID_TOKEN);
        return new UserCache();
    }

    /**
     * 获取用户令牌token
     * @param request 请求头
     * @return 用户令牌token
     */
    public String getUserToken(HttpServletRequest request){
        String token = request.getHeader(WechatConstant.TOKEN);
        if (StringUtils.isNotBlank(token)){
            return token;
        }
        //TOKEN为空，用户未登录
        ExceptionUtils.error(WechatLoginEnum.USER_NOT_LOGIN);
        return "";
    }

    /**
     * 检查Token的有效性 已过期， 请使用 checkTokenStatus(HttpServletRequest request)
     * @param token 待校验token
     * @return true 有效 false 无效
     */
    @Deprecated
    public boolean checkTokenStatus(String token) {
        if (StringUtils.isNotBlank(token)) {
            //判断是否在redis中
            return Boolean.TRUE.equals(redisCommonUtils.hasKey(token));
        }
        return false;
    }

    /**
     * 获取token 并检查token的有效性
     * @param request 请求头
     * @return true 有效  false token无效
     */
    public boolean checkTokenStatus(HttpServletRequest request){
        String token = request.getHeader(WechatConstant.TOKEN);
        if (StringUtils.isNotBlank(token)){
                //判断是否在redis中
            return Boolean.TRUE.equals(redisCommonUtils.hasKey(token));
        }
        //用户未登录
        ExceptionUtils.error(WechatLoginEnum.USER_NOT_LOGIN);
        return  false;
    }

    /**
     * 通过userId获取缓存中的用户信息
     * @param userId 用户id
     * @return
     */
//    public UserCache getRedisUserMessage(Integer userId){
//        if (userId != null){
//            Object hget = redisCommonUtils.hget(CacheInstruction.USER_CACHE, userId);
//            if(hget != null){
//                return (UserCache)redisCommonUtils.hget(CacheInstruction.USER_CACHE, userId);
//            }else {
//                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("user_id",userId);
//                User user = userMapper.selectById(queryWrapper);
//                if (user == null){
//                    ExceptionUtils.error(UserEnum.USER_INFO_NOT_EXIST);
//                }
//                UserCache userCache = new UserCache();
//                BeanUtils.copyProperties(user,userCache);
//                redisCommonUtils.hset(CacheInstruction.USER_CACHE, userId, userCache);
//                return (UserCache)redisCommonUtils.hget(CacheInstruction.USER_CACHE, userId);
//            }
//        }
//        return new UserCache();
//    }

}
