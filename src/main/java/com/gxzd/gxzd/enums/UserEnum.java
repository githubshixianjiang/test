package com.gxzd.gxzd.enums;

/**
 * 用户枚举
 *
 * @author Administrator
 */

public enum UserEnum implements Code {

    /** 用户相关 */
    SAVE_USER_ERROR(1200, "新增用户失败"),
    USER_INFO_NOT_EXIST(1201, "用户信息不存在"),
    GET_USER_FAILED(1202,"获取我的信息失败"),
    USER_UPDATE_INFO_FAILED(1203,"修改我的信息失败"),
    UPDATE_USER_ADDR_FAILED(1204,"修改地址失败"),
    SAVE_USER_ADDR_FAILED(1205,"新增用户地址失败"),
    FILE_NOT_FOUND(1206,"头像不能为空"),
    USERNAME_NOT_EXIST(1207,"用户名不满足条件"),
    PASSWORD_NOT_EXIST(1208,"密码不满足条件"),
    USER_REPEAT(1209,"用户名重复"),
    USER_LOGIN_FAILED(1210,"用户名或密码错误"),
    IS_NOT_ADMINS(1211,"不是管理员用户"),
    USER_LOGING_NOT_ONE(1212,"非本用户操作"),
    USER_LOGIN_NOT_SHARTING(1213,"非共享账单用户操作"),
    USER_IS_NOT_EXIST(1214,"用户不存在")


    ;

    private int code;
    private String msg;

    UserEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String msg() {
        return this.msg;
    }

}
