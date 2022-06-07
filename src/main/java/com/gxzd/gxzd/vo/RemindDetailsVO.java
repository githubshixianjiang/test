package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel("提醒详情实体")
@Data
public class RemindDetailsVO {

    @ApiModelProperty("提醒id")
    private Integer id;

    @ApiModelProperty(value = "提醒名称")
    private String remind;

    @ApiModelProperty("提醒时间")
    public Date remindTime;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "打开状态")
    private Integer status;

}
