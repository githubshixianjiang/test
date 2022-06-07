package com.gxzd.gxzd.exception;




import com.gxzd.gxzd.utils.Result;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * 自定义异常类
 */
@Data
@NoArgsConstructor
public class XYOtoException extends RuntimeException{

    @Getter
    private Result result;

    public XYOtoException(Result result) {
        this.result = result;
    }

}
