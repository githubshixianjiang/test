package com.gxzd.gxzd.enums;

import io.swagger.annotations.ApiModel;

/**
 * @author coderFu
 * @create 2021/10/1
 */
@ApiModel("响应码统一封装")
public interface Code {

    /**
     * 状态码
     * @return
     */
    int code();

    /**
     * 提示信息
     * @return
     */
    String msg();




}
