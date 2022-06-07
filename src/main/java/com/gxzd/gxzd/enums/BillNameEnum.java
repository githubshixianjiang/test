package com.gxzd.gxzd.enums;

public enum BillNameEnum implements Code{

    SAVE_BILLNAME_ERROR(1300,"新增账单失败"),
    THIS_BILL_IS_NOLL(1302,"此账单不存在"),
    UPDATE_BILL_ERROR(1303,"修改账单失败"),
    BILL_ID_IS_NOT_NULL(1304,"账单id不能为空"),
    REMOVE_BILL_ERROR(1305,"删除账单失败"),
    SAVE_BILLSHARING_ERROR(1306,"新增共享账单失败"),
    UPDATE_BILLSHARING_ERROR(1307,"修改共享账单失败"),
    SAVE_BILLSHARING_USER_ERROR(1308,"邀请失败"),
    BILLSHARING_IS_NOT_NULL(1309,"已邀请过"),
    BILLSHARING_IS_NOT_SHARING(1310,"该账单不是共享账单")


    ;


    private int code;
    private String msg;
    BillNameEnum(int code, String msg){
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
