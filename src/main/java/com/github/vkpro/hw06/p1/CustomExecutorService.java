package com.github.vkpro.hw06.p1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

/**
 * Custom ExecutorService implementation
 * Supports both platform threads and virtual threads
 */
public class CustomExecutorService implements ExecutorService {

    private final int poolSize;
    private final boolean useVirtualThreads;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final AtomicInteger threadCounter = new AtomicInteger(0);
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final List<Thread> workerThreads = new ArrayList<>();
    private final AtomicInteger activeVirtualThreads = new AtomicInteger(0);

    public static CustomExecutorService withPlatformThreadPool(int corePoolSize) {
        return new CustomExecutorService(corePoolSize, false);
    }

    public static CustomExecutorService withVirtualThreadPerTask() {
        return new CustomExecutorService(0, true);
    }

    public CustomExecutorService(int poolSize, boolean useVirtualThreads) {
        if (!useVirtualThreads && poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be positive for platform threads");
        }
        this.poolSize = poolSize;
        this.useVirtualThreads = useVirtualThreads;

        if (!useVirtualThreads) {
            initializePlatformThreadPool();
        }
    }

    private void initializePlatformThreadPool() {
        for (int i = 0; i < poolSize; i++) {
            Thread thread = Thread.ofPlatform()
                    .name("platform-thread-" + threadCounter.incrementAndGet())
                    .start(this::pollTask);
            workerThreads.add(thread);
        }
    }

    private void pollTask() {
        while (!shutdown.get() || !taskQueue.isEmpty()) {
            try {
                Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                if (task != null) {
                    task.run();
                }
            } catch (Exception e) {
                System.err.println("Task execution failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void execute(Runnable task) {
        if (useVirtualThreads) {
            activeVirtualThreads.incrementAndGet();
            Thread.ofVirtual()
                    .name("virtual-task-" + threadCounter.incrementAndGet())
                    .start(() -> {
                        try {
                            task.run();
                        } catch (Exception e) {
                            System.err.println("Task execution failed: " + e.getMessage());
                        } finally {
                            activeVirtualThreads.decrementAndGet();
                        }
                    });
        } else {
            taskQueue.offer(task);
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public Future<?> submit(Runnable task) {
        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public void shutdown() {
        shutdown.set(true);
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);

        while (!isTerminated() && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }

        return isTerminated();
    }

    @Override
    public boolean isTerminated() {
        return shutdown.get() && taskQueue.isEmpty() &&
                (useVirtualThreads ? activeVirtualThreads.get() == 0
                        : workerThreads.stream().noneMatch(Thread::isAlive));
    }

    // Unimplemented methods - properly throw UnsupportedOperationException
    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(java.util.Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }
}
