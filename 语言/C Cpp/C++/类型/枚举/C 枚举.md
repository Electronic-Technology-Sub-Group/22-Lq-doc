---
语言: cpp
语法类型: 基础语法
---
直接使用 `enum` 声明的枚举。默认情况下，枚举类型为 `int`，从 0 开始递增。

```cpp
enum Weekday {
    Mon, Tues, Wed, Thurs, Fri, Set, Sun
};
```

> [!warning] 枚举成员在 C++ 中不是 `int` 类型的，但可隐式转换

```cpp
enum Weekday {
    Mon, Tues, Wed, Thurs, Fri, Set, Sun
};

int main() {
    // Mon=0, Tues=1
    cout << "Mon=" << Mon << ", Tues=" << Tues << endl;
    // g++: 7Weekday
    cout << typeid(Tues).name() << endl;
    return 0;
}
```

枚举可以自定义其底层类型和值，只要是某种整型即可
* 使用 `:` 指定底层类型
* 在没有自定义值的情况下，枚举值为上一个枚举值 +1
* 枚举值可以相同，`==` 运算符实际运算的是比较基类型的值

```cpp
// 内部数据以 unsigned long 类型存储
enum Weekday: unsigned long {
    Mon, Tues = 3, Wed, Thurs = 2, Fri, Set, Sun = 7
};

int main() {
    // 0, 3, 4, 2, 3, 4, 7
    cout << Mon << ", " << Tues << ", " << Wed << ", " << Thurs << ", " 
         << Fri << ", " << Set << ", " << Sun << endl;
    // 1
    cout << (Tues == Fri) << endl;
    return 0;
}
```