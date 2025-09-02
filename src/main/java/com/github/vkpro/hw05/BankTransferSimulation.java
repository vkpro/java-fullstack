package com.github.vkpro.hw05;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankTransferSimulation {
    public static void main(String[] args) throws InterruptedException {
        // Instantiate bank
        Bank bank = new Bank(200, 0L, 1_000L);
        BigInteger initialTotal = bank.getSumOfAllAccounts();
        System.out.println("Initial total: " + initialTotal);

        Random random = new Random();
        int numberOfThreads = 1_000;

        // Create virtual thread executor
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // Submit 1000 transfer tasks
            for (int i = 0; i < numberOfThreads; i++) {
                executor.submit(() -> {
                    int from = bank.pickRandomAccountId();
                    int to = bank.pickRandomAccountId();

                    // Ensure we're transferring between different accounts
                    while (from == to) {
                        to = bank.pickRandomAccountId();
                    }

                    // Get random amount less than "from" account balance
                    long currentBalance = bank.getAccountBalance(from);
                    long amount = currentBalance > 0 ? random.nextLong(1, currentBalance + 1) : 0;

                    if (amount > 0) {
                        // Use transfer method instead of separate operations
                        bank.transfer(from, to, amount);
                    }
                });
            }

            // Wait for all threads to finish
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Some threads did not complete within timeout");
            }
        }

        // Print final total and compare
        BigInteger finalTotal = bank.getSumOfAllAccounts();

        System.out.println("Final total: " + finalTotal);
        System.out.println("Comparison: Initial = " + initialTotal + ", Final = " + finalTotal);
        System.out.println("Amounts " + (initialTotal.equals(finalTotal) ? "match" : "do not match"));
    }
}
