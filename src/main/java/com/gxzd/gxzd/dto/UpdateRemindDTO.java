package com.gxzd.gxzd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@ApiModel(value = "修改提醒")
@Data
public class UpdateRemindDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    @Min(value = 0, message = "id参数异常")
    private Integer id;

    @ApiModelProperty(value = "提醒名称")
    @NotBlank(message = "提醒名称不能为空")
    private String remind;

    @ApiModelProperty(value = "提醒时间：示例：2022-05-30 22:06")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @NotBlank(message = "提醒时间不能为空")
    private String remindTime;

    @ApiModelProperty(value = "重复状态（1重复 2不重复）")
    @NotNull(message = "重复状态不能为空")
    @Min(value = 1, message = "重复状态参数异常")
    @Max(value = 2, message = "重复状态参数异常")
    private Integer around;

    @ApiModelProperty(value = "链接")
    @NotBlank(message = "链接不能为空")
    private String url;

}
