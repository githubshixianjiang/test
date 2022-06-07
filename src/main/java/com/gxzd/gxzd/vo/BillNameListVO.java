package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@ApiModel("账单名称列表")
@Data
public class BillNameListVO {

    @ApiModelProperty("账单id")
    private Integer id;

    @ApiModelProperty("账单名称")
    private String billName;

}
