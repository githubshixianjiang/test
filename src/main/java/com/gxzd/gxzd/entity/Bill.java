package com.gxzd.gxzd.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Bill对象", description="")
public class Bill implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "账单名称id")
    private Integer billNameId;

    @ApiModelProperty(value = "账单时间")
    private Date billTime;

    @ApiModelProperty(value = "账单金额")
    private Integer price;

    @ApiModelProperty(value = "共享状态（1共享账单 2不共享账单）")
    private Integer sharing;

    @ApiModelProperty(value = "状态（1收入 2支出）")
    private Integer status;

    @ApiModelProperty(value = "分类（1、购物 2、医疗 3、教育 4、水电费 5、餐饮 6、话费 7、 交通 8、运动 9、宠物 10、娱乐）")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifiedTime;


}
