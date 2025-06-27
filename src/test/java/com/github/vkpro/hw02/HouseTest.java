package com.github.vkpro.hw02;

import com.github.vkpro.hw02.p1.House;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HouseTest {

    @Test
    void builder() throws NoSuchFieldException, IllegalAccessException {
        var house = House.builder()
                .address("123 Main St")
                .rooms("4")
                .area("120 m2")
                .price("250000")
                .build();

        Field addressField = House.class.getDeclaredField("address");
        addressField.setAccessible(true);
        assertEquals("123 Main St", addressField.get(house));

        Field roomsField = House.class.getDeclaredField("rooms");
        roomsField.setAccessible(true);
        assertEquals("4", roomsField.get(house));

        Field areaField = House.class.getDeclaredField("area");
        areaField.setAccessible(true);
        assertEquals("120 m2", areaField.get(house));

        Field priceField = House.class.getDeclaredField("price");
        priceField.setAccessible(true);
        assertEquals("250000", priceField.get(house));
    }
}