package com.gxzd.gxzd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@ApiModel("账单详情VO")
@Data
public class BillDetailsVO {

//    @ApiModelProperty("账单时间")
//    @DateTimeFormat(pattern = "yyyy-MM")
//    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
//    private Date billTime;

    @ApiModelProperty("支出")
    private Integer expenditure;

    @ApiModelProperty("收入")
    private Integer income;

    @ApiModelProperty("账单记录list")
    private List<BillRecordVO> billRecords;

}
