`namespace` [[关键字]]可以声明一个[[命名空间]]，用于解决全局成员的命名冲突问题。

```c++
namespace aaa {
    int a {10};
}
```

`namespace` 是逻辑上的一种分割，因此同一个[[命名空间]]可以分布在不同文件中，同一文件中可以包含多个命名空间，也可以多次声明同一个命名空间。即使成员分布在不同文件中，名称相同的命名空间仍是同一个命名空间。

```c++
// a.h
namespace aaa {
    int a {10};
}
```

```c++
// b.h
namespace bbb {
    int a {100};
}
```

```c++
// c.h
namespace aaa {
    // 冲突：与 a.h 的 a 重名
    int a {20};
}
```

```c++
// d.h
namespace aaa {
    int b {20};
}

namespace bbb {
    int b {30};
}

// 没问题
namespace aaa {
    int c {50};
}
```

命名空间也可以嵌套

```c++
// e.h
namespace aaa {
    namespace bbb {
        namespace ccc {
            // aaa::bbb::ccc::abc = 10
            int abc {10};
        }
    }

    namespace ddd {
        // aaa::ddd::def = 20
        int def {20};
    }
}
```