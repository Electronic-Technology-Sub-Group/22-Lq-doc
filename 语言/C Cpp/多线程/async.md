`async` 对异步操作进一步封装：

```c++
async(std::launch::async | std::launch::deferred, func, args...);
```

最重要的还是 `std::launch::async | std::launch::deferred` 策略

* `std::launch::async`：任务执行在另一线程
* `std::launch::deferred`：任务执行在当前线程，延迟执行，在调用 `get` 或 `wait` 方法才会执行

‍
