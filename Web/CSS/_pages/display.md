
# 外部模式

- block：块级模式
- inline：行内模式

# 内部模式

- flow：流式布局
- flow-root：块级元素，该元素将作为新的块级格式化上下文的根元素
- table：块级元素，行为类似 `<table>`
- flex：块级元素，弹性盒模型
- grid：块级元素，网格模型
- ruby：实验性，类似内联元素，行为类似 `<ruby>`

# 列表元素

- list-item：允许使用 `list-style-type` 和 `list-style-position`，类似列表元素

# 内部：

`table`，`ruby` 等具有内部结构的布局，需要内部的 `display` 值声明对应的结构

# 盒

是否产生 display 盒
- none：隐藏元素，且不占用布局空间
	- 若需要隐藏元素但占据布局控件，应使用 `visibility` 属性
- contents：元素自身不再产生盒子（伪盒子/子盒子取代）
	- 无障碍可能无法正常使用（浏览器bug）

# 预组合

- inline-block：inline flow-root
- inline-table：inline table
- inline-flex：inline flex
- inline-grid：inline grid

*双值属性：CSS 3 定义了 `display` 支持双值属性，因此预组合可以直接使用两个属性代替。但目前一些浏览器对双值属性支持并不好*