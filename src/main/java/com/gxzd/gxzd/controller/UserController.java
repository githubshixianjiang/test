package com.gxzd.gxzd.controller;


import com.gxzd.gxzd.dto.WXLoginDTO;
import com.gxzd.gxzd.service.UserService;
import com.gxzd.gxzd.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("开发者登录")
    @PostMapping("devAuthLogin")
    public Result devAuthLogin() {
        return userService.devAuthLogin();
    }

    @ApiOperation("微信授权登录")
    @PostMapping("login")
    public Result login(@Valid @RequestBody WXLoginDTO wxLoginDTO) {
        return userService.login(wxLoginDTO);
    }

    @ApiOperation("校验token")
    @PostMapping("checkToken")
    public Result checkToken() {
        return userService.checkToken();
    }

    @ApiOperation("退出登录")
    @PostMapping("logout")
    public Result logout() {
        return userService.logout();
    }


}

