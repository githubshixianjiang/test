package com.gxzd.gxzd.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@ApiModel(value = "新增共享账本邀请实体")
@Data
public class SaveSharingBillDTO {

    @ApiModelProperty(value = "账单id")
    @NotNull(message = "账单id不能为空")
    @Min(value = 0, message = "账单id参数异常")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

}
