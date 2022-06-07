package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel("提醒VO")
@Data
public class RemindVO {

    @ApiModelProperty("提醒id")
    private Integer id;

    @ApiModelProperty("提醒时间")
    public Date remindTime;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "打开状态")
    private Integer status;


}
