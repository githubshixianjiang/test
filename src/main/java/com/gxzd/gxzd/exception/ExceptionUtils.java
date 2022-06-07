package com.gxzd.gxzd.exception;


import com.gxzd.gxzd.enums.Code;
import com.gxzd.gxzd.utils.Result;

/** 异常处理工具类
 * @author Administrator
 */
public class ExceptionUtils {

    public static void error(Code code) {
        throw new XYOtoException(new Result(code));
    }
}
