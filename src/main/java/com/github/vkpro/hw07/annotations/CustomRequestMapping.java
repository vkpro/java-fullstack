package com.github.vkpro.hw07.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRequestMapping {
    String value();
}
