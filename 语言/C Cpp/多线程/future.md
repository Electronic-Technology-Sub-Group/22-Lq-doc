`std::future` 类用于解决异步问题，作为异步结果的传递通道。
* `std::future`：不可复制的异步结果
* `std::shared_future`：可复制的异步结果，可存于容器中
* `std::paclaged_task`：包装一个调用对象，绑定函数和 `future`

```run-cpp
#include <iostream>
#include <thread>
#include <future>

using namespace std;

int main() {
    packaged_task<int(int)> task ([] (int value) {return value * 2;});
    future<int> f = task.get_future();

    thread(std::move(task), 10).detach();
    cout << "Result is " << f.get() << endl;
    return 0;
}
```

* `std::promise`：包装一个值，绑定值和`future`

```run-cpp
#include <iostream>
#include <thread>
#include <future>

using namespace std;

int main() {

    promise<int> p;
    future<int> f = p.get_future();

    thread t_show_value([&] () {
        cout << "Waiting for value..." << endl;
        cout << "Future value is " << f.get() << endl;
    });

    thread t_get_value([&] () {
        cout << "Getting value..." << endl;
        this_thread::sleep_for(chrono::seconds(5));
        cout << "Value updated." << endl;
        p.set_value(10);
    });

    t_show_value.join();
    t_get_value.join();
    return 0;
}
```

‍
