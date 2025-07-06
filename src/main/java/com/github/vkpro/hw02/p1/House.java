package com.github.vkpro.hw02.p1;

import lombok.Builder;

@Builder
// Generates a builder pattern for this class
public class House {
    private String address;
    private String rooms;
    private String area;
    private String price;
}
