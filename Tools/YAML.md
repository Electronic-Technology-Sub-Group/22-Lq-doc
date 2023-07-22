# YAML

YAML 是一种数据格式，一般文件名以 .yml 为后缀

## 基本语法

- 大小写敏感
- 空格表示缩进，不使用 Tab
- 空格表示层级，空格个数不重要，但要左对齐

## 数据类型

### 注释

以 # 开头

```yaml
# 这是一条注释
```

### 表

或称字典、对象、映射等。一个对象以 : 为一个键值对（冒号后要空格），以空格表示子表

```yaml
key: value
map2: 
 child-key1: value1
 child-key2: value2
```

等价于

```json
{
    "key": "value",
    "map2": {
        "child-key1": "value1",
        "child-key2": "value2"
    }
}
```

较简单的子表可以以 {} 表示子表，此时 : 后无空格

```yaml
map2:{child-key1: value1, child-key2: value2}
```

复杂的表可以将键与值分开，以 ? 和 : 分割

```yaml
?
 key1
 key2
:
 value1
 value2

# 等价于
key1: value1
key2: value2
```



### 数组

以 - 开头表示，或 []

```yaml
array1:
 - A
 - B
 - C
array2: [A, B, C]
```



### 布尔值

真：true，True，TRUE 均可

假：false，False，FALSE 均可

### 其他

字符串，整数，浮点，Null，时间与日期

## 引用

& 建立锚点，* 引用锚点，<< 合并数据

```yaml
defaults: &defaults
  adapter: postgres
  host: localhost

development:
  database: _development
  <<: *defaults

test:
  database: _test
  <<: *defaults
```

等价于

```json
{
    "defaults": {
        "adapter": "postgres",
        "host": "localhost"
    },
    "development": {
        "database": "_development",
        "adapter": "postgres",
        "host": "localhost"
    },
    "test": {
        "database": "_test",
        "adapter": "postgres",
        "host": "localhost"
    }
}
```

又如

```yaml
- &showell Steve 
- Clark 
- Brian 
- Oren 
- *showell
```

即

```json
["Steve", "Clark", "Brian", "Oren", "Steve"]
```

