package com.gxzd.gxzd.enums;

/**
 * @author Administrator
 */

public enum BillEnum implements Code{

    SAVE_BILL_RECORD_ERROR(2000,"新增账单记录失败"),
    UPDATE_BILL_RECORD_ERROR(2001,"修改账单记录失败"),
    BILL_RECORD_IS_NOLL(2002,"账单记录不存在"),
    REMOVE_BILL_RECORD_ERROR(2003,"删除账单失败"),


    ;


    private int code;
    private String msg;
    BillEnum(int code, String msg){
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
