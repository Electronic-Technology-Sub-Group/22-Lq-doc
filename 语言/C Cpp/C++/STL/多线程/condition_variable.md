---
语言: cpp
语法类型: 标准库
---
`std::condition_variable` 可以实现阻塞一个线程，并在满足某些条件或超时时唤醒线程，需要配合锁实现。

以下为一个 `CountDownLatch` 的一个实现：

```cpp
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>

using namespace std;

int main() {
    atomic_int count {10};

    mutex m;
    condition_variable cv;

    thread awaiting = thread([&] () {
        unique_lock<mutex> lock(m);
        while (count > 0) {
            // 每 5ms 检查一次
            cv.wait_for(lock, chrono::milliseconds(5));
        }
        cout << "Latch opened!" << endl;
    });

    thread count_down = thread([&] () {
        while (count > 0) {
            // 每 1s 计数一次
            this_thread::sleep_for(chrono::seconds(1));
            count--;
            cout << "Count = " << count << endl;
        }
        cout << "CountDown finished!" << endl;
    });

    awaiting.join();
    count_down.join();
}
```
