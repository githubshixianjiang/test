package com.gxzd.gxzd.utils;

public enum ResultCode {
    OK_SUCCESS(true,200,"操作成功"),
    OPERATION_FAIL(false,400, "操作失败"),
    ERROR(false,500,"系统内部错误");

    private int code;
    private String message;
    boolean success;

    ResultCode(boolean success,int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public boolean success(){
        return this.success;
    }

}
