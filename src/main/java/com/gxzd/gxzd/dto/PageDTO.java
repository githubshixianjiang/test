package com.gxzd.gxzd.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Description
 * @Author: TingFeng
 * @CreateTime: 2022/4/24  16:12
 */
@Api("分页")
@Data
public class PageDTO {

    @ApiModelProperty("第一次查询第一页中第一个话题的id 默认0为查第一页数")
    @Min(value = 0, message = "返回的第一页数据必须大于等于0")
    Integer endIndexId;

    @ApiModelProperty("默认第一页")
    @Min(value = 1, message = "页数必须大于等于1")
    Integer pageNum;

    @ApiModelProperty("默认每页十条")
    @Min(value = 1, message = "每页的条数必须大于等于1")
    @Max(value = 200, message = "每页的最大条数不超过200条")
    Integer pageSize;

    public PageDTO() {
        this.endIndexId = 0;
        this.pageNum = 1;
        this.pageSize = 10;
    }

}
