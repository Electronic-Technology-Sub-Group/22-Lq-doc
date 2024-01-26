---
参考1: https://zhuanlan.zhihu.com/p/363757919
参考2: https://ruanyifeng.com/blog/2017/02/fp-tutorial.html
参考3: https://llh911001.gitbook.io/mostly-adequate-guide-chinese/
---
函数式编程属于声明式编程的一种，脱胎自数学的范畴论，使用数学表达式的方式处理数据。

> [!note]+ 范畴
> 包含以下所有元素的集合组成一个范畴
> 
> - 所有类型的对象
> - identity 及其他所有函子（纯函数）
> - 所有函子的组合

> [!note]+ 命令式编程
> 面向计算机硬件的抽象，存在变量、赋值语句、表达式、控制语句等组成，关注计算机的详细执行步骤，一步步告诉计算机要怎么做
> - 大部分通用性编程语言，包括 C/C++，Java，Python，C# 等都是命令式编程语言
> - 面向对象、面向过程思想都属于命令式编程思想

> [!note]+ 声明式编程
> 以数据结构的形式表达程序执行的逻辑，告诉计算机应该做什么，而不是具体要怎么做，因此通常不需要声明变量，也不需要包含循环控制语句。
> - SQL 就是一种典型的声明式编程语言

> [!note]+ 函数式编程
> 函数式编程思想脱胎于范畴论，本质是数学工具的编程实现。函数的值仅取决于函数参数，不依赖于其他状态。
> 
> 大多数通用语言通过闭包或类似的技术实现函数式编程，如 Java，C++ 或 JavaScript 的 lambda 表达式等。也有纯函数式语言，如 Haskell 等
# 函数

函数式编程中的函数指的是数学中的函数，即自变量的映射，具有以下特点：
- 函数是一等公民，即与其他数据类型具有相同地位，可用于参数、赋值等
- 函数是纯函数
## 纯函数

> [!note]+ 纯函数及副作用
> 纯函数是这样一种函数，即相同的输入，永远会得到相同的输出，而且没有任何可观察的副作用。
> 
> 副作用：与函数外部环境发生任何交互，包括但不限于任何对函数之外的状态发生改变，用户输入输出交互，HTTP 请求等

纯函数为函数式编程带来了很多优点：
- 可缓存性 Cacheable：纯函数可方便的根据输入做缓存
	- 延迟执行
- 可移植性 Portable：纯函数计算结果与外界环境无关，且不与外界产生任何交互
- 自文档化 Self-Documenting：依赖明确，便于观察和理解
- 可测试性 Testable：允许方便的自动生成输入并断言输出，不需要模拟外部环境
- 合理性 Reasonable：纯函数总是符合引用透明性的
- 并行性：纯函数不需要任何共享内存

> [!note]+ 引用透明性
> 一段代码可以替换成它执行所得的结果，而且是在不改变整个程序行为的前提下替换的，那么我们就说这段代码是引用透明的

最简单的一个纯函数称为 `identity` 或 `id` 函数，输入一个值，并直接返回输入的值。
## 函数运算

函数基本运算包括函数合成（Compose）和柯里化（Curry）。
### 柯里化

> [!note]+ 柯里化
> 柯里化 Curry 指只传递给函数一部分参数来调用它，让它返回一个函数去处理剩下的参数。

只传给函数一部分参数通常也叫做部分求值或局部调用，函数接收一些函数但不立即求值，而是返回一个保存有已有参数的新函数，直到所有参数都传入时再求值，能够大量减少样板文件代码。

`curry` 函数也是一个纯函数。每通过 `curry` 传递一个参数调用函数，就返回一个确定的新函数处理剩余的参数。某些实现中 `curry` 函数也允许一次传递多个参数，等效于多次调用 `curry` 函数。以下是 `curry` 函数的一个 Java 实现

```java
// Function3 接口
interface Function3<T1, T2, T3, R> { R invoke(T1 v1, T2 v2, T3 v3); }

// curry 方法的一种简单实现
<T1, T2, T3, R> BiFunction<T2, T3, R> curry(Function3<T1, T2, T3, R> func, T1 v1) {
    return (v2, v3) => func(v1, v2, v3);
}
```
### 代码组合

函数合成是指将多个函数组合后生成新函数的过程
#### pointfree

> [!note]+ Pointfree
> 无值风格，函数无须提及将要操作的数据是什么样的。
> 
> 运算过程抽象化，处理一个值，但不提到这个值。

```java
Function f1, f2;

// 非 pointfree 形式写法
// 因为提及了传入参数 v
Function f = v -> f2.apply(f1.apply(v));

// pointfree 形式的写法
// 假设一个 compose 方法
Function compose(Function f1, Function f2);
Function f = compose(f1, f2);
```
## 高阶函数

满足以下特点之一的函数称为高阶函数
- 接受函数作为参数，如集合中常见的 `map`，`foreach`，`reduce` 等
- 返回值是一个函数，如柯里化方法等
# 容器

容器是可以装载特定或任意类型值的对象 - 一个存放数据的盒子。
### functor

函子 `Functor` 是一种实现 `map` 函数的容器。这允许我们在不解开容器的情况下操作容器中的值。

```java
// 容器
interface Container<A> {}
// 函子
interface Functor<A> extends Container<A> {
    <B> Functor<B> map(Function<A, B> f);
}
```

![[Pasted image 20231022233403.png]]

在范畴学中，`functor` 接受一个范畴的对象和态射（`morphism`），然后把它们映射（`map`）到另一个范畴里去。根据定义，这个新范畴一定会有一个单位元（`identity`），也一定能够组合态射；我们无须验证这一点，前面提到的定律保证这些东西会在映射后得到保留。
- `functor` 相当于图中的 F，即容器；`F a` 表示函子中存放有 a 数据
- 态射相当于图中的 `f`；单位元即 `id` 函数

可以把范畴想象成一个有着多个对象的网络，对象之间靠态射连接。那么 `functor` 可以把一个范畴映射到另外一个，而且不会破坏原有的网络。如果一个对象 `a` 属于源范畴 `C`，那么通过 functor `F` 把 `a` 映射到目标范畴 `D` 上之后，就可以使用 `F a` 来指代 `a` 对象。

![[Pasted image 20231022234931.png]]
### pointed functor

pointed functor 是实现了 `of` 函数的函子。`of` 函数可用于创建一个函子。

> 在某些地方，`of` 也被称为 `point`，`pure`，`unit` 或 `return` 等不同名称

`of` 的作用形式上替代了 `new` 关键字，实际是表示将值放到默认最小化上下文中，关键是实现将任意值存入容器并到处 `map`
## Identity

最简单的函子，存储了一个值，与 `id` 函数作用相同（？）
## Maybe

类似 Java 的 Optional 类或 Kotlin 等语言的可空类型，该容器可空，调用 map 函数时会检查容器是否为空

在一些允许枚举类型的语言，如 Rust 中，Maybe 可能定义为 `Some(x)/None` 或 `Just(x)/Nothing`，可使用模式匹配处理
## Either

`Either` 是一个二选一的容器，分为 `Left`、`Right` 两个子类，类似 c 的 `union`。

`Either` 通常用于消除 `try-catch` 的副作用。`Right` 类似于 `Identity`，而 `Left` 多表示异常信息。除此之外，`Either` 还可以表示逻辑或、coproduct 等概念。
## applicative functor

Applicative 是实现了 `ap` 方法的 `pointed functor`。该接口实现的是让不同 `functor` 相互 `apply`（即 `map`） 的功能。

```java
// pointed functor
interface PointedFunctor<T> {
    <R> Applicative<R> map(Function<T, R> f);
    static <T> Applicative<T>(T value);
    T getValue();
}
// applicative functor
interface Applicative<T> extends PointedFunctor<T> {
    // ap 函数
    default <R, I> PointedFunctor<R> ap(PointedFunctor<I> other) {
        // 要求 T 本身是一个函数 Function<I, R>
        return other.map((Function<I, R>) value());
    }
    // static 版本的 ap，带有类型校验
    static <T, R> PointedFunctor<R> ap(
                      Applicative<Function<T, R>> f, 
                      PointedFunctor<T> v) {
        return v.map(f.value());
    }
}
```

这种写法有点像前序的运算符。下面例子中使用伪代码，F 是一个 `applicative functor`

```
add :: int -> int -> int
int a, b, sum

sum = F.of(add)      // F(int -> int -> int)
       .ap(F.of(a))  // F(int -> int)
       .ap(F.of(b))  // int
```

`map` 等价于 `of/ap`

```
f :: int -> int
int x

// 等价
F.of(x).map(f) == F.of(f).ap(F.of(x))
```
### lift

> [!note]+ lift
> 一个函数在调用的时候，如果被 `map` 包裹了，那么它就会从一个非 functor 函数转换为一个 functor 函数。这个过程称为 `lift`

可以通过 pointfree 的方式调用 applicative functor，其中 App 是一个 applicative functor

```java
App<R> lift(Function<T, R> f, App<T> v) {
    return v.map(f);
}
App<R> lift2(BiFunction<T1, T2, R> f, App<T1> v1, App<T2> v2) {
    return v1.map(f).ap(v2);
}
App<R> lift3(Function3<T1, T2, T3, R> f, App<T1> v1, App<T2> v2, App<T3> v3) {
    return v1.map(f).ap(v2).ap(v3);
}
// ... 可以有无数个
```
### 运算律

- 同一律：对 functor 应用 id 函数不会改变值

```
A: functor
v: A v

A.of(id).ap(v) == v
```

- 同态：不管是把所有的计算都放在容器里还是先在外面进行计算然后再放到容器里，结果都是一样的

```
A.of(f).ap(A.of(x)) == A.of(f(x))
```

- 互换：让函数在 `ap` 的左边还是右边发生 lift 是无关紧要的

```
v: x -> r
x: value

v.ap(A.of(x)) == A.of(f -> f(x)).ap(v)
```

- 组合：标准的函数组合适用于容器内部的函数调用

```
A.of(compose).ap(u).ap(v).ap(w) = u.ap(v.ap(w))
```
## Monad

monad 是一个可以扁平化（flatten）的 pointed functor。该容器允许延迟计算和自动剥离容器，每调用一次 `join()` 脱一层容器，`join()` 方法符合结合律、同一律。

> 结合律：compose(join, map(join)) == compose(join, join)

![[Pasted image 20231023024435.png]]

> 同一律：compose(join, of) == compose(join, map(of)) == id

![[Pasted image 20231023024525.png]]
### flatMap

在 `map` 后紧接 `join` 的操作组合称为 `flatMap`，在其他地方也有叫 `chain`，`bind`，`>>=` 等。通过 `flatMap` 可以直接获取 `map` 和 `ap` 函数

```java
interface Monad<T> {
    Monad<R> flatMap(Function<T, Monad<R>> f);
}

interface App<T> extends Monad<T> {
    default App<R> map(Function<T, R> f) {
        return flatMap(v -> of(f.apply(v)));
    }
    default App<R> ap(App<V> v) {
        return flatMap(f -> v.map((Function<V, R>) f));
    }
}
```
# 自然变换

自然变换 Nature Transformation, nt 是应用于容器上的一种变换。我们可以先自然变换再`map`，或者先`map`再自然变换，结果都是一样的。

![[Pasted image 20231023091816.png]]

```
nt :: (Functor F, Functor G) => F a -> G a

compose(map(f), nt) == compose(nt, map(f));
```

> [!note]+ 同构
> isomorphism，一个值在两种类型之间可以互相转换，且不会损失任何数据称为同构

在 Nature Transformation 接口中，既可以 from，又可以 to 的两个类型即同构。
# Traversable

Traversable 接口由 `sequence` 与 `traverse` 组成
- `sequence`：翻转内外容器类型
- `traverse`：对值进行变换后翻转内外容器类型

```
sequence :: (Traversable t, Applicative f) => (a -> f a) -> t(f a) -> f(t a)
traverse :: (Traversable t, Applicative f) =>
                            (a -> f a, a -> t b) -> a -> f(t b) 
```
# Monoid

> [!note]+ 半群
> 半群 Semigroup：集合 S 有二元运算 $S\times S\to S$，且满足结合律，即 $\forall x, y, z\in S$， 有 $(x\cdot y)\cdot z=x\cdot (y\cdot z)$，称数对$(S, \cdot)$ 为半群，$\cdot$ 为该半群的乘法。上下文明确的情况下，可简称半群 S
> 
> 任何 functor 都是半群

>[!warning]
>任何 functor 都是半群

> [!note]+ 幺半群
> 幺半群 Monoid：若半群 S 上的乘法有幺元（单位元），即 $\exists 1\in S$，有 $\forall s\in S, 1\cdot s=s\cdot 1=s$，称 S 为幺半群

半群定义了一个数据集合 S 和一个运算符 $\cdot$，该运算符合结合律，且任意两个集合集合中的元素通过运算符运算后结果仍在集合 S 中。

幺半群则是在半群的基础上，增加了一个单位元，该元素与任何元素 s 进行运算后结果仍为 s。

以集合为例。任意 `List` 组成的集合与运算 `addAll` 组成了一个幺半群。任意两个 `List` 执行 `addAll` 后形成的新列表仍是一个列表，符合半群定义；一个空列表可以认为是一个单位元，符合幺半群定义。

在函数式编程中，`Monoid` 定义的运算通常称为 `concat`，用以合并两个容器的结果。
- 函数可以是 Monoid
	- `identity` 可以作为单位元
	- 任意两个定义域和值域都在相同的集合内，可以通过 compose 进行 concat
- Monad 可以是 Monoid
- Applicative functor 可以是 Monoid：`lax monoidal functor`，通过实现 Monoid 后在还原出 ap
