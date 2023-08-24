# 数组

数组创建在托管堆上，使用 `[]` 声明。数组创建可以有以下形式：

```csharp
T[] arrName;
arrName = new T[count];

T[] arrName = new T[count];
T[] arrName = new T[count]{ t1, t2, t3, ... };
T[] arrName = new T[] { t1, t2, t3, ... };
T[] arrName = { t1, t2, t3, ... };
```

声明多维数组后，数组维度无法改变

```csharp
T[,] arrNane = new T[count1, count2];

T[,] arrName = {
    { t00, t01, t02, ... },
    { t10, t11, t12, ... },
    { t20, t21, t22, ... }, ...
};
```

也可以声明数组的数组，对不同的数组不要求个数相同

```csharp
T[][] arrName = new T[count1][];
arrName[0] = new T[count2];
arrName[1] = new T[count3];
...
```

## Array

Array 类为抽象类，是所有数组的基类：
- 可用 `Array.CreateInstance` 创建数组
- 实现了 `IEnumerable` 接口：可使用 `for` 或者 `foreach` 循环
- 实现了 `ICloneable` 接口：可使用 `Clone()` 复制
- 当数组内元素实现了 `IComparable` 接口，可通过 `Array.Sort()` 排序
- 协变：`T[]` 可以匹配任何 T 子类的数组；但只能应用于引用类型，不能使用值类型

## ArraySegment

代表数组的一个片段。片段并不会复制原数组的数据，其内容改变后，原数组也会改变

```c#
int[] intArr1 = { 3, 7, 12, 5200, 15, 216, 56 };
int[] intArr2 = { 4, 6, 13, 5199, 16, 215, 57 };
// ArraySegment 表示数组的一段
var segments = new ArraySegment<int>[2]
{
    new ArraySegment<int>(intArr1, 0, 3),
    new ArraySegment<int>(intArr2, 2, 3)
};
int sum = 0;
foreach (var s in segments) {
    for(int i = s.Offset; i < s.Offset + s.Count; i++)
    {
        sum += s.Array[i];
    }
}
```
# Tuple

元组，结合了不同类型的对象

C# 默认提供了从 `Tuple<T1>` 到 `Tuple<T1, T2, ...., T7>` 共 7 种有限长度的元组， `<T1, ..., T7, TTuple<...>>` 7个标准类型和1个元组类型的元组
