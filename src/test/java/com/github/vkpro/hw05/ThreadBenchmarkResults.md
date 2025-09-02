# Thread Benchmark Results

```shell
=== Testing with 1 threads ===
Parallel stream sum: 100000001 (17.98 ms)
ExecutorService sum: 100000001 (18.89 ms)
Performance ratio: 0.95x (stream faster)

=== Testing with 10 threads ===
Parallel stream sum: 100000001 (6.42 ms)
ExecutorService sum: 100000001 (6.58 ms)
Performance ratio: 0.98x (stream faster)

=== Testing with 100 threads ===
Parallel stream sum: 100000001 (6.57 ms)
ExecutorService sum: 100000001 (23.80 ms)
Performance ratio: 0.28x (stream faster)

=== Testing with 1000 threads ===
Parallel stream sum: 100000001 (7.64 ms)
ExecutorService sum: 100000001 (202.34 ms)
Performance ratio: 0.04x (stream faster)
```

## Conclusion

- Low counts (1-10): Both approaches perform similarly
- Medium counts (100): Stream shows stable result, ExecutorService suffers
- High counts (1000): ExecutorService becomes unusably slow

Parallel Streams are the clear winner due to their automatic optimization and consistent performance across different thread counts.