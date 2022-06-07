package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@ApiModel("我的账单名称")
@Data
public class MyBillNameVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("账单名称")
    private String billName;

}
