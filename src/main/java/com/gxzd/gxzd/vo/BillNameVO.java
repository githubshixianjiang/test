package com.gxzd.gxzd.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel("账单VO")
@Data
public class BillNameVO {

    @ApiModelProperty("账单id")
    private Integer id;

    @ApiModelProperty(value = "账单名称")
    private String billName;

    @ApiModelProperty(value = "描述")
    private String content;

    @ApiModelProperty(value = "共享状态（1共享账单 2不共享账单）")
    private Integer sharing;

    @ApiModelProperty(value = "总收入(1元 = 100)")
    private Integer income;

    @ApiModelProperty(value = "总支出")
    private Integer expenditure;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
