package com.gxzd.gxzd.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@ApiModel(value = "新增账单实体")
@Data
public class SaveBillNameDTO {

    @ApiModelProperty(value = "账单名称")
    @NotBlank(message = "账单名称不能为空")
    private String billName;

    @ApiModelProperty(value = "描述")
    private String content;

    @ApiModelProperty(value = "共享状态（1共享账本 2个人账本）")
    @NotNull(message = "共享状态不能为空")
    @Min(value = 1, message = "状态参数异常")
    @Max(value = 2, message = "状态参数异常")
    private Integer sharing;


}
