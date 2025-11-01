package com.github.vkpro.hw07.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDeleteMapping {
    String value();
}