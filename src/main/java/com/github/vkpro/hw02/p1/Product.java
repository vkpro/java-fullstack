package com.github.vkpro.hw02.p1;

import lombok.Getter;
import lombok.Setter;

public class Product {
    // Lombok annotations to generate setter and getter methods
    @Setter
    @Getter
    private String name;
    @Getter
    private final int quantity = 1;
    @Setter
    private double price;
}
