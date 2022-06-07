package com.gxzd.gxzd.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信登录验证信息实体
 *
 * @author Administrator
 */
@ApiModel("微信登录验证信息实体")
@Data
public class WxInfo {

    @ApiModelProperty("小程序请求码,前端获取")
    private String code;

    @ApiModelProperty("小程序openid唯一标识，官方分配")
    private String openid;

    @ApiModelProperty("小程序密钥，官方分配")
    private String session_key;

    @ApiModelProperty("小程序开发平台唯一标识符")
    private String unionId;

    @ApiModelProperty("返回状态码")
    private Integer errcode;

    @ApiModelProperty("错误信息")
    private String errmsg;

}
