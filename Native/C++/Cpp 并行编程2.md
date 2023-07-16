协程是一种通过任务调度，使用单线程模拟多线程，同时执行多个任务的模型。可以在函数执行到某个地方时暂停执行，随后可以从暂停的地方恢复执行。

优点：
- 相对线程来说，不需要切换线程上下文，任务切换相当快
- 实质是单线程模拟多线程，不需要锁机制，不需要控制共享资源，只需要判断状态即可

缺点：
- 无法充分利用多核处理器并行处理能力
- 阻塞操作（如IO）时会阻塞掉整个程序

协程实际是将“异步”风格编程“同步”化，大大增强单线程处理能力，但无法充分利用多线程处理能力。可使用多个线程+协程的组合。

控制关键字：
- `co_wait`：挂起协程，进行其他任务
- `co_return`：协程返回（任务结束）
- `co_yield`：协程返回一个结果并挂起，等待下次调用执行（任务阶段性完成）

规则：
- 一个线程只能有一个协程，即同时只能运行一个协程程序
- 协程返回值应为 `Promise`
- 协程控制关键字只能在协程中使用
- 可将异步函数包装在 `Awaitable` 类中，使用 `co_wait` 关键字调度
- 无栈协程：所有局部状态都在方法栈中，协程没有分配独立的栈空间

```c++
#include <thread>
#include <coroutine>
#include <functional>
#include <windows.h>

// 给协程体使用的承诺特征类型
struct  CoroutineTraits {        // 名称自定义 |Test|
    struct promise_type {        // 名称必须为 |promise_type|
        // 必须实现此接口。(协程体被创建时被调用)
        auto get_return_object() {
            return CoroutineTraits{};
        };

        // 必须实现此接口, 返回值必须为awaitable类型。(get_return_object之后被调用)
        auto initial_suspend() {
            return std::suspend_never{};   // never表示继续运行
            //return std::suspend_always{}; // always表示协程挂起
        }

        // 必须实现此接口, 返回值必须为awaitable类型。(return_void之后被调用)
        // MSVC需要声明为noexcept，否则报错
        auto final_suspend() noexcept {
            return std::suspend_never{};
        }

        // 必须实现此接口, 用于处理协程函数内部抛出错误
        void unhandled_exception() {
            std::terminate();
        }

        // 如果协程函数内部无关键字co_return则必须实现此接口。(协程体执行完之后被调用)
        void return_void() {}

        // 注意：|return_void|和|return_value| 是互斥的。
        // 如果协程函数内部有关键字co_return则必须实现此接口。(协程体执行完之后被调用)
        //void return_value() {}

        // 如果协程函数内部有关键字co_yield则必须实现此接口, 返回值必须为awaitable类型
        auto yield_value(int value) {
            // _valu=value;     // 可对|value|做一些保存或其他处理
            return std::suspend_always{};
        }
    };
};

// 协程使用的await对象
struct CoroutineAwaitObj {  // 名称自定义 |CoroutineAwaitObj|
    // await是否已经计算完成，如果返回true，则co_await将直接在当前线程返回
    bool await_ready() const {
        return false;
    }

    // await对象计算完成之后返回结果
    std::string await_resume() const {
        return _result;
    }

    // await对象真正调异步执行的地方，异步完成之后通过handle.resume()来是await返回
    void await_suspend(const std::coroutine_handle<> handle) {
        std::jthread([handle, this]() {
            // 其他操作处理
            _result = "Test";

            // 恢复协程
            handle.resume();
                     }).detach();
    }

    // 将返回值存在这里
    std::string _result;
};

// 协程体
// |CoroutineTraits| 并不是返回值，而是协程的特征类型；不可以是void、string等返回类型
CoroutineTraits CoroutineFunc() {
    std::cout << "Start CoroutineFunc" << std::endl;

    auto ret = co_await CoroutineAwaitObj();
    std::cout << "Return:" << ret << std::endl;

    std::cout << "Finish CoroutineFunc" << std::endl;
}

int main(int argc, char* argv[]) {
    CoroutineFunc();

    Sleep(10*1000);

    return 0;
}
```

