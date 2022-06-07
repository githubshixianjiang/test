package com.gxzd.gxzd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxzd.gxzd.dto.WXLoginDTO;
import com.gxzd.gxzd.entity.User;
import com.gxzd.gxzd.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface UserService extends IService<User> {

    /**
     * 开发者登录
     * @return
     */
    Result devAuthLogin();

    /**
     * 微信登录
     * @param wxLoginDTO
     * @return
     */
    Result login(WXLoginDTO wxLoginDTO);

    /**
     * 退出登录
     * @return
     */
    Result logout();

    /**
     * 校验token
     * @return
     */
    Result checkToken();
}
