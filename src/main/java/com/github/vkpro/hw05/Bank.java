package com.github.vkpro.hw05;

import lombok.Getter;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private final long[] accountBalances;
    private final ReentrantLock[] accountLocks;
    @Getter
    private final int numberOfAccounts;

    public Bank(int numberOfAccounts, long minBalance, long maxBalance) {
        // Validate input parameters
        validateConstructorParams(numberOfAccounts, minBalance, maxBalance);

        this.numberOfAccounts = numberOfAccounts;
        this.accountBalances = new long[numberOfAccounts];
        this.accountLocks = new ReentrantLock[numberOfAccounts];

        // Initialize locks for each account
        for (int i = 0; i < numberOfAccounts; i++) {
            accountLocks[i] = new ReentrantLock();
        }

        initializeAccountsWithRandomBalances(minBalance, maxBalance);
    }

    private void validateConstructorParams(int numberOfAccounts, long minBalance, long maxBalance) {
        // Check if number of accounts is valid
        if (numberOfAccounts <= 0) {
            throw new IllegalArgumentException("Number of accounts must be positive");
        }

        // Check if balance range is valid
        if (minBalance > maxBalance) {
            throw new IllegalArgumentException("Min balance cannot be greater than max balance");
        }
    }

    /**
     * Sets up all accounts with random balances in the specified range.
     */
    private void initializeAccountsWithRandomBalances(long minBalance, long maxBalance) {
        var random = ThreadLocalRandom.current();

        // Fill each account with random balance
        for (int i = 0; i < numberOfAccounts; i++) {
            setAccountBalance(i,random.nextLong(minBalance, maxBalance + 1));
        }
    }

    /**
     * Picks a random account ID from all available accounts.
     */
    public int pickRandomAccountId() {
        var random = ThreadLocalRandom.current();
        return random.nextInt(numberOfAccounts);
    }

    public long getAccountBalance(int accountId) {
        validateAccountId(accountId);
        return accountBalances[accountId];
    }

    public  void setAccountBalance(int accountId, long newBalance) {
            validateAccountId(accountId);
            accountBalances[accountId] = newBalance;
    }

    public BigInteger getSumOfAllAccounts() {
        var totalSum = BigInteger.ZERO;

        for (long balance : accountBalances) {
            totalSum = totalSum.add(BigInteger.valueOf(balance));
        }

        return totalSum;
    }

    public void transfer(int fromAccountId, int toAccountId, long amount) {
        validateAccountId(fromAccountId);
        validateAccountId(toAccountId);

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        // Lock in consistent order to prevent deadlock
        int firstLock = Math.min(fromAccountId, toAccountId);
        int secondLock = Math.max(fromAccountId, toAccountId);

        accountLocks[firstLock].lock();
        try {
            accountLocks[secondLock].lock();
            try {
                if (accountBalances[fromAccountId] < amount) {
                    return; // Insufficient funds
                }

                // Perform the transfer
                accountBalances[fromAccountId] -= amount;
                accountBalances[toAccountId] += amount;
            } finally {
                accountLocks[secondLock].unlock();
            }
        } finally {
            accountLocks[firstLock].unlock();
        }
    }

    private void validateAccountId(int accountId) {
        if (accountId < 0 || accountId >= numberOfAccounts) {
            throw new IllegalArgumentException(
                    "Account ID must be between 0 and " + (numberOfAccounts - 1)
            );
        }
    }
}