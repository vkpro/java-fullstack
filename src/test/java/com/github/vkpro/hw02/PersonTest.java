package com.github.vkpro.hw02;

import com.github.vkpro.hw02.p1.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @Test
    void getName() {
        person.setName("Alice");
        var name = person.getName();
        assertEquals("Alice", name);
    }

    @Test
    void getAge() {
        person.setAge(9);
        var age = person.getAge();
        assertEquals(9, age);
    }

    @Test
    void getEmail() {
        person.setEmail("alice@gmail.com");
        var email = person.getEmail();
        assertEquals("alice@gmail.com", email);
    }

    @Test
    void personToString() {
        person.setName("Alice");
        person.setAge(9);
        person.setEmail("alice@gmail.com");

        var personTo_String = person.toString();
        assertTrue(personTo_String.contains( "Alice"));
        assertTrue(personTo_String.contains( "9"));
        assertTrue(personTo_String.contains( "alice@gmail.com"));
    }
}