package com.github.vkpro.hw02;

import com.github.vkpro.hw02.p1.Book;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

    @Test
    void testLombokGeneratedMethods() throws NoSuchFieldException, IllegalAccessException {
        Book book = new Book("Title", "Author", 101);

        // Use reflection to access private fields
        Field titleField = Book.class.getDeclaredField("title");
        titleField.setAccessible(true);
        assertEquals("Title", titleField.get(book));

        Field authorField = Book.class.getDeclaredField("author");
        authorField.setAccessible(true);
        assertEquals("Author", authorField.get(book));

        Field pagesField = Book.class.getDeclaredField("pages");
        pagesField.setAccessible(true);
        assertEquals(101, pagesField.get(book));

        Field priceField = Book.class.getDeclaredField("price");
        priceField.setAccessible(true);
        assertEquals(9.99, priceField.get(book));
    }
}