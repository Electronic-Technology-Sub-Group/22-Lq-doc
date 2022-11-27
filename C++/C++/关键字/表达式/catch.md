与[[try]]共同构成异常处理，表示异常处理块中的捕获块

```c++
try {
    // do something
} catch (const std::exception &e) {
    // do something
} catch (const std::exception&) {
    // do something
} catch (...) {
    // do something
}
```

可捕获的类型不应是以下类型及其[[数组]]，[[引用]]和[[指针]]：
- [[不完整类型]]，[[void]]除外
- 抽象类类型
- [[右值]]引用

当我们不关心具体的异常值时，异常变量可省略（示例第二个 `catch` 块）

当我们使用 `...` 作为捕获变量时，表示该块可以捕获任意类型的异常