# 注释

使用 `#` 开头
# 判断与循环

Ruby 的判断与其他语言大同小异，仍然是借助布尔类型，但注意 `true` 和 `false` 属于两个不同的数据类型。

![[Pasted image 20230802231011.png]]

注意：
- 除 `nil` 和 `false` 外，其他所有值（包括 0）都隐式转换成 `true`
- 逻辑运算符 `and` 与 `&&` 等效，`or` 与 `||` 等效，都可以短路；如果不想短路，使用 `&` 和 `|`

```ad-note
`nil` 相当于其它语言的 `null`, `nullptr` 等
```

Ruby 的判断主要有以下几种方法，其中 `if` 与 `unless` 是一对反义词：

```ruby
if [condition]
    # do something
end

unless [condition]
    # do something
else 
    # do something
end

[do something] if [condition]
[do something] unless [condition]
```

```ad-note
Ruby 中代码块需要以 `end` 结尾
```

使用 `while` 和 `until` 实现循环，`while` 和 `unless` 互为反义词

```ruby
while [condition]
    # do something
end

[do something] while [condition]
```
# 基本容器
## 数组

数组使用 `[]` 声明

```ruby
arr = ['aaa', 'bbb']
```
- 空数组直接用 `[]`
- 数组支持下标访问
	- 负数：倒数第 n 个值
	- 切片：`arr[1..2]` 表示取出第 1 个和第 2 个元素作为新数组。`1..2` 实际返回一个 Range 对象
	- 数组内各元素类型不一定相同
- 压入新元素可以使用 `push`，弹出最后一个元素使用 `pop`
## 散列表

散列表类似其它语言的 Map，是一串键值对，使用 `{}` 声明，同样支持下标访问，键值类型随意

```ruby
# 创建散列表
numbers = {
    1 => 'one',
    2 => 'two',
    :three => 3
}
# 访问
puts numbers[1]
puts numbers[:three]
```

```ad-note
使用字符串时，相同字符串可能代表两个不同的变量
使用 `:符号名` 声明的符号不同，相同符号名代表的对象一定是相同的

![[Pasted image 20230803092839.png]]
```

使用散列表可以构建类似命名参数的函数参数

```ruby
def use_named_param(options={})
    # do something
end

use_named_param param1 => values 
```

```ad-note
1. Ruby 支持函数默认值，options 默认为空散列表
2. 当散列表为最后一个形参时，大括号可省略
```
# 函数与代码块

使用 `def` 定义函数。函数不一定要在类中。每个函数都有一个返回值，无返回值的函数返回 `nil`

```ruby
def is_nil(obj)
    return obj == nil
end
```

很多情况下函数有很多简写
- 没有明确标明返回值时，默认返回最后一行语句的结果
- 无参数时，参数列表可以省略
- 调用函数可以省略括号

```ruby
# 无参，直接返回 true
def tell_the_truth
    true
end

# 调用省略括号，输出 true
puts tell_the_truth
```
## 代码块

代码块使用 `{}` 包围，或使用 `do ... end` 的格式。按惯例，只有一行的代码块使用 `{}`，多行使用 `do-end` 结构，参数使用 `|x|` 标记

```ruby
3.times { puts 'this code will print 3 times' }

3.times do |x|
    puts 'hello'
    puts 'this is the %d times' % x
end
```

```ad-note
1. 整形的 times 方法表示后面的代码将被执行 n 次
2. 不需要参数时，参数可省略
```

使用 `&` 标记该参数表示该参数作为一个闭包传入，使用 `call` 函数执行。但注意！Ruby 中一个函数只能有一个闭包变量且必须是函数最后一个变量

在 Ruby，闭包这玩意吧，挺麻烦的，详见 [[[转]深入理解ruby闭包]]

```ruby
# 使用 & 表示变量是一个代码块或函数
def call_block(&block)
    block.call
end

def run_block(&block)
    # 代码块或函数传递时也要加 &
    call_block(&block)
end

def aaa
    puts 'aaa'
end

# aaa
run_block(aaa)
```
### yield

`yield` 也可以用来执行闭包。此时，每执行一次 `yield` 就是执行一次闭包，且闭包变量不能在函数参数列表上显式声明

`yield` 后可接传入的参数

```ruby
def run_three_times
    yield
    yield
    yield
end

run_three_times { puts 'called' }
```
# 类

Ruby 所有类继承自 Object，各个类的基类可以通过 class 文件的 `superclass` 获取

类通过 `class` 定义

```ruby
class Tree
    attr_accessor :children, :node_name

    def initialize(name, children=[])
        @children = children
        @node_name = name
    end

    def visit_all(&block)
        visit(&block)
        children.each { |c| c.visit_all &block }
    end

    def visit(&block)
        block.call self
    end
end
```

- 实例变量使用 `@`，类变量（相当于Java中 `static` 成员）使用 `@@` 声明
- `attr_accessor` 定义实例变量、访问方法和设置方法，是 `attr` 的一种
- `initialize` 函数为构造函数，在调用 `类名.new(...)` 时自动调用

```ad-note
命名规范：
1. 类名以首字母大写的驼峰式
2. 实例变量、方法名使用小写+下划线，常量使用大写+下划线
3. 用于逻辑测试的方法名以 `?` 为后缀
```
## 模块与 Mixin

Ruby 使用模块组合类，将功能组合到类上，以解决多继承的复杂性。

模块是一组函数和常量的集合。若一个类包含了一个模块，则该模块的所有成员将整合到类中。

使用 `module` 声明模块，然后通过 `include` 将其混合到类上。

```ruby
# 定义 ToFile 包含两个函数 filename, to_f 用于保存类到文件
module ToFile
    
    def filename
        "object_#{self.object_id}.txt"
    end

    def to_f
        File.open(filename, 'w') { |f| f.write(to_s) }
    end
end

class Person
    # 包含模块 ToFile
    include ToFile

    attr_accessor :name

    def initialize(name)
        @name = name
    end

    # 重写 to_s 方法
    def to_s
        name
    end

end

# to_f 已经加入了类中，可直接调用
Person.new('matz').to_f
```

Ruby 中的模块是通过鸭子类型实现的。ToFile 模块与 Person 类对象没有必然的联系，Person 中没有实现 ToFile 的方法，但我们不需要关心细节 - 我们只要可以通过这种方法调用接口（模块） 成员即可。
## 内置模块

- `enumerable`：定义了 `each` 方法，实现了 
	- `all?(|obj| block)`
	 - `any?(|obj| block)`
	 - `map(|obj| block)`
		 - `collect`：等同于 map
	 - `each_with_index(|obj, idx| block)`
	 - `find(ifnil=false, |obj| block)`
	 - `find_all(|obj| block)`
		 - `select`：等同于 find_all
	 - `include?(obj)`
	 - `inject(initial=nil, |memo, obj| block)`：类似于 JavaScript 的 `reduce`，但默认初始值为 nil 而非第一个元素
	 - `partition(|obj| block)`：按条件分割，返回两个集合
- `comparable`：定义了 `<=>` 运算符