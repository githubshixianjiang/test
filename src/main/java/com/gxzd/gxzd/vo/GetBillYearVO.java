package com.gxzd.gxzd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@ApiModel("获取年账单VO")
@Data
public class GetBillYearVO {

    @ApiModelProperty("总账单记录数")
    private Integer billNumCount;

    @ApiModelProperty("总收入")
    private Integer billCount;

    @ApiModelProperty("类型Map")
    private Map<Integer, Integer> billMap;

    @ApiModelProperty("账单列表")
    private List<BillDetailsByCount> billDetailsList;

}
