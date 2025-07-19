package com.github.vkpro.hw02.p1;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CarTest {
    @Test
    void testRequiredArgsConstructorAndFields() throws Exception {
        Car car = new Car("Toyota", "Corolla");

        Field brandField = Car.class.getDeclaredField("brand");
        brandField.setAccessible(true);
        assertEquals("Toyota", brandField.get(car));

        Field modelField = Car.class.getDeclaredField("model");
        modelField.setAccessible(true);
        assertEquals("Corolla", modelField.get(car));

        Field engineField = Car.class.getDeclaredField("engine");
        engineField.setAccessible(true);
        assertNull(engineField.get(car)); // engine should be null by default

        Field ageField = Car.class.getDeclaredField("age");
        ageField.setAccessible(true);
        assertEquals(0, ageField.get(car));
    }
}