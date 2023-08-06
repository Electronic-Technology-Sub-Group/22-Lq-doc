# 开放类

在某个类首次使用 `class` 定义时，Ruby 会创造一个类。

当一个类被再次声明定义时，Ruby 会对当前类进行修改

```ruby
# 为系统内置 nil 的类 NilClass 新增 blank? 函数
class NilClass
    def blank?
        true
    end
end

# 为系统内置字符串类 String 新增 blanks? 函数
class String
    def blank?
        self.size == 0
    end
end

# true
puts nil.blank?
# true
puts ''.blank?
# false
puts '111'.blank?
```
# method_missing

通过重写 `method_missing` 方法，可实现自定义的函数，类似于其他语言的动态代理

```ruby
# 罗马数字类 RomanNum
# 使用时，形如 RomanNum.X RomanNum.XL 等会自动转换成整形
class RomanNum
    def self.method_missing name, *args
        roman = name.to_s
        # 将左减转换成右加，方便后面计算
        roman.gsub!("IV", "IIII")
        roman.gsub!("IX", "VIIII")
        roman.gsub!("XL", "XXXX")
        roman.gsub!("XC", "LXXXX")
        # 将遵循右加的罗马数字转换成整数
        roman.count("I") +
        roman.count("V") * 5 +
        roman.count("X") * 10 +
        roman.count("L") * 50 +
        roman.count("C") * 100
    end
end  
# 4
puts RomanNum.IV
# 150
puts RomanNum.LC
```

这么做的问题是，IDE 难以进行语法检查
# 模块
## 模块

可以通过向已存在的类添加模块方便的扩展类功能

```ruby
class 类名 < 模块名
end
```

例：为 CSV 类新增根据类名从文件读数据的功能

```ruby
# 模块类
class ActsAsCsv
    # 根据类名从文件读文件
    def read
        # 文件路径：类名（小写）.txt
        file = File.new(self.class.to_s.downcase + '.txt')
        # 将每一列标题保存到类变量 headers 中
        @headers = file.gets.chomp.split(', ')
        # 解析并存储到类变量 result 中
        file.each do |row|
            @result << row.chomp.split(', ')
        end
    end

    # getter
    def headers
        @headers
    end
    def csv_contents
        @result
    end

    # 构造函数
    def initialize
        @result = []
        read
    end
end

# 将模块类绑定到 RubyCsv 类
class RubyCsv < ActsAsCsv
end
```
## 宏

使用宏可以根据环境变化改变类行为

在模块类中定义一个函数（宏），在其中通过 `define_method` 定义函数

```ruby
class ActsAsCsv
    def self.act_as_csv
        define_method 'read' do
            # 文件路径：类名（小写）.txt
            file = File.new(self.class.to_s.downcase + '.txt')
            # 将每一列标题保存到类变量 headers 中
            @headers = file.gets.chomp.split(', ')
            # 解析并存储到类变量 result 中
            file.each do |row|
                @result << row.chomp.split(', ')
            end
        end

        define_method 'headers' do
            @headers
        end

        define_method 'csv_contents' do
            @result
        end

        define_method 'initialize' do
            @result = []
            read
        end
    end
end
```

之后绑定到 `RubyCsv` 类

```ruby
class RubyCsv < ActsAsCsv
    # 调用 act_as_csv 宏
    act_as_csv
end
```

当任何模块被其他模块包含时，Ruby 会自动调用 `included` 方法，之前的例子主要由于类也是模块。因此，我们可以将 ActsAsCsv 改写成模块

```python
module ActsAsCsv
    # 在包含模块调用 included 方法时自动附加 ClassMethods 模块
    # ClassMethods 模块中存在 acts_as_csv 方法，该方法附加 InstanceMethods 模块
    # InstanceMethods 模块附加相关属性和方法
    def self.included(base)
        base.extend ClassMethods
    end

    module ClassMethods
        # acts_as_csv 方法可以根据需要选择或组合包含的模块
        def acts_as_csv
            include InstanceMethods
        end
    end

    # 实际附加的模块
    module InstanceMethods
        def read
            @csv_contents = []
            file = File.new(self.class.to_s.downcase + '.txt')
            @headers = file.gets.chomp.split(', ')

            file.each do |row|
                @result << row.chomp.split(', ')
            end
        end

        attr_accessor :headers, :csv_contents

        def initialize
            read
        end
    end
end

# 我们这里使用的是模块，没有依赖其他类
class RubyCsv
    include ActsAsCsv
    acts_as_csv
end
```