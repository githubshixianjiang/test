package com.gxzd.gxzd.aspect;


import com.gxzd.gxzd.enums.WechatLoginEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import com.gxzd.gxzd.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录切面
 * @author Administrator
 */
@Aspect
@Component
public class RequiresLoginAspect {

    @Resource
    private HttpServletRequest request;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(com.gxzd.gxzd.aspect.annotation.RequiresLogin)")
    public void handle() {
    }

    /**
     * 循环通知
     * 对需要登录的接口进行通知
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "handle()")
    public Object handleAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //从redis中取出token
        String userToken = redisUtils.getUserToken(request);
        if (userToken == null){
            //无token则循环通知
            ExceptionUtils.error(WechatLoginEnum.USER_NOT_LOGIN);
        }
        return proceedingJoinPoint.proceed();
    }

}
