package com.gxzd.gxzd.constant;

/**
 * 缓存常量类
 * @author Administrator
 */
public interface CacheInstruction {

    /**
     * 用户缓存过期时间30天
     */
    long USER_CACHE_EXPIRE_TIME = 60*60*24*30;

    /**
     * 缓存名：用户信息的缓存 userCache
     * 缓存值： key：key是用户的id， value：value是存储用户的信息userCache （详见 userCache类）
     */
    String USER_CACHE = "userCache";

}
