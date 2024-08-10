#cpp20 `constinit` 说明符确保变量具有静态存储时间

* 校验声明的变量具有静态存储时间

```c++
constinit int x = 11;

int main() {
    constinit static int y = 42;
    // error: 'constinit' can only be applied to a variable with static or thread storage duration
    constinit int z = 10;
    return 0;
}
```

* 变量具有常量初始化程序

```c++
const char* f() { return "hello"; }
constexpr const char* g() { return "cpp"; }
// error: 'constinit' variable 'str1' does not have a constant initializer
// error: call to non-'constexpr' function 'const char* f()'
constinit const char* str1 = f();
constinit const char* str2 = g();
```

* 当用于非初始化声明时，表示 `thread_local` 变量已被赋值

```c++
extern thread_local constinit int x;
```
