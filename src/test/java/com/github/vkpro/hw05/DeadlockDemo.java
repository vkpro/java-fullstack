package com.github.vkpro.hw05;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeadlockDemo {

    public static void main(String[] args) {
//        deadlock1_CircularWait();
        deadlock2_NestedSynchronization();
//        deadlock3_ReadWriteLockDeadlock();
    }

    /**
     * Circular Wait
     * Two threads acquire locks in different orders
     */
    public static void deadlock1_CircularWait() {
        System.out.println("=== DEADLOCK 1: Circular Wait ===");

        final Object lock1 = new Object();
        final Object lock2 = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: Acquired lock1");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                System.out.println("Thread 1: Waiting for lock2...");
                synchronized (lock2) {
                    System.out.println("Thread 1: Acquired lock2 (this will never print)");
                }
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: Acquired lock2");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                System.out.println("Thread 2: Waiting for lock1...");
                synchronized (lock1) {
                    System.out.println("Thread 2: Acquired lock1 (this will never print)");
                }
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
            System.out.println("DEADLOCK OCCURRED! To avoid deadlock always acquire locks in the same order");
        } catch (InterruptedException e) {
        }
    }

    /**
     * Nested Synchronization with Method Calls
     * Two objects call each other's synchronized methods
     */
    public static void deadlock2_NestedSynchronization() {
        System.out.println("=== DEADLOCK 2: Nested Synchronization ===");

        class Resource {
            private String name;

            public Resource(String name) {
                this.name = name;
            }

            // When executing methodA, you need exclusive access to both resources
            public synchronized void methodA(Resource other) {
                System.out.println(name + " executing methodA...");

                // Small delay to increase chance of deadlock
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                // This tries to get a lock on the other resource while holding your own lock
                other.methodB(this);
            }

            public synchronized void methodB(Resource caller) {
                System.out.println(name + " executing methodB for " + caller.name);
            }
        }

        Resource resource1 = new Resource("Resource-1");
        Resource resource2 = new Resource("Resource-2");

        // Thread 1: Resource1 calls methodA on Resource2
        new Thread(() -> resource1.methodA(resource2)).start();

        // Thread 2: Resource2 calls methodA on Resource1
        new Thread(() -> resource2.methodA(resource1)).start();

        try {
            Thread.sleep(2000);
            System.out.println("DEADLOCK OCCURRED! Avoid calling other synchronized methods while holding locks");
        } catch (InterruptedException e) {
        }
    }

    /**
     * ReadWriteLock Deadlock
     * Thread holds read lock and tries to upgrade to write lock
     */
    public static void deadlock3_ReadWriteLockDeadlock() {
        System.out.println("=== DEADLOCK 3: ReadWriteLock Upgrade Deadlock ===");

        final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        Thread thread1 = new Thread(() -> {
            rwLock.readLock().lock();
            System.out.println("Thread 1: Acquired read lock");

            try {
                Thread.sleep(100);

                System.out.println("Thread 1: Trying to upgrade to write lock...");
                rwLock.writeLock().lock(); // This will block forever
                System.out.println("Thread 1: Got write lock (never prints)");
                rwLock.writeLock().unlock();

            } catch (InterruptedException e) {
            } finally {
                rwLock.readLock().unlock();
            }
        }, "Reader-Writer-1");

        Thread thread2 = new Thread(() -> {
            rwLock.readLock().lock();
            System.out.println("Thread 2: Acquired read lock");

            try {
                Thread.sleep(100);

                System.out.println("Thread 2: Trying to upgrade to write lock...");
                rwLock.writeLock().lock(); // This will also block forever
                System.out.println("Thread 2: Got write lock (never prints)");
                rwLock.writeLock().unlock();

            } catch (InterruptedException e) {
            } finally {
                rwLock.readLock().unlock();
            }
        }, "Reader-Writer-2");

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
            System.out.println("DEADLOCK OCCURRED! To avoid deadlock release read lock before acquiring write lock");
        } catch (InterruptedException e) {
        }
    }
}