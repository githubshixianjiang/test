package com.gxzd.gxzd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author Administrator
 */
@ApiModel(value = "获取年账单DTO")
@Data
public class GetBillYearDTO implements Serializable {

    @ApiModelProperty(value = "具体年份")
    @DateTimeFormat(pattern = "yyyy")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    private String applyTime;

    @ApiModelProperty(value = "账单状态（1收入 2支出）")
    private Integer status;

}
