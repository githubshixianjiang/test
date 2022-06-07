package com.gxzd.gxzd.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Administrator
 */
@ApiModel(value = "微信登录实体")
@Data
public class WXLoginDTO {

    @ApiModelProperty("登录唯一code")
    @NotEmpty(message = "唯一code值不能为空")
    private String code;

    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("用户头像")
    @NotEmpty(message = "用户头像不能为空")
    private String path;
    
}
