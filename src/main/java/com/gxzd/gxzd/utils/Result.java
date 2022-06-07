package com.gxzd.gxzd.utils;



import com.gxzd.gxzd.enums.Code;
import com.gxzd.gxzd.enums.CommonEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author Administrator
 */
@Data
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String msg;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(Code code, T data){
        this.code = code.code();
        this.msg = code.msg();
        this.data = data;
    }

    /**
     * 成功情况调用返回
     * @return
     */
    public static Result<Object> success(){
        return new Result<>(CommonEnum.SUCCESS);
    }

    /**
     * 调用失败情况
     * @return
     */
    public static Result<Object> error(){
        return new Result<>(CommonEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 调用失败情况
     * @return
     */
    public static Result<Object> error(Object data){
        return new Result<>(CommonEnum.REQUEST_PARAM_ERROR,data);
    }

    /**
     * 有数据的时候调用返回
     * @param data
     * @return
     */
    public static Result<Object> success(Object data){
        return new Result<>(CommonEnum.SUCCESS,data);
    }

    public Result(Code code){
        this.code = code.code();
        this.msg = code.msg();
        this.data = null;
    }

}
