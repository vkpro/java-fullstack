package com.github.vkpro.hw05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BankTest {

    private static final long MIN_BALANCE = 1000L;
    private static final long MAX_BALANCE = 10000L;
    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank(10, MIN_BALANCE, MAX_BALANCE);
    }

    @Test
    void testConstructor_ValidParameters_CreatesBankSuccessfully() {
        assertNotNull(bank);
        assertEquals(10, bank.getNumberOfAccounts());
    }

    @Test
    void testConstructor_InvalidNumberOfAccounts_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bank(0, MIN_BALANCE, MAX_BALANCE));
        assertThrows(IllegalArgumentException.class,
                () -> new Bank(-1, MIN_BALANCE, MAX_BALANCE));
    }

    @Test
    void testConstructor_InvalidBalanceRange_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bank(5, MAX_BALANCE, MIN_BALANCE));
    }

    @Test
    void testAccountBalances_WithinSpecifiedRange() {
        for (int i = 0; i < bank.getNumberOfAccounts(); i++) {
            long balance = bank.getAccountBalance(i);
            assertTrue(balance >= MIN_BALANCE && balance <= MAX_BALANCE,
                    "Account " + i + " balance " + balance + " is outside range [" + MIN_BALANCE + ", " + MAX_BALANCE + "]");
        }
    }

    @Test
    void testGetAccountBalance_ValidAccount_ReturnsBalance() {
        long balance = bank.getAccountBalance(0);
        assertTrue(balance >= MIN_BALANCE && balance <= MAX_BALANCE);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 10, 100})
    void testGetAccountBalance_InvalidAccount_ThrowsException(int invalidAccountId) {
        assumeTrue(invalidAccountId < 0 || invalidAccountId >= bank.getNumberOfAccounts());
        assertThrows(IllegalArgumentException.class, () -> bank.getAccountBalance(invalidAccountId));
    }

    @Test
    void testTransfer_ValidTransfer_CompletesSuccessfully() {
        long fromBalance = bank.getAccountBalance(0);
        long toBalance = bank.getAccountBalance(1);
        long transferAmount = 500L;

        bank.transfer(0, 1, transferAmount);

        assertEquals(fromBalance - transferAmount, bank.getAccountBalance(0));
        assertEquals(toBalance + transferAmount, bank.getAccountBalance(1));
    }

    @Test
    void testTransfer_SameAccount_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(0, 0, 100L));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 1, 100",
            "0, -1, 100",
            "0, 1, -100",
            "0, 10, 100",
            "10, 1, 100"
    })
    void testTransfer_InvalidParameters_ThrowsException(int from, int to, long amount) {
        assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(from, to, amount));
    }

    @Test
    void testGetTotalBalance_ReturnsCorrectSum() {
        long expectedTotal = 0L;
        for (int i = 0; i < bank.getNumberOfAccounts(); i++) {
            expectedTotal += bank.getAccountBalance(i);
        }

        BigInteger actualTotal = bank.getSumOfAllAccounts();
        assertEquals(BigInteger.valueOf(expectedTotal), actualTotal);
    }

    @Test
    void testBankWithSingleAccount() {
        Bank singleAccountBank = new Bank(1, 5000L, 5000L);
        assertEquals(1, singleAccountBank.getNumberOfAccounts());
        assertEquals(5000L, singleAccountBank.getAccountBalance(0));
        assertEquals(BigInteger.valueOf(5000), singleAccountBank.getSumOfAllAccounts());
    }
}
