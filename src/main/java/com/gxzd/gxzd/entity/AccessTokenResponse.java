package com.gxzd.gxzd.entity;

import lombok.Data;

/**
 * 请求获取accesstoken的响应体
 */
@Data
public class AccessTokenResponse {
    // 获取到的凭证
    private String access_token;

    // 凭证有效时间，单位：秒。目前是7200秒之内的值。
    private Integer expires_in;

    // 错误码
    private Integer errcode;

    // 错误信息
    private String errmsg;
}
