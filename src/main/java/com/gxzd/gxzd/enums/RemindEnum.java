package com.gxzd.gxzd.enums;

public enum RemindEnum implements Code {

    SAVE_REMIND_ERROR(2100,"新增提醒失败"),
    REMOVE_REMIND_ERROR(2101,"删除提醒失败"),
    THIS_REMIND_IS_NULL(2102, "此提醒不存在"),
    UPDATE_REMIND_ERROR(2103,"修改提醒失败");
    ;



    private int code;
    private String msg;
    RemindEnum(int code, String msg){
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
