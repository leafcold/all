package first.core.context;/*
 *创建者: zsq
 *创建时间:2020/3/21 21:34
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionDoName {
    short value();
}
