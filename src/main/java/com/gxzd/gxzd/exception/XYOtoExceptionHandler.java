package com.gxzd.gxzd.exception;



import com.gxzd.gxzd.enums.CommonEnum;
import com.gxzd.gxzd.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Administrator
 * //全局异常处理类（控制）
 */
@Slf4j
@RestControllerAdvice
 public class XYOtoExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(XYOtoExceptionHandler.class);

    /**
     * 请求参数反序列化失败
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result HttpMessageNotReadableException(HttpMessageNotReadableException e){
        return Result.error(CommonEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 解决单个参数时，前端不传参，报400的异常
     * 这里是解决 数值类型 传递 空字符串的情况 ""
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result MissingServletRequestParameterException(MissingServletRequestParameterException e){
        return Result.error(CommonEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 处理请求参数不符合要求，被validate检测到所抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result error(BindException e){
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * valid 参数校验拦截返回
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        //从异常中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getFieldErrors().get(0);
        logger.error("Code:400 参数非法:{}", objectError.getDefaultMessage());
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(XYOtoException.class)
    public Result<String> APIExceptionHandler(XYOtoException e){
        //此处传递的响应码枚举
        log.error("Code:{} - ApiException:{}", e.getResult().getCode(), e.getResult().getMsg());
        return e.getResult();
    }

}