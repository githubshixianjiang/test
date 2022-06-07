package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel("模糊查询账单VO")
@Data
public class BillLikeVO {

    @ApiModelProperty("账单id")
    private Integer id;

    @ApiModelProperty("账单名称")
    private String billName;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
