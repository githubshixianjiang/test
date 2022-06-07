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
@ApiModel(value = "修改账单实体")
@Data
public class UpdateBillNameDTO {

    @ApiModelProperty(value = "账单id")
    @NotNull(message = "账单id不能为空")
    @Min(value = 0, message = "账单id参数异常")
    private Integer id;

    @ApiModelProperty(value = "账单名称")
    @NotBlank(message = "账单名称不能为空")
    private String billName;

    @ApiModelProperty(value = "描述")
    private String content;

    @ApiModelProperty(value = "共享状态")
    private Integer sharing;

}
