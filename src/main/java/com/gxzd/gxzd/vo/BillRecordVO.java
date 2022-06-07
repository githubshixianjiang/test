package com.gxzd.gxzd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel("账单记录VO")
@Data
public class BillRecordVO {

    @ApiModelProperty("type")
    private Integer type;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("金额")
    private Integer price;

    @ApiModelProperty("状态（1收入 2支出）")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "MM-dd", timezone = "GMT+8")
    private Date createTime;

}
