package com.gxzd.gxzd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel(value = "修改账单记录DTO")
@Data
public class UpdateBillRecordDTO {

    @ApiModelProperty(value = "账单记录id")
    @NotNull(message = "账单记录id不能为空")
    @Min(value = 0, message = "账单记录id参数异常")
    private Integer id;

    @ApiModelProperty(value = "账单id")
    @NotNull(message = "账单id不能为空")
    @Min(value = 0, message = "账单id参数异常")
    private Integer billNameId;

    @ApiModelProperty(value = "账单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank(message = "账单时间不能为空")
    private String billTime;

    @ApiModelProperty(value = "账单金额（1元 = 100）")
    @NotNull(message = "账单金额不能为空")
    @Min(value = 0, message = "金额参数异常")
    private Integer price;

    @ApiModelProperty(value = "账单状态（1收入 2支出）")
    @NotNull(message = "账单状态不能为空")
    @Min(value = 1, message = "账单状态参数异常")
    @Max(value = 2, message = "账单状态参数异常")
    private Integer status;

    @ApiModelProperty(value = "账单分类（1、购物 2、医疗 3、教育 4、水电费 5、餐饮 6、话费 7、 交通 8、运动 9、宠物 10、娱乐）")
    @NotNull(message = "账单分类不能为空")
    @Min(value = 1, message = "账单分类参数异常")
    @Max(value = 10, message = "账单分类参数异常")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String content;

}
