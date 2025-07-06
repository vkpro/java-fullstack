package com.github.vkpro.hw02.p1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
//Generates an all-args constructor
public class Book {

    private String title;
    private String author;
    private int pages = 100;
    private final double price = 9.99;
}
