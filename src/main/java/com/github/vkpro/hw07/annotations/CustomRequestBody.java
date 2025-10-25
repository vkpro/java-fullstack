package com.github.vkpro.hw07.annotations;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRequestBody {
    boolean required() default true;
}