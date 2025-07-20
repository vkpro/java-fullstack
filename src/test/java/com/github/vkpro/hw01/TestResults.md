# List Benchmark Results

```shell
--- Insertion Performance (1000000 items) ---
ArrayList: Duration = 127,99 ms, Memory Used = 31,91 MB
CustomList: Duration = 111,90 ms, Memory Used = 29,18 MB
LinkedList: Duration = 277,63 ms, Memory Used = 40,75 MB
CustomLinkedList: Duration = 269,15 ms, Memory Used = 42,12 MB

--- Add/Remove Cycle (100000 cycles) ---
ArrayList: Duration = 1360,74 ms, Memory Used = 3,23 MB
CustomList: Duration = 1076,54 ms, Memory Used = 2,96 MB
LinkedList: Duration = 41,55 ms, Memory Used = 3,95 MB
CustomLinkedList: Duration = 29,45 ms, Memory Used = 4,11 MB
```

## Conclusion

- For sequential insertions at the end, array-based lists (ArrayList/CustomList) are the fastest and memory-efficient.
- For insertions/removals at the beginning, linked lists (LinkedList/CustomLinkedList) win with O(1) operations.
