package com.github.vkpro.hw02.p4;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CustomTestRunner {
    public static void main(String[] args) {
        CustomTestRunner.runTestsInPackage("com.github.vkpro.hw02.p4");
    }

    public static void runTestsInPackage(String packageName) {
        Set<Class<?>> classesInPackage;
        try {
            classesInPackage = getClassesInPackage(packageName);
        } catch (Exception e) {
            System.err.println("Failed to scan package: " + e);
            return;
        }
        System.out.println("\nclassesInPackage = " + classesInPackage);
        int passed = 0;
        int failed = 0;

        for (Class<?> testClass : classesInPackage) {

            int[] classResults = new int[2];
            runAllCustomTestsInClass(testClass, classResults);
            passed += classResults[0];
            failed += classResults[1];
        }
        displayTestResults(passed, failed);
//        System.out.printf("Result: Passed: %d, Failed: %d%n", passed, failed);
    }

    private static void displayTestResults(int passed, int failed) {
        int total = passed + failed;
        System.out.println("\n========== Test Results ========");
        System.out.printf("Total tests: %d\n", total);
        System.out.printf("Passed:     %d\n", passed);
        System.out.printf("Failed:     %d\n", failed);
        System.out.println("==================================");
    }

    private static void runAllCustomTestsInClass(Class<?> testClass, int[] results) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                System.out.println(testClass);
                runSingleCustomTest(testClass, method, results);
            }
        }
    }

    private static void runSingleCustomTest(Class<?> testClass, Method method, int[] results) {
        try {
            var constructor = testClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            method.setAccessible(true);
            long start = System.nanoTime();
            method.invoke(instance);
            long end = System.nanoTime();
            long durationMs = (end - start) / 1_000_000;
            System.out.printf("    %s PASSED (%d ms)%n", method.getName(), durationMs);
            results[0]++;
        } catch (Throwable ex) {
            System.out.printf("    %s FAILED: %s%n", method.getName(), ex.getCause());
            results[1]++;
        }
    }

    private static Set<Class<?>> getClassesInPackage(String packageName) throws IOException, ClassNotFoundException, URISyntaxException {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return classes;
        }
        if (!"file".equals(resource.getProtocol())) {
            System.err.println("Unsupported resource protocol: " + resource.getProtocol());
            return classes;
        }
        Path dir = Paths.get(resource.toURI());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.class")) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                if (fileName.endsWith(".class")) {
                    String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }
}