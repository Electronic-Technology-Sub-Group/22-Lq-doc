---
_filters: []
_contexts: []
_links: []
_sort:
  field: rank
  asc: false
  group: false
_template: ""
_templateName: ""
---
Rust 允许使用不安全 Rust，关闭部分编译器静态检查。其存在意义在于

- 静态分析本质上是保守的，某些代码可能合法但编译器无法获取足够信息
- 底层计算机硬件固有的不安全性，Rust 需要直接与操作系统交互

使用 `unsafe`​ 关键字切换到不安全 Safe，不安全意味着可以

- 解引用裸指针
- 调用不安全函数
- 访问或修改可变静态变量
- 实现不安全 `trait`​
- 访问 `union` ​ 字段，兼容 C 中的联结体