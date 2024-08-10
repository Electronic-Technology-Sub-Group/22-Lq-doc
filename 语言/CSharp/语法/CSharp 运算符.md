- `new`、`sizeof`、`is`、`as`、`typeof`、`unchecked` 都是运算符
- `::`：名称空间别名限定符
- `??`：空合并运算符

```c#
int? a = null;
int b;
b = a ?? 10;    // b=10
a = 3;
b = a ?? 10;    // b=3
```

- `?.`，`?[]`：空值传播运算符
- `nameof()`：标识符的名称运算符，获取符号/属性/方法的名称

使用 `operator` 声明运算符重载

```c#
public static Type operator [运算符 +/-/...] (...) {...}
```

索引器： `[]` 运算符：

```c#
public [返回类型] this[索引类型(通常为int) index]
{
    get{}
    set{}
}
```

对于强制类型转换：

```C#
public static implicit operator B(A value) {...} // 必须显式转换
public static explicit operator B(A value) {...} // 可隐式转换
```

- `==` 与 `!=`、`<=` 与 `>=`、`<` 与 `>` 必须成对重载
- `==` 运算符必须重写 `Equals` 与 `GetHashCode` 方法
- `+=`、`-=`、`*=`、`/=`、`>>=`、`<<=`、`%=`、`&=`、`|=`、`^=` 会被自动隐式重写
