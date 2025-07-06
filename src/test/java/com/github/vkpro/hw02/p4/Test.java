package com.github.vkpro.hw02.p4;

import java.lang.annotation.*;

//to access via reflection during program execution
@Retention(RetentionPolicy.RUNTIME)
// it can only be applied to methods
@Target(ElementType.METHOD)
public @interface Test {
}