package com.github.vkpro.hw02.p1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProductTest {
    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void getName() {
        product.setName("milk");
        assertEquals("milk", product.getName());
        assertNotEquals("bread", product.getName());
    }

    @Test
    void getQuantity() {
        assertEquals(1, product.getQuantity());
        assertNotEquals(0, product.getQuantity());
        assertNotEquals(2, product.getQuantity());
    }
    @Test
    void getPrice() throws NoSuchFieldException, IllegalAccessException {
        product.setPrice(2.99);
        var priceField = Product.class.getDeclaredField("price");
        priceField.setAccessible(true);
        assertEquals(2.99, priceField.get(product));
    }
}