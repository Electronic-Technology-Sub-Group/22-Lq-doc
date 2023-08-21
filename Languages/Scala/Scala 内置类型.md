# Tuple

元组：包含多个不同种类元素的容器，不可变，常用于函数返回多个值，Tuple 类一直到 Tuple22（即22个）

```scala
// Tuple2[String, Int]，可简写为 (String, Int)
val ingredient = ("Sugar", 25)
```

使用 `_n` 访问元组中的值，从 1 开始

```scala
ingredient._1 // "Sugar"
ingredient._2 // 25
```
# 集合

Scala 集合包括可变集合与不可变集合，分别在 `scala.collection.mutable` 和 `scala.collection.immutable` 包中。

一般来说，若要同时使用可变和不可变集合，约定只导入 `scala.collection.mutable` 包，使用 mutable.Xxx 作为可变集合，Xxx 作为不可变集合。

`scala.collection.generic` 包中包含了集合的抽象构建块。

除了 Buffer 外，所有集合类都存在 根、可变、不可变 三种变体（Buffer 只存在可变）
## 继承关系

- 根继承：
![image-20200627145127884](image-20200627145127884.png)
- 可变：
![image-20200627145207479](image-20200627145207479.png)
- 不可变：
![image-20200627145252581](image-20200627145252581.png)
## 遍历

集合均实现了 Iterable 特征，该特征定义了以下几种基本方法

```scala
val list = List(0, 1, 2, 3, 4, 5)
```

- iterator、foreach：获取 Iterator 迭代器，或对其每个元素进行访问迭代
  
  ```scala
  val list = List(1, 2, 3, 4, 5)
  // Iterator[Int] = <iterator>
  val iter = list.iterator
  ```

- concat：连接两个集合
  
  ```scala
  val list0 = List(0, 1, 2)
  val list1 = List(3, 2, 1)
  // list = List(0, 1, 2, 3, 2, 1)
  val list = list0 + list1
  ```

- map、flatMap、collect：通过原本集合的元素生成新集合
	- collect 方法：接收一个偏函数（可以是普通函数，此时与 map 无异；也可以是 case 子句，相当于 filter+map）
  
  ```scala
  def list = List(0, 1, 2, 3, 4, 5)
  // List(0, 1, 10, 11, 100, 101)
  val retMap = list.map(i => i.toBinaryString)
  // List(0, 0, 1, 0, 1, 2, 0, 1, 2, 3, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 5)
  val retFlatMap = list.flatMap(i => 0.to(i))
  // List(10, 1, 12, 3, 14, 5)
  val retCollect = list.collect {
    case i if (i % 2 == 0) => i+10 // 0=>10 2=>12 4=>14
    case i => i // 1=>1 3=>3 5=>5
  }
  // List(10, 12, 14)
  val retCollect2 = list.collect {
    case i if (i % 2 == 0) => i+10 // 0=>10 2=>12 4=>14（map），其他因为没有 case 匹配，抛弃了（filter）
  }
  ```

- to 及 toList 等一系列转换方法：将该集合整体转换为其他类型集合；若该集合本就是该类型，则返回 this
  
  ```scala
  def list = List(0, 1, 2)
  // Set(0, 1, 2)
  println(list.toSet)
  // List(0, 1, 2)
  println(list.toList)
  // true
  println(list.toList == list)
  ```

- copyToArray：复制到数组
  
  ```scala
  val list = List(0, 1, 2, 3, 4, 5)
  val array = Array(-1, -1, -1, -1, -1)
  list.copyToArray(array, 1, 3)
  // -1, 0, 1, 2, -1
  println(array.mkString(", "))
  ```

- isEmpty，notEmpty，size，sizeCompare：判断该集合是否可以获取其存储的元素数目（如 LazyList 可能具有无限的元素等），获取集合元素数目等
	- knownSize：类似 size，一般来说可以通过有限的时间计算出 size 则会返回 size，否则（如需要遍历整个列表）返回 -1
	- sizeIs：提供一种 sizeCompare 的简化操作，list.sizeIs < size <==> list.sizeCompare(size) < 0，支持大小比较、\=\=、!= 的比较
- head、last、headOption、lastOption、find：集合元素检索/搜索
- tail、init、slice、take、drop、filter：通过某些选择或过滤，生成集合的子集合
	- tail/init：除 head/last 外的所有元素
	- slice：截取 `[fromInclude, toExclude]` 的元素

```scala
// List(1, 2, 3, 4, 5)
val tail = list.tail
// List(0, 1, 2, 3, 4)
val init = list.init
// List(1, 2, 3)
val slice = list.slice(1, 4)
```

- take/drop：选取前/后 n 个元素
	- takeWhile/dropWhile：选取/丢弃前 n 个连续的符合要求的元素后的集合
	- takeRight/dropRight：同 take/drop，倒序选取
  
  ```scala
  // List(0, 1, 2)
  val take = list.take(3)
  // List(3, 4, 5)
  val takeRight = list.takeRight(3)
  // List(0) 仅选取了 0
  val takeWhile = list.takeWhile(i => i % 2 == 0)
  // List(3, 4, 5)
  val drop = list.drop(3)
  // List(0, 1, 2)
  val dropRight = list.dropRight(3)
  // List(1, 2, 3, 4, 5) 仅丢弃了 0
  val dropWhile = list.dropWhile(i => i % 2 == 0)
  ```

- filter/filterNot：过滤
	- withFilter：非严格过滤器，会在后续的 map/flatMap/foreach/withFilter 等操作时才进行过滤运算
  
  ```scala
  // List(0, 2, 4)
  val filter = list.filter(i => i % 2 == 0)
  // List(1, 3, 5)
  val filterNot = list.filterNot(i => i % 2 == 0)
  // scala.collection.IterableOps$WithFilter
  // foreach: 0 2 4
  val withFilter = list.withFilter(i => i % 2 == 0)
  ```

- splitAt、span、partition、partitionMap、groupBy、groupMap、groupMapReduce、grouped、sliding：将一个集合拆分成多个子集
	- splitAt/span：在某个或第一个不符合某个条件的位置断开，返回 Tuple
	- partition/partitionMap：根据某个条件断开，返回 Tuple
	- groupBy/groupMap/groupMapReduce：将 list 折叠成 Map
	- grouped & sliding：将集合按元素数目拆分成迭代器
    
```scala
// (List(0, 1, 2),List(3, 4, 5))
val splitAt = list.splitAt(3)
// (List(0, 1, 2),List(3, 4, 5))
val span = list.span(i => i == 0 || i % 3 != 0)
// (List(0, 3),List(1, 2, 4, 5))
val partition = list.partition(i => i % 3 == 0)
// HashMap(0 -> List(0, 3), 1 -> List(1, 4), 2 -> List(2, 5))
val groupBy = list.groupBy(i => i % 3)
// Map(0 -> List(0, 11), 1 -> List(1, 100), 2 -> List(10, 101))
val groupMap = list.groupMap(i => i % 3)(i => i.toBinaryString)
// Map(0 -> 0+11, 1 -> 1+100, 2 -> 10+101)
val groupMapReduce = list.groupMapReduce(i => i % 3)
                                        (i => i.toBinaryString)
                                        ((i, j) => s"$i+$j")
// groupedList: Iterator[List[Int]]
//  0: List(1, 2, 3)
//  1: List(4, 5)
val groupedList = list grouped 3
// slidingList: Iterator[List[Int]]
//  0: List(1, 2, 3)
//  1: List(2, 3, 4)
//  2: List(3, 4, 5)
val slidingList = list sliding 3
```

- exists、forall、count：测试或遍历元素
	- exists：列表中存在符合某个条件的元素
	- forall：列表中所有元素都符合某条件
	- count：列表中符合某条件的元素数目
- foldLeft、foldRight、reduceLeft、reduceRight：折叠、展开元素
- sum、product、min、max：进行一些数字或可比较元素的具体操作折叠
- mkString、addString：将集合转换为字符串，addString 方法会将该字符串添加到一个 StringBuilder 中
- view：视图操作，返回一个 SeqView 对象
## Seq

Seq 特征表示序列。有序：从 0 开始；可迭代：length 已知

```scala
val seq0 = Seq(0, 1, 2, 3, 4, 5)
val seq1 = mutable.Seq(-1, -2, 2, 5, 10, 1)
```

- apply、isDefinedAt、indices：索引相关
	- apply：获取第 i 个元素，即 list(i)
- isDefinedAt：索引 i 是否包含在索引表 indices 中
    
    ```scala
    seq0.isDefinedAt(5) // true
    seq0.isDefinedAt(10) // false
    ```

- indices：所有可用索引
  
  ```scala
  seq0.indices // Range 0 until 6
  ```

- length、lengthCompare：长度相关。length 就是 size 的一个别名
- indexOf，segmentLength，startsWith，endsWith，contains，corresponds，search：搜索相关
	- indexOf/lastIndexOf/search：搜索元素第一次/最后一次出现的位置
	- indexOfSlice/lastIndexOfSlice：序列版本
      
      ```scala
      seq0.indexOfSlice(mutable.Seq(2, 3)) // 2
      seq0.indexOfSlice(mutable.Seq(3, 5)) // -1
      ```

- indexWhere/lastIndexWhere：满足要求的版本
- segmentLength：满足要求的连续子序列的最长长度
- contains：列表包含某元素
	- sameElements：两个列表包含相同元素且序列相同，一般与 == 可替代
	- containsSlice：是否包含某序列
	- corresponds：二元谓词测试，将两个列表相同位置的元素作为一对进行测试
- startsWith/endsWith：以某元素开头或结尾
- prepended，append，padTo：增加元素
  - prepended/appended：返回在列表前/后增加元素后的新列表
    - prependedAll/appendedAll：连接列表的版本
  - padTo：在指定位置添加元素后的新序列
- updated，patch，update：对列表的某些值进行替换
  - updated：将列表某位置的元素替换，即使是可变列表也返回改变后的新列表。
  - update：仅可变列表，用于更新列表的元素，list.update(i, a) 相当于 list(i)=a
- sorted，sortWith，sortBy：排序
- reverse，reverseIterator：返回反转的列表或倒序迭代器
- intersect，diff，distinct，distinctBy：集合间的操作
  - intersect：求两集合交集
    
    ```scala
    // List(1, 2, 5)
    seq0.intersect(seq1)
    ```

- diff：求两集合差异
  
  ```scala
  // List(0, 3, 4)
  seq0.diff(seq1)
  ```

- distinct：去重后的列表
- distinctBy：删除元素后的元素