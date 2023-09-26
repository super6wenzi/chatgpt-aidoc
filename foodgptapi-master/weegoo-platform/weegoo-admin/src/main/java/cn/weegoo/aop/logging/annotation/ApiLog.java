package cn.weegoo.aop.logging.annotation;

import cn.weegoo.sys.constant.enums.LogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {

    LogTypeEnum type() default LogTypeEnum.ACCESS;

    String value() default "";
}
