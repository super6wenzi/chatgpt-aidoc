package cn.weegoo.core.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelFieldProperty {

    String value() default "";

    String service() default "";

    String wrapper() default "";
}
