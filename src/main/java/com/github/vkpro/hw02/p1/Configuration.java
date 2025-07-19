package com.github.vkpro.hw02.p1;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Configuration {
    private String brand;
    private int price;
}