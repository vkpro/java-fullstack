package com.github.vkpro.hw06.p1;

import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CustomExecutorServiceTest {

    private CustomExecutorService executor;

    @AfterEach
    void cleanup() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate within timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Nested
    @DisplayName("Factory Methods Tests")
    class FactoryMethodsTests {

        @Test
        @DisplayName("Should create platform thread executor with valid core pool size")
        void testWithPlatformThreadPool() {
            executor = CustomExecutorService.withPlatformThreadPool(4);
            assertNotNull(executor);
            assertFalse(executor.isShutdown());
        }

        @Test
        @DisplayName("Should create virtual thread executor")
        void testWithVirtualThreadPerTask() {
            executor = CustomExecutorService.withVirtualThreadPerTask();
            assertNotNull(executor);
            assertFalse(executor.isShutdown());
        }

        @Test
        @DisplayName("Should throw exception for invalid core pool size")
        void testInvalidCorePoolSize() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CustomExecutorService(0, false));
            assertThrows(IllegalArgumentException.class,
                    () -> new CustomExecutorService(-1, false));
        }

        @Test
        @DisplayName("Should allow zero core pool size for virtual threads")
        void testVirtualThreadsWithZeroCorePoolSize() {
            assertDoesNotThrow(() -> new CustomExecutorService(0, true));
        }
    }

    @Nested
    @DisplayName("Platform Thread Tests")
    class PlatformThreadTests {

        @BeforeEach
        void setup() {
            executor = CustomExecutorService.withPlatformThreadPool(2);
        }

        @Test
        @DisplayName("Should execute simple runnable task")
        @Timeout(5)
        void testExecuteRunnableTask() throws InterruptedException {
            AtomicBoolean executed = new AtomicBoolean(false);

            executor.execute(() -> executed.set(true));

            // Wait for task to complete
            Thread.sleep(100);
            assertTrue(executed.get());
        }

        @Test
        @DisplayName("Should execute multiple tasks concurrently")
        @Timeout(5)
        void testMultipleTasks() throws InterruptedException {
            AtomicInteger counter = new AtomicInteger(0);
            int taskCount = 10;

            for (int i = 0; i < taskCount; i++) {
                executor.execute(() -> {
                    try {
                        Thread.sleep(50); // Simulate work
                        counter.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // Wait for all tasks to complete
            Thread.sleep(1000);
            assertEquals(taskCount, counter.get());
        }

        @Test
        @DisplayName("Should submit callable and return future")
        @Timeout(5)
        void testSubmitCallable() throws Exception {
            Future<String> future = executor.submit(() -> "Hello World");

            assertEquals("Hello World", future.get(2, TimeUnit.SECONDS));
            assertTrue(future.isDone());
        }

        @Test
        @DisplayName("Should submit runnable and return future")
        @Timeout(5)
        void testSubmitRunnable() throws Exception {
            AtomicBoolean executed = new AtomicBoolean(false);
            Future<?> future = executor.submit(() -> executed.set(true));

            assertNull(future.get(2, TimeUnit.SECONDS));
            assertTrue(future.isDone());
            assertTrue(executed.get());
        }
    }

    @Nested
    @DisplayName("Virtual Thread Tests")
    class VirtualThreadTests {

        @BeforeEach
        void setup() {
            executor = CustomExecutorService.withVirtualThreadPerTask();
        }

        @Test
        @DisplayName("Should execute task using virtual thread")
        @Timeout(5)
        void testVirtualThreadExecution() throws InterruptedException {
            AtomicBoolean executed = new AtomicBoolean(false);

            executor.execute(() -> executed.set(true));

            Thread.sleep(100);
            assertTrue(executed.get());
        }

        @Test
        @DisplayName("Should handle many virtual threads efficiently")
        @Timeout(10)
        void testManyVirtualThreads() throws InterruptedException {
            AtomicInteger counter = new AtomicInteger(0);
            int taskCount = 1000;

            for (int i = 0; i < taskCount; i++) {
                executor.execute(() -> counter.incrementAndGet());
            }

            // Wait for completion
            Thread.sleep(2000);
            assertEquals(taskCount, counter.get());
        }

        @Test
        @DisplayName("Should submit callable with virtual thread")
        @Timeout(5)
        void testVirtualThreadCallable() throws Exception {
            Future<Integer> future = executor.submit(() -> 42);

            assertEquals(Integer.valueOf(42), future.get(2, TimeUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("Shutdown Tests")
    class ShutdownTests {

        @Test
        @DisplayName("Should shutdown gracefully for platform threads")
        @Timeout(10)
        void testPlatformThreadShutdown() throws InterruptedException {
            executor = CustomExecutorService.withPlatformThreadPool(2);
            AtomicInteger completedTasks = new AtomicInteger(0);

            // Submit some tasks
            for (int i = 0; i < 5; i++) {
                executor.execute(() -> {
                    try {
                        Thread.sleep(100);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            executor.shutdown();
            assertTrue(executor.isShutdown());

            boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            assertTrue(terminated);
            assertTrue(executor.isTerminated());
            assertEquals(5, completedTasks.get());
        }

        @Test
        @DisplayName("Should shutdown gracefully for virtual threads")
        @Timeout(10)
        void testVirtualThreadShutdown() throws InterruptedException {
            executor = CustomExecutorService.withVirtualThreadPerTask();
            AtomicInteger completedTasks = new AtomicInteger(0);

            // Submit some tasks
            for (int i = 0; i < 5; i++) {
                executor.execute(() -> {
                    try {
                        Thread.sleep(100);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            executor.shutdown();
            assertTrue(executor.isShutdown());

            boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            assertTrue(terminated);
            assertTrue(executor.isTerminated());
            assertEquals(5, completedTasks.get());
        }

        @Test
        @DisplayName("Should timeout on awaitTermination")
        @Timeout(5)
        void testAwaitTerminationTimeout() throws InterruptedException {
            executor = CustomExecutorService.withPlatformThreadPool(1);

            executor.execute(() -> {
                try {
                    Thread.sleep(2000); // Long running task
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.shutdown();
            boolean terminated = executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            assertFalse(terminated);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @BeforeEach
        void setup() {
            executor = CustomExecutorService.withPlatformThreadPool(2);
        }

        @Test
        @DisplayName("Should throw NPE for null command in execute")
        void testExecuteNullCommand() {
            assertThrows(NullPointerException.class, () -> executor.execute(null));
        }

        @Test
        @DisplayName("Should throw NPE for null task in submit")
        void testSubmitNullTask() {
            assertThrows(NullPointerException.class, () -> executor.submit((Callable<String>) null));
            assertThrows(NullPointerException.class, () -> executor.submit((Runnable) null));
        }

        @Nested
        @DisplayName("State Management Tests")
        class StateManagementTests {

            @Test
            @DisplayName("Should correctly report shutdown state")
            void testShutdownState() {
                executor = CustomExecutorService.withPlatformThreadPool(2);

                assertFalse(executor.isShutdown());
                assertFalse(executor.isTerminated());

                executor.shutdown();

                assertTrue(executor.isShutdown());
            }

            @Test
            @DisplayName("Should correctly report termination state")
            @Timeout(5)
            void testTerminationState() throws InterruptedException {
                executor = CustomExecutorService.withPlatformThreadPool(1);

                executor.execute(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                executor.shutdown();
                assertFalse(executor.isTerminated()); // Should not be terminated while task is running

                executor.awaitTermination(2, TimeUnit.SECONDS);
                assertTrue(executor.isTerminated());
            }
        }

        @Nested
        @DisplayName("Concurrent Access Tests")
        class ConcurrentAccessTests {

            @Test
            @DisplayName("Should handle concurrent task submissions safely")
            @Timeout(10)
            void testConcurrentSubmissions() throws InterruptedException {
                executor = CustomExecutorService.withPlatformThreadPool(4);
                AtomicInteger completedTasks = new AtomicInteger(0);
                int numThreads = 10;
                int tasksPerThread = 50;
                CountDownLatch latch = new CountDownLatch(numThreads);

                for (int i = 0; i < numThreads; i++) {
                    new Thread(() -> {
                        try {
                            for (int j = 0; j < tasksPerThread; j++) {
                                executor.execute(() -> completedTasks.incrementAndGet());
                            }
                        } finally {
                            latch.countDown();
                        }
                    }).start();
                }

                latch.await();
                Thread.sleep(2000); // Wait for tasks to complete

                assertEquals(numThreads * tasksPerThread, completedTasks.get());
            }
        }
    }
}