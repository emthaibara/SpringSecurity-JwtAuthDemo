package com.securityserviceprovider.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/18
 */


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidator.class})
public @interface IsMobile {
    String message() default "手机号码格式不正确";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
