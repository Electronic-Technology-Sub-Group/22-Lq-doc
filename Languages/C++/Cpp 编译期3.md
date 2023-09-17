# 断言

用于在编译期对数据进行测试，若不通过则产生编译期异常，使用 `static_assert` 关键字触发

```c++
static_assert([constant_expression], [string_literal]);
```
- `[constant_expression]`：一个返回 `bool` 类型的常量表达式
- `[string_literal]`：若为 `false` 则显示的异常提示

该断言用于编译时，不会对运行时产生影响
## 类型检查

断言可用于对模板的类型进行检查，相关头文件位于`type_traits`中

```c++
template<class T>
T average(const vector<T>& data) {
    static_assert(is_arithnetic<T>::value, "Type T must is arithmetic");
    // do something
}
```

- `is_arithnetic<T>`：类型 T 实现了 `+` 等算术运算符
- 类型判断：`is_integral<T>`，`is_signed<T>`，`is_unsigned<T>`，`is_floating_point<T>`，`is_enum<T>` 等
# 属性

属性自 C++ 11 版本引入，使用 `[[  ]]` 包围，用于开发者向编译器提供额外信息，类似于 Java 的源码级与编译级注解。

属性可以修饰几乎任何 C++ 元素，包括函数、变量、成员名、类型、代码块、控制语句、Translation Unit 等。

- `[[noreturn]]`：函数没有返回（即无法触发任何 `return` 语句或无法执行到函数尾部）
	- 任何分支都有 `throw` 或 `exit()` 等方式禁止函数正常结束
- `[[carries_dependency]]`：搭配 `std::memory_order_consume` 使用，允许我们将 `dependency` 跨越函数进行传递，用于避免在弱一致性模型平台上产生不必要的内存栅栏导致代码效率降低。
- `[[deprecated]]`、`[[deprecated("reason")]]`：编译器输出警告：该元素将在未来被删除。可以带一个描述字符串
- `[[fallthrough]]`：抑制 `switch` 中 `case` 块没有 `break` 时编译器的警告行为
- `[[nodiscard]]`、`[[nodiscard("reason")]]`：该函数返回值不应该被直接丢弃。若其他地方使用了该函数但没有对返回值做进一步处理则编译器会产生警告
- `[[maybe_unused]]`：抑制编译器对未使用的变量、成员的警告
- `[[likely]]`，`[[unlikely]]`：修饰标号或非声明语句，告诉编译器哪一个分支执行的概率更大（小）
- `[[no_unique_address]]`：该成员拥有空类型，则编译器可以将它优化为不占用空间的部分。
# 循环引用

若头文件 A 引入了头文件 B，头文件 B 又引入了头文件 A，则会产生循环引用问题。可以尽量使用前向声明对其进行解绑，在源码文件中再引入相应的头文件
# 模板链接

通常情况下，我们将 C++ 代码分为头文件和源文件（dot-C 文件）两部分。
- 头文件扩展名常为 `.h`，`.H`，`.hh`，`.hpp`，`.hxx` 等
- 源文件扩展名常为 `.cpp`，`.c`，`.C`，`.cc`，`.cxx` 等

一般情况下，我们会将类、结构体等类型定义，全局变量，函数声明和内联函数实现放在头文件中，其他函数实现放在源文件中，并在源文件中引入对应头文件。这样定义对函数可见，且不会出现重复定义问题。

但模板函数有所不同，当以如下形式定义在两个文件中时编译能通过（符号都能找到），但若在其他地方使用时，会导致链接失败，提示找不到 `print_typeof` 函数。

```c++
// h 文件
template <typename T>  
void print_typeof(T value);

// cpp 文件
#include <iostream>
#include "tmp.h"
template<typename T>
void print_typeof(T value) {
    std::cout << typeid(value).name() << std::endl;
}
```

```c++
// other cpp
// 假设 print_typeof 函数在 "print_typeof.h" 头文件中定义
#include "print_typeof.h"

print_typeof(10);
```

问题在于，链接器从源代码中找到了 `print_typeof` 的定义头文件，但找不到其实现，链接器就无法将模板函数或模板类实例化成具体函数、具体类，这种模式称为模板的包含模型。而普通函数或类是不需要实例化这一步的。

要解决这个问题有两种解决办法
- 将涉及模板的具体实现与头文件放在一起
- 在能访问到的地方对使用到的类型实现模板特化
- 使用导出模板，不建议且大概率无效，详见[[Cpp 模板#导出模板]]
