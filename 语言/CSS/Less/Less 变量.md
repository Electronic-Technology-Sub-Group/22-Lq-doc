变量可以在几乎任何地方使用，包括但不限于属性名（及其一部分），属性值，选择器，引入等。

如果变量作为构成 CSS 对象的一部分，如属性名，选择器，URL 等，通常使用 `@{属性名}` 的方式

# 变量声明与使用

变量应通过 `@` 声明，使用时也应通过 `@` 引用

```Less
@hover-color: #123456

a:hover {
    color: @hover-color;
}
```

## 属性

```Less
@a-color: #aa00aa;  
  
a, .link {  
  background-color: @a-color;  
}  
  
a:hover {  
  color: @a-color;  
}
```

编译成 CSS 后：

```CSS
a, .link {  
  background-color: #aa00aa;  
}

a:hover {  
  color: #aa00aa;  
}
```

## 属性名

```Less
@property-name: color;  
  
div {  
  @{property-name}: red;  
  background-@{property-name}: green;  
}
```

编译成 CSS 后：

```CSS
div {  
  color: red;  
  background-color: green;  
}
```

## 选择器

```Less
@my-selector: banner

.@{my-selector} {
    color: red;
}
```

编译成 CSS 后：

```CSS
.banner {  
  color: red;  
}
```

## 路径及导入

```Less
@img-head: ../img;  
@lib-head: ../lib;  
  
@import "@{lib-head}/less-import.css";  
  
body {  
  background: url("@{img-head}/white-sand.png");  
}
```

编译成 CSS 后：

```CSS
/* import 会被提前到整个文档的开头 */
@import "../lib/less-import.css";
body {  
  background: url("../img/white-sand.png");  
}
```

# 变量引用

在 Less 中，可以将一个变量的名字定义成一个变量，后面使用 `@@` 引用，有点像指针

```Less
@primary-color: green;  
  
.section {  
  @color: primary-color;  
  color: @@color;  
}
```

编译成 CSS 后：

```CSS
.section {  
  color: green;  
}
```

# 延迟计算

变量可以在任何地方声明，被赋值的总是当前作用域（大括号）内最后一次可访问的该名称所对应的值。
- 当前作用域内存在同名变量时，直接用当前域内的最终值，包括外部使用的内部变量
- 当前作用域内不存在同名变量时，会向上层层查询

```Less
@var-w: 20%;  
@var-h: 30%;  
  
.lazy-eval {  
  @var-h: 50%;  
  width: @var-w;  
  height: @var-h;  
  @a: 9%;  
  @var-h: 5%;  
}  
  
@var-w: @a;  
@var-h: @a;  
@a: 100%;
```

编译成 CSS 后：

```CSS
.lazy-eval {  
  /* @var-w 在上层最终引用了 @a 在域内有值为 9% */
  width: 9%;  
  /* @var-h 在域内就有最终值 5% */
  height: 5%;  
}
```

## 默认值

域内值可以覆盖域外引用，便可以用来改变默认值

```Less
@base-color: green;  
@dark-color: darken(@base-color, 10%);  
  
.widget {  
  @base-color: #FF00FF;  
  color: @dark-color;  
  background-color: darken(#FF00FF, 10%);  
}
```

编译成 CSS 后：

```CSS
.widget {  
  color: #cc00cc;  
  background-color: #cc00cc;  
}
```

可以看到， `@dark-color` 的值的确是 `darken(#FF00FF, 10%)`，即域内的 `darken(@base-color, 10%)` 的值。

此特性可用于各种 UI 库的默认值，且在实际使用时可自由的更改其默认值

# 属性变量

Less 3 新增功能，可直接使用属性名作为变量名，此时使用 `$`

```Less
.widget {  
  color: red;  
  background-color: $color;  
}
```

编译成 CSS 后：

```CSS
.widget {  
  color: red;  
  background-color: red;  
}
```
