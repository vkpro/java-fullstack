# Fibonacci Algorithms Benchmark Results

```shell
Algo       n          Duration (ms)   Memory (KB)    
-------------------------------------------------
recursive  10         8               491.55         
memorized  10         0               43.36          
iterative  10         0               0.00           
-------------------------------------------------
recursive  20         0               20.49          
memorized  20         0               54.37          
iterative  20         0               0.00           
-------------------------------------------------
recursive  30         0               0.00           
memorized  30         0               3.80           
iterative  30         0               0.00           
-------------------------------------------------
recursive  35         30              0.00           
memorized  35         0               2.59           
iterative  35         0               0.00           
-------------------------------------------------
recursive  40         406             0.00           
memorized  40         0               2.59           
iterative  40         0               0.00           
-------------------------------------------------
recursive  45         3661            1083.91        
memorized  45         0               2.59           
iterative  45         0               0.00           
-------------------------------------------------
recursive  50         58043           0.00           
memorized  50         0               2.59           
iterative  50         0               0.00           
-------------------------------------------------
memorized  20000      13              3238.05        
iterative  20000      0               0.00           
-------------------------------------------------
memorized  200000     63              21328.85       
iterative  200000     0               0.00           
-------------------------------------------------
memorized  400000     29              24031.63       
iterative  400000     0               0.00       
```

---

## Conclusion

For large n, `fibonacciIterative` algorithm is fastest and uses minimal memory. `fibonacciMemoized` is also efficient but uses more memory due to caching. `fibonacciRecursive` is extremely slow and impractical for large n.