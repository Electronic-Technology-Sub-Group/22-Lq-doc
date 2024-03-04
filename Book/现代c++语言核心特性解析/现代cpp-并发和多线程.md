# 线程局部存储

> [!note]
> 线程局部存储：独立于各个线程的存储对象，对象内存在线程开始后分配，线程结束时进行回收，且每个线程都有各自独立的对象实例

不同操作系统 API 都提供了创建线程局部存储的方法，但 C++ 并未在语言层面上对其进行支持。C++11 之前，各编译器也提供了自己的线程局部变量

- Windows：通过 TlsAlloc 分配一个 *线程局部存储槽索引（TLS slot index）*，实质是 *线程内部环境块（TEB）* 的线程局部存储数组的索引

```c++
#include <windows.h>
#include <cstdio>

DWORD tlsThreadName;

// 线程执行的任务方法
DWORD WINAPI Fun(LPVOID lpParam) {
    // 初始化线程数据
    LPVOID lpvData;
    // 通过 LocalAlloc 申请内存
    lpvData = LocalAlloc(LPTR, 20 * sizeof(char));
    sprintf((char*) lpvData, "Thread %lu", GetCurrentThreadId());
    // 保存当前值 -- 实际上 TLS 保存的是一个指针
    TlsSetValue(tlsThreadName, lpvData);

    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", (char*) TlsGetValue(tlsThreadName), i);
    }

    // 释放通过 LocalAlloc 申请的内存
    LocalFree(lpvData);
    return 0L;
}

int main() {
    // 通过 TlsAlloc 申请一个空槽位（索引）
    tlsThreadName = TlsAlloc();

    // 创建两个线程，运行并等待结束
    HANDLE hThread1 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    HANDLE hThread2 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    WaitForSingleObject(hThread1, INFINITE);
    WaitForSingleObject(hThread2, INFINITE);

    // 释放占用的索引
    TlsFree(tlsThreadName);
    return 0;
}
```

- Linux：通过 `pthread_key_create` 与 `pthread_key_delete` 创建与释放线程存储对象

```c++
#include <pthread.h>
#include <cstdio>

pthread_key_t key;

// 线程执行的任务方法
void *fun(void *p) {
    // 初始化线程变量
    char name[21];
    pthread_t thread = *static_cast<pthread_t *>(p);
    pthread_getname_np(thread, name, (size_t) 20);
    pthread_setspecific(key, name);

    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", (char *) pthread_getspecific(key), i);
    }
    
    return nullptr;
}

int main() {
    // 申请一个键
    pthread_key_create(&key, nullptr);
    // 创建两个线程，执行方法
    pthread_t ps0, ps1;
    pthread_create(&ps0, nullptr, fun, &ps0);
    pthread_create(&ps1, nullptr, fun, &ps1);
    pthread_join(ps0, nullptr);
    pthread_join(ps1, nullptr);
    // 移除键
    pthread_key_delete(key);
    return 0;
}
```

- 编译器方法：gcc 使用 `__thread` 修饰变量，vs 使用 `__declspec(thread)` 修饰变量

```c++
#include <windows.h>
#include <cstdio>

// 根据编译器选择修饰
#ifdef __GNUC__                       // gcc
__thread char name[20];
#elif _MSC_VER                        // vs
__declspec(thread) char name[20];
#endif

// 线程执行的任务方法
DWORD WINAPI Fun(LPVOID lpParam) {
    sprintf(name, "Thread %lu", GetCurrentThreadId());
    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", name, i);
    }
    return 0L;
}

int main() {
    // 创建两个线程，运行并等待结束
    HANDLE hThread1 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    HANDLE hThread2 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    WaitForSingleObject(hThread1, INFINITE);
    WaitForSingleObject(hThread2, INFINITE);
    return 0;
}
```
## thread_local

C++11 新增 `thread_local` 声明，声明一个对象的生命周期是一个线程。该声明可以与 `static` 与 `extern` 结合。

> [!warning]
> 使用 `thread_local` 声明的变量仅仅定义了其生命周期，并没有限制其可访问性。即允许通过 `&` 取变量值交给其他线程使用，但危险性过高

> [!warning]
> 使用 `thread_local` 声明的变量地址在运行时计算，不是一个常量，因此对其取址（`&`）的值对编译器不可见

```c++
#include <windows.h>
#include <cstdio>

thread_local char name[20];

// 线程执行的任务方法
DWORD WINAPI Fun(LPVOID lpParam) {
    sprintf(name, "Thread %lu", GetCurrentThreadId());
    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", name, i);
    }
    return 0L;
}

int main() {
    // 创建两个线程，运行并等待结束
    HANDLE hThread1 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    HANDLE hThread2 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    WaitForSingleObject(hThread1, INFINITE);
    WaitForSingleObject(hThread2, INFINITE);
    return 0;
}
```
# 协程

C++20 开始引入协程，带有 `co_await`，`co_return` 或 `co_yield` 任意关键字的函数称为协程函数,通常配合 `future`，`generator` 标准库使用。
- 协程函数不能是 `main` 函数，构造函数，析构函数，`consteval`，`constexpr`
- 协程函数不能使用 `return`
- 协程函数不能使用变长参数

> [!warning]
> 注意：协程由于不需要操作系统参与调度，可以节省切换线程的开销。但协程不一定比线程快，协程程序根本上还是单线程，在做 IO 相关或类似的需要 CPU 等待的任务时有优势，但在做 CPU 运算密集型程序时，多线程通常比协程优势更大，此时线程切换开销相比硬件上的并行运算节省的时间可以忽略。

有关协程的三个关键字：
- `co_await` 触发一个挂起点。开始执行任务，后接一个等待器（任务）。
- `co_return` 触发一个挂起点。方法执行完成
- `co_yield` 触发一个挂起点。暂停执行并返回一个值
设普通函数 A 调用了协程函数 B，B 中 `co_await` 触发一个耗时任务并返回 A，等待任务完成后重新回到 B；`co_return` 表示 B 执行完成，设置返回值后回到 A。`co_yield` 表示 B 产生了一个新的值并返回 A，但 B 还未完全完成。

> [!warning]
> 就 C++20 来说，标准库中还未有现成的用于返回的类型，需要自定义

`co_await` 后接一个等待器（`awaiter`），要求存在以下成员：
- `bool await_ready()`：当该函数返回 `true` 时，表示数据已经准备好，无需继续等待
- `void await_suspend(std::coroutine_handle<> h)`：当数据未准备好时，执行此函数
	- `std::coroutine_handle`：协程句柄，可用于控制协程流程，`operator()` 和 `resume()` 函数可以用于恢复协程
	- 允许返回 `void`，`bool` 或 `coroutine_handle` 类型
		- `void`，`true`：将执行权交给调用者，协程保持挂起
		- `false`：恢复当前协程运行
		- `coroutine_handle`：恢复 `handle` 对应的协程
- `T await_resume()`：返回协程执行结果，该结果称为可等待体

`co_yield`，`co_return` 要求函数返回类型是一个 `std::coroutine_trait<T>` 的一个子类，并有一个嵌套类型 `promise_type`
- `promise_type`：用于存放数据的类型
	- `T get_return_object()`：设协程函数 B 在 A 中调用，该函数的返回值就是调用后返回给 A 的值
	- `awaiter initial_suspend()`，`awaiter final_suspend()`：用于给库代码编写者在协程前后挂起机会的等待器，通常返回：
		- `suspend_always`：必然挂起，常用于 initial。final 使用这个时需要手动销毁协程句柄
		- `suspend_never`：从不挂起，常用于 final
	- `yield_value(T value)`：保存操作数的值，并返回等待器，通常返回 `suspend_always`
	- `void return_void()` 或 `void return_value(T value)`：二选一，后者对应 `co_return` 有值的情况
	- 可选：`V await_transform(expr e)`，使 `co_await expr` 转变为 `co_await await_transform(expr)`
	- `void unhandled_exception()`：产生异常时调用