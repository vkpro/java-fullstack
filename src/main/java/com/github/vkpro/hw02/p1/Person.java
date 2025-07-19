package com.github.vkpro.hw02.p1;

import lombok.Data;

@Data
// Generates getters for all fields, a useful toString method,
// and hashCode and equals implementations that check all non-transient fields
public class Person {
    private String name;
    private int age;
    private String email;
}
