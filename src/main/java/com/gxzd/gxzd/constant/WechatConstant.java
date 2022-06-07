package com.gxzd.gxzd.constant;

import io.swagger.annotations.ApiModel;

/**
 * @author coderFu
 * @create 2021/10/2
 */
@ApiModel("微信常量类")
public interface WechatConstant {

    /**
     * 小程序唯一码
     */
    String APP_ID = "wxad68fc78199bf34a";

    /**
     * 小程序密钥
     */
    String SECRET = "693fd04d395899acf25b502bc9cc9b93";

    /**
     *微信认证统一返回结果状态 0:成功状态、 40029:code无效
     */
    int SUCCESS_RESULT_STATUS = 0;

    int EXPIRE_RESULT_STATUS = 40029;

    /**
     * 用户凭证常量
     */
    String TOKEN = "token";




}
