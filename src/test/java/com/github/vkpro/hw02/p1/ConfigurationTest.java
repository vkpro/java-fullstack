package com.github.vkpro.hw02.p1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    private Configuration config;

    @BeforeEach
    void setUp() {
        config = new Configuration();
    }

    @Test
    void setBrandAndPrice() {
        config.setBrand("Brand")
                .setPrice(99);
        assertEquals("Brand", config.getBrand());
        assertEquals(99, config.getPrice());
    }

    @Test
    void setPrice() {
        config.setPrice(1500);
        assertEquals(1500, config.getPrice());
        assertNotEquals(0, config.getPrice());
    }

    @Test
    void setBrand() {
        config.setBrand("Dell");
    }

}