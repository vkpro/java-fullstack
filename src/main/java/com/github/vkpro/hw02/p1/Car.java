package com.github.vkpro.hw02.p1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//Generates a constructor with required arguments.
public class Car {
    private final String brand;
    private final String model;
    private String engine;
    private final int age = 0;
}
