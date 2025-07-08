package com.github.vkpro.hw03;

import com.github.vkpro.hw01.CustomListTest;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class CustomLinkedListTest extends CustomListTest {
    static Stream<List<String>> listProvider() {
        return Stream.of(
                new LinkedList<>(),
                new CustomLinkedList<>()
        );
    }
}
