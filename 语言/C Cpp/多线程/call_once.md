`std::call_once`  配合 `std::once_flag` 用于保证某一函数在多线程中仅执行一次

```cpp
#include <iostream>
#include <thread>
#include <mutex>

using namespace std;

once_flag flag;

void initialize() {
    call_once(flag, [] () {
        cout << "Invoke this" << endl;
    });
}

int main() {
    // Invoke this
    thread threads[5];
    for (auto & i : threads) {
        i = thread(initialize);
    }

    for (auto &item: threads) {
        item.join();
    }

    // All finished
    cout << "All finished" << endl;
}
```
