package com.gxzd.gxzd.enums;

/**
 * @author coderFu
 * @create 2021/10/1
 *通用返回枚举
 */

public enum RedisCommonEnum implements Code {


    KEY_ADD_FAIL(4000,"key添加失败"),
    INCREASE_FACTOR_NOT_VALID(4001,"递增因子不合法")
    ;




   private int code;
   private String msg;
    RedisCommonEnum(int code, String msg){
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
