package com.gxzd.gxzd.enums;

/**
 * @author coderFu
 * @create 2021/9/30
 */
public enum WechatLoginEnum implements Code {

    /**
     * 微信授权登录相关
     */
    OPENID_IS_EMPTY(1120,"openid凭证为空"),
    GET_SECRET_FAILED(1121,"获取加密数据失败"),
    PHONE_TYPE_TRANSFORM_FAILED(1122,"手机类型转换错误"),
    UPDATE_AUTHORIZATION_MESSAGE_FAILED(1123,"更新授权信息失败"),
    INVALID_TOKEN(1124,"无效token"),
    USER_NOT_LOGIN(1125,"用户未登录"),
    CODE_ALREADY_EXPIRE(1126,"CODE已失效"),
    TOKEN_EXPIRE_TIME_ALREADY_UPDATE(1127,"Toke时效已延长"),
    ;

    private int code;
    private String msg;
    WechatLoginEnum(int code, String msg){
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
