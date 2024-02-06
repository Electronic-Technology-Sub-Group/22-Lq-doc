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
# thread_local

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

