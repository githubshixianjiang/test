package com.gxzd.gxzd.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

/**
 * 登录注解
 * @author Administrator
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface RequiresLogin {
}
