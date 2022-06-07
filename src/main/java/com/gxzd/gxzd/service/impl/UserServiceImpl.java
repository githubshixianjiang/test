package com.gxzd.gxzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxzd.gxzd.constant.CacheInstruction;
import com.gxzd.gxzd.constant.DataBasesOperationResult;
import com.gxzd.gxzd.constant.WechatConstant;
import com.gxzd.gxzd.dto.WXLoginDTO;
import com.gxzd.gxzd.entity.User;
import com.gxzd.gxzd.entity.UserCache;
import com.gxzd.gxzd.entity.WxInfo;
import com.gxzd.gxzd.enums.UserEnum;
import com.gxzd.gxzd.enums.WechatLoginEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import com.gxzd.gxzd.mapper.UserMapper;
import com.gxzd.gxzd.service.UserService;
import com.gxzd.gxzd.utils.JwtUtils;
import com.gxzd.gxzd.utils.RedisCommonUtils;
import com.gxzd.gxzd.utils.RedisUtils;
import com.gxzd.gxzd.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisCommonUtils redisCommonUtils;

    @Resource
    private RestTemplate restTemplate;


    /**
     * 开发者登录
     *
     * @return
     */
    @Override
    public Result devAuthLogin() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", "String");
        User integer = baseMapper.selectOne(queryWrapper);
        UserCache userCache = new UserCache();
        if (integer == null) {
            User user = new User();
            user.setOpenid("String");
            user.setUsername("String");
            user.setPath("String");

            BeanUtils.copyProperties(user, userCache);

            Integer insert = baseMapper.insert(user);
            if (insert == DataBasesOperationResult.ERROR) {
                log.error("模拟用户数据插入失败");
                ExceptionUtils.error(UserEnum.SAVE_USER_ERROR);
            } else {
                log.info("模拟用户数据插入成功");
                User user1 = baseMapper.selectOne(queryWrapper);
                userCache.setId(user1.getId());
            }
        }
        log.info("查询开发者信息");
        User user = baseMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(user, userCache);

        String token = JwtUtils.getJwtToken(userCache.getId(), user.getUsername());
        redisCommonUtils.set(token, userCache.getId(), 30L, TimeUnit.DAYS);
        redisCommonUtils.hset(CacheInstruction.USER_CACHE, userCache.getId(),userCache);

        return Result.success(token);
    }

    /**
     * 微信登录
     * @param wxLoginDTO
     * @return
     */
    @Override
    public Result login(WXLoginDTO wxLoginDTO) {

        WxInfo wxInfo = getSession(wxLoginDTO.getCode());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", wxInfo.getOpenid());
        User user1 = baseMapper.selectOne(queryWrapper);
        User user = new User();
        if(user1 == null){
            user.setUsername(wxLoginDTO.getUsername());
            user.setOpenid(wxInfo.getOpenid());
            user.setPath(wxLoginDTO.getPath());

            log.info("存储用户信息");
            Integer insert = baseMapper.insert(user);
            if (insert == DataBasesOperationResult.ERROR){
                log.error("存储用户信息失败");
                ExceptionUtils.error(UserEnum.SAVE_USER_ERROR);
            }
        }
        log.info("用户创建成功");
        User user2 = baseMapper.selectOne(queryWrapper);
        UserCache userCache = new UserCache();
        BeanUtils.copyProperties(user2, userCache);

        String token = JwtUtils.getJwtToken(userCache.getId(), userCache.getUsername());
        redisCommonUtils.set(token, userCache.getId(), 30L, TimeUnit.DAYS);
        redisCommonUtils.hset(CacheInstruction.USER_CACHE, userCache.getId(), userCache);

        return Result.success(token);
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public Result logout() {

        String userToken = redisUtils.getUserToken(request);
        try {
            Integer userId = (Integer) redisCommonUtils.get(userToken);
            if (userId == null){
                ExceptionUtils.error(WechatLoginEnum.USER_NOT_LOGIN);
            }
            redisCommonUtils.del(userToken);
        }catch (Exception e){
            ExceptionUtils.error(WechatLoginEnum.USER_NOT_LOGIN);
        }
        return Result.success();
    }

    /**
     * 校验token
     * @return
     */
    @Override
    public Result checkToken() {

        //检查Token的有效性
        boolean flag = redisUtils.checkTokenStatus(request);
        if (flag) {
            try {
                UserCache userCache = redisUtils.getRedisUserMessage(request);
                String token = redisUtils.getUserToken(request);
                //Token有效，更新token的日期
                log.info("更新redis中token的有效时间");
                redisCommonUtils.set(token, userCache.getId(), 30L, TimeUnit.DAYS);
                //将用户信息添加到用户缓存组中
                redisCommonUtils.hset(CacheInstruction.USER_CACHE, userCache.getId(), userCache);
                log.info("更新redis中token的有效时间成功");
            } catch (Exception e) {
                log.info("大概率是【序列化的对象无法匹配】，很小可能是redis挂掉");
                ExceptionUtils.error(WechatLoginEnum.INVALID_TOKEN);
            }
            return Result.success(WechatLoginEnum.TOKEN_EXPIRE_TIME_ALREADY_UPDATE);
        }
        ExceptionUtils.error(WechatLoginEnum.INVALID_TOKEN);
        return Result.success();
    }

    /**
     * 调用微信接口获取session_key, openid ，进行登录凭证校验
     *
     * @param code
     * @return
     */
    public WxInfo getSession(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + WechatConstant.APP_ID + "&secret=" + WechatConstant.SECRET + "&js_code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        if (body == null) {
            //请求接口出现错误
            log.info("请求微信认证信息接口错误,很可能是url路径错误");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        WxInfo wxInfo = null;
        try {
            //将body中的数据（json格式）转为java实体
            wxInfo = objectMapper.readValue(body, WxInfo.class);
            //如果没有请求成功走这里，请求成功不走
            if (wxInfo.getOpenid() == null) {
                //code过期的情况
                if (wxInfo.getErrcode() == WechatConstant.EXPIRE_RESULT_STATUS) {
                    ExceptionUtils.error(WechatLoginEnum.CODE_ALREADY_EXPIRE);
                }
                //code重复使用的情况
            }
        } catch (JsonProcessingException e) {
            log.info("body数据转换java实体错误");
        }
        return wxInfo;
    }
}
