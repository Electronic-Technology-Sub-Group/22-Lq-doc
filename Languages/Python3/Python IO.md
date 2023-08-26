# 格式化

- 方式：使用字符串前添加 f 表示含有引用内容；或使用 str.format() 方法。格式化部分用 {} 包围
- Template 类：str 类中包含一个 Template 类，使用类似 $x 占位符并用字典的值替换
- [格式规格迷你语言](https://docs.python.org/zh-cn/3.8/library/string.html#formatspec)：
	- 冒号：: 后整数部分为字符串最小宽度，会在前面补全空格；小数部分为小数四舍五入保留位数
	- 叹号：! 后跟某些方法处理
	- 顺序：使用 format 方法时，大括号中数字代表 format 参数顺序 {0} {1} 之类似的
	- 引用：使用 format 方法时，对应位置为 table 时，通过 `[]` 访问其 value
		- `'{0["a"]}, {0["b"]}'.foramt(table)`
	- 关键字：使用 format 方法时，大括号中文字代表 format 参数关键字
		- `"{abc}".format(abc="aaa")`

 | 符号 | 等效方法 | 说明                                                                                       |
 | ---- | -------- | ------------------------------------------------------------------------------------------ |
 | !a   | ascii()  | 类似 repr，非 ASCII 字符使用 \x \u \U 转义                                                 |
 | !s   | str()    | 返回 obj.__str__() 方法，若不存在该方法则返回 repr(obj)                                    | 
 | !r   | repr()   | 返回一个对象可打印表示形式的字符串，大多与 eval() 返回值相同，可用 obj.__repr__() 方法控制 |

```python
part0 = 'p0'
part1 = 'p1'
str1 = f'part0={part0}, part1={part1}'
print(str1) # part0=p0, part1=p1
yes_votes = 42_572_654
no_votes = 43_132_495
percentage = yes_votes / (yes_votes + no_votes)
str2 = '{:-9} YES votes {:2.2%}'.format(yes_votes, percentage)
print(str2) # ' 42572654 YES votes 49.67%'
```

- zfill：str.zfill(n) 可识别正负号，前面填 0
- rjust/ljust/center：左/右/居中对齐，填充空格
- printf 方式：% 操作符
    
    ```python
    import math
    # The value of pi is approximately 3.142.
    print('The value of pi is approximately %5.3f.' % math.pi)
    ```
# 文件

- with 块：使用 with 块可以自动关闭文件
    
    ```python
    with open("file") as f:
        f.read()
    f.closed # True
    ```
    
- 打开：使用 open(file, mode) 方法返回一个 file object 类型对象
    - mode
        - b：以二进制模式打开。默认以 text 模式打开，会自动根据操作系统替换换行符
        - raw：打开方式
            - r：只读
            - r+：读写
            - a：只写，写入到文档末尾
            - w：只写，若文件已存在则覆盖原文件
- 关闭
    - f.close()
    - 使用 f.closed 检查文件是否被关闭
- 读取
    - read：f.read([size])，读取 size 个字符（text 模式）或字节（二进制模式）
        - size 为空或负数时，获取到末尾
        - 若读取已经达到文件末尾，则返回 ''
    - readline：读取一行，包含换行符
        - 可使用 for 遍历行
            
            ```python
            for line in f:
                print(line, end='')
            ```
            
- 写入
    - f.write(value)：写入内容。若二进制模式 value 需要为字节，若 text 模式 value 需要为 str
- 其他
    - f.tell()：返回 int，给出当前文件指针位置
    - f.seek(offset, [whence])：修改文件指针位置
        - offset：偏移量
        - whence：相对值，0 为文件头，1 为当前位置，2 为文件末尾，默认 0
# Json

使用 json 包
- json.dumps(obj)：将对象转化为 json 字符串
    - json.dump(obj, f)：将对象转化为 json 字符串并保存到文件中
- json.load(f)：从文件读取 json 对象
# pickle

将任意 python obj 序列化与反序列化为二进制数据的协议