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

/*
- Scans a given package for .class files and loads them.
- Finds methods annotated with @Test.
- Runs each test method
- Report Results
 */
public class CustomTestRunner {

    public static void main(String[] args) {
        String packageName = "com.github.vkpro.hw02.p4";
        runTestsInPackage(packageName);
    }

    /**
     * Runs all test methods in all classes found in the specified package.
     */
    public static void runTestsInPackage(String packageName) {
        Set<Class<?>> classes;
        try {
            classes = findClassesInPackage(packageName);
        } catch (Exception e) {
            System.err.println("Failed to load classes from package: " + e);
            return;
        }

        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;
        long totalExecutionTimeMs = 0;

        System.out.printf("""
                \n=== Custom Test Runner Results ===
                Package: %s
                Classes scanned: %d
                Test Results:
                %n""", packageName, classes.size());

        for (Class<?> clazz : classes) {
            var results = runTestsInClass(clazz);
            totalTests += results.testsRun();
            passedTests += results.testsPassed();
            failedTests += results.testsFailed();
            totalExecutionTimeMs += results.executionTimeMs();
        }

        System.out.printf("""
                                
                        Summary:
                        Total tests: %d
                        Passed: %d
                        Failed: %d
                        Total execution time: %dms
                        Success rate: %.1f%%
                        """, totalTests, passedTests, failedTests, totalExecutionTimeMs,
                totalTests == 0 ? 0.0 : (passedTests * 100.0) / totalTests);
    }

    /**
     * Finds all classes in the given package.
     */
    private static Set<Class<?>> findClassesInPackage(String packageName)
            throws IOException, ClassNotFoundException, URISyntaxException {

        // Converts a package name to a path and uses the class loader to locate its directory.
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            System.err.println("No resource found for package: " + packageName);
            return classes;
        }
        Path directory = Paths.get(resource.toURI());

        // Converts each .class file to a full class name and add them into the classes set
        // e.g. `com.github.vkpro.hw02.p4.ExampleTest`
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.class")) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }

    /**
     * Runs test methods in a class and collect results.
     */
    private static TestResults runTestsInClass(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        int passed = 0;
        int failed = 0;
        long classExecutionTime = 0;
        int testCount = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                testCount++;
                var result = runSingleTest(clazz, method);
                if (result.passed()) {
                    passed++;
                } else {
                    failed++;
                }
                classExecutionTime += result.executionTimeMs();
            }
        }

        return new TestResults(testCount, passed, failed, classExecutionTime);
    }



    /**
     * Run a single test method and print detailed result.
     */
    private static SingleTestResult runSingleTest(Class<?> clazz, Method method) {
        Object instance;
        try {
            var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (Exception e) {
            System.out.printf("✗ %s.%s (0ms) - Failed to instantiate test class: %s%n",
                    clazz.getSimpleName(), method.getName(), e.getMessage());
            return new SingleTestResult(false, 0);
        }

        long startTime = System.currentTimeMillis();
        try {
            method.setAccessible(true);
            method.invoke(instance);
            long durationMs = (System.currentTimeMillis() - startTime);
            System.out.printf("✓ %s.%s (%dms)%n", clazz.getSimpleName(), method.getName(), durationMs);
            return new SingleTestResult(true, durationMs);
        } catch (Throwable t) {
            long durationMs = (System.currentTimeMillis() - startTime);
            Throwable cause = t.getCause() != null ? t.getCause() : t;
            System.out.printf("✗ %s.%s (%dms) - %s%n", clazz.getSimpleName(), method.getName(), durationMs, cause);
            return new SingleTestResult(false, durationMs);
        }
    }

    /**
     * The result of a single test method run.
     */
    private record SingleTestResult(boolean passed, long executionTimeMs) {
    }

    /**
     * Test run results for a class.
     */
    private record TestResults(int testsRun, int testsPassed, int testsFailed, long executionTimeMs) {
    }
}
