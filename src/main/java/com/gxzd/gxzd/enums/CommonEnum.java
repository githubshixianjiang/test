package com.gxzd.gxzd.enums;

/**
 * @author coderFu
 * @create 2021/10/1
 *通用返回枚举
 */

public enum CommonEnum implements Code {


    SUCCESS(200,"操作成功"),
    ERROR(500,"系统响应错误"),
    REFUSE(501,"拒绝访问"),
    ILLEGAL_REQUEST(503,"非法请求，拒绝服务"),
    REQUEST_PARAM_ERROR(402,"请求参数错误"),
    UNEXPECTED_KEY(406,"无效的key"),
    CONTENT_NOT_VALIDATE(4100,"文本不合法"),
    FILE_NOT_FOUND(4101,"文件不存在")
    ;




   private int code;
   private String msg;
    CommonEnum(int code, String msg){
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
