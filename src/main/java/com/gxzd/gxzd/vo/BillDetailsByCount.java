package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@ApiModel("统计账单详情")
@Data
public class BillDetailsByCount {

    @ApiModelProperty("账单名称")
    private String billName;

    @ApiModelProperty("支出")
    private Integer expenditure;

    @ApiModelProperty("收入")
    private Integer income;

}
