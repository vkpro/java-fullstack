package com.github.vkpro.hw02.p1;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtils {
    @SneakyThrows
    // disables the compiler's requirement to catch or declare
    // checked exceptions
    public static List<String> readAllLines(String filePath) {
        return Files.readAllLines(Path.of(filePath));
    }
}