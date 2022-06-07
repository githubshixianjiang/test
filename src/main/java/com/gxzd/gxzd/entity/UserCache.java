package com.gxzd.gxzd.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
@ApiModel("存储到redis中的用户信息")
public class UserCache {

    @ApiModelProperty("用户自增id")
    private Integer id;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("用户头像")
    private String path;

    @ApiModelProperty("用户openid")
    private String openid;

    @ApiModelProperty(value = "用户状态（1正常 2禁用）")
    private Integer status;

}
