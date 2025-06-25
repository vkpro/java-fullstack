# List Benchmark Results

```shell
## Insertion Performance (1,000,000 items)
ArrayList: Duration = 47.80 ms, Memory Used = 30.90 MB
LinkedList: Duration = 89.53 ms, Memory Used = 41.29 MB
CustomList: Duration = 30.68 ms, Memory Used = 29.06 MB

## Add/Remove Cycle (100,000 cycles)
ArrayList: Duration = 281.25 ms, Memory Used = 2.88 MB
LinkedList: Duration = 6.41 ms, Memory Used = 3.91 MB
CustomList: Duration = 283.30 ms, Memory Used = 2.82 MB
```

## Conclusion

- CustomList is fastest for insertions.
- LinkedList is best for add/remove at the start.
- ArrayList and CustomList are similar for add/remove, but slower than LinkedList.