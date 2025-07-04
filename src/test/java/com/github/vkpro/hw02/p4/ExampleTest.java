package com.github.vkpro.hw02.p4;

public class ExampleTest {
    @Test
    public void testSuccess() throws InterruptedException {
        Thread.sleep(1);
        assert true;
    }

    @Test
    public void testFailure() {
        throw new RuntimeException("fail");
    }


}