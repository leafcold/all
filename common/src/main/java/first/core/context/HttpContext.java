package first.core.context;/*
 *创建者: zsq
 *创建时间:2020/4/16 23:35
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpContext {
    String value();
}
