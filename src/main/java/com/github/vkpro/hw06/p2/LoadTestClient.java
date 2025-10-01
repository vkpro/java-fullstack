package com.github.vkpro.hw06.p2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple load test client for CustomWebServer
 * Measures response times and throughput
 */
public class LoadTestClient {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        System.out.println("Load Test - Response Times & Throughput");
        System.out.println();

        // Test both servers
        TestResult platformResult = testServer("Platform Threads", 8080);
        TestResult virtualResult = testServer("Virtual Threads", 8081);

        // Compare performance
        System.out.println("PERFORMANCE COMPARISON");
        System.out.println("=========================");
        printComparison(platformResult, virtualResult);
    }

    static TestResult testServer(String name, int port) throws Exception {
        System.out.println("Testing " + name + "...");

        int totalRequests = 20000;
        int concurrentThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
        CountDownLatch latch = new CountDownLatch(concurrentThreads);

        AtomicLong totalResponseTime = new AtomicLong(0);
        int requestsPerThread = totalRequests / concurrentThreads;

        long testStart = System.currentTimeMillis();

        for (int i = 0; i < concurrentThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        long responseTime = sendRequest(port, "/api/time");
                        totalResponseTime.addAndGet(responseTime);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long totalTestTime = System.currentTimeMillis() - testStart;
        executor.shutdown();

        // Calculate metrics
        double throughput = (double) totalRequests / (totalTestTime / 1000.0);
        double avgResponseTime = (double) totalResponseTime.get() / totalRequests / 1_000_000.0; // Convert ns to ms

        TestResult result = new TestResult(name, throughput, avgResponseTime, totalTestTime);

        System.out.printf("  Throughput: %.1f req/sec%n", result.throughput);
        System.out.printf("  Avg Response Time: %.2f ms%n", result.avgResponseTime);
        System.out.printf("  Total Test Time: %d ms%n%n", result.totalTime);

        return result;
    }

    static long sendRequest(int port, String path) {
        long start = System.nanoTime();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + path))
                    .timeout(Duration.ofSeconds(5))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // Ignore errors for simplicity
        }
        return System.nanoTime() - start;
    }

    static void printComparison(TestResult platform, TestResult virtual) {
        System.out.printf("%-20s %-15s %-20s%n", "Metric", "Platform", "Virtual");
        System.out.println("-------------------------------------------------------");

        // Throughput comparison
        System.out.printf("%-20s %-15.1f %-20.1f",
                "Throughput (req/s)", platform.throughput, virtual.throughput);
        if (virtual.throughput > platform.throughput) {
            double times = virtual.throughput / platform.throughput;
            System.out.printf(" (%.2fx faster)", times);
        } else if (platform.throughput > virtual.throughput) {
            double times = platform.throughput / virtual.throughput;
            System.out.printf(" (%.2fx slower)", times);
        }
        System.out.println();

        // Response time comparison (lower is better)
        System.out.printf("%-20s %-15.2f %-20.2f",
                "Response Time (ms)", platform.avgResponseTime, virtual.avgResponseTime);
        if (virtual.avgResponseTime < platform.avgResponseTime) {
            double times = platform.avgResponseTime / virtual.avgResponseTime;
            System.out.printf(" (%.2fx faster)", times);
        } else if (platform.avgResponseTime < virtual.avgResponseTime) {
            double times = virtual.avgResponseTime / platform.avgResponseTime;
            System.out.printf(" (%.2fx slower)", times);
        }
        System.out.println();

        System.out.println();
    }

    static class TestResult {
        final String name;
        final double throughput;        // requests per second
        final double avgResponseTime;   // milliseconds
        final long totalTime;          // milliseconds

        TestResult(String name, double throughput, double avgResponseTime, long totalTime) {
            this.name = name;
            this.throughput = throughput;
            this.avgResponseTime = avgResponseTime;
            this.totalTime = totalTime;
        }
    }
}