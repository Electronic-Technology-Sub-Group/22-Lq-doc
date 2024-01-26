---
状态: 未完成
未完成部分: Representable，App2 & K2
---
DFU `datafixers.kinds` 包记录了 DFU 中容器类型的设置。

函数式编程的最大特点就是直接操作容器而不是直接操作数据，对数据的变换对原容器中的数据不产生影响，而是创建一个包含新数据的容器。
# App 与 K

DFU 中实际数据都是存在在一个个容器中的，App 和 App2 即容器的接口，该接口规定了两个泛型 F 和 A(与B)，没有任何需要实现的成员

```java
public interface App<F extends K1, A> {}
public interface App2<F extends K2, A, B> {}
```

两个泛型中，`F` 表示容器类型，每个容器中都有一个特定实现的空类型用于区分实际容器类型。`A`/`B` 则表示容器中数据的类型。

`F` 继承自 `K1` 或 `K2`。`K1` 对应 `App`，`K2` 对应 `App2`，用于在模糊化容器类型后还能重新找回容器类型 - 每个容器都有一个属于自己的 `Mu` 类，该类继承自 `K1` 或 `K2`，可以代表该类型并通过对应类型的 `unbox` 方法找回自己的类型。

`K1` 和 `K2` 中的 1 或 2 表示数据运算时，每次算子运算的数据个数，而后面的 A B 则表示每个运算数的类型。大多数容器类都只存储一类数据，因此都是 `App1`。而 `Pair` 和 `Either` 类由于每次只处理一个数据，也是 `App1` 类型。
# Functor 与 Kind

```java
public interface Functor<F extends K1, Mu extends Functor.Mu> extends Kind1<F, Mu> {
    interface Mu extends Kind1.Mu {}
    <T, R> App<F, R> map(Function<T, R> func, App<F, T> ts);
}
```

`Functor` 意为函子，定义了一个方法 `map`，通过一个 `Function` 根据一个容器中的数据创建新数据，并将新数据打包返回。

**可以说，`Functor` 是整个 DFU 实现数据变换工作的核心接口**。

`Functor` 两个泛型 F 和 Mu 分别表示操作的容器类型和算子本身类型。

`Functor` 继承自 `Kind1`，而 `Kind1` 继承自 `App<Mu, F>`，`Kind1` 的子接口只有 `Functor`。`Kind1` 实现了一系列方法创建 `Product` 对象，可用于进一步的对象变换。
## CartesianLike

```java
public interface CartesianLike<T extends K1, C,  Mu extends CartesianLike.Mu> extends Functor<T, Mu> {
    interface Mu extends Functor.Mu, Traversable.Mu {}
    
    <A> App<Pair.Mu<C>, A> to(App<T, A> input);
    <A> App<T, A> from(App<Pair.Mu<C>, A> input);

    default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input);
}
```

该接口描述通过算子变换前后的数据集满足双射的关系。该接口的三个泛型 `T`, `C`, `Mu` 分别表示操作容器类型、映射对应的另一个类型、算子类型。

该接口定义了三个方法。`from` 和 `to` 分别表示从两个集合之间的映射结果，`traverse` 则具有类似 `flatMap` 的效果，变换函数返回的就是一个容器。

该接口 `from`, `to` 的返回值实际是 `Pair` 类型，表示一个切实存在的一一映射。
## CocartesianLike

```java
public interface CocartesianLike<T extends K1, C,  Mu extends CocartesianLike.Mu> extends Functor<T, Mu> {
    interface Mu extends Functor.Mu, Traversable.Mu {}
    
    <A> App<Either.Mu<C>, A> to(App<T, A> input);
    <A> App<T, A> from(App<Either.Mu<C>, A> input);

    default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input);
}
```

该类型表示经算子变换后的结果集和输入集之间有满射关系，但不满足双射关系，因此返回的是一个 `Either` 而不是 `Pair`
## Traversable

```java
public interface Traversable<T extends K1, C,  Mu extends Traversable.Mu> extends Functor<T, Mu> {
    interface Mu extends Functor.Mu, Traversable.Mu {}
    
    <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input);

    default <F extends K1, A> App<F, App<T, A>> flip(Applicative<F, ?> applicative, App<T, App<F, A>> input);
}
```

`Traversable` 接口描述这么一种行为：`traverse`，对输入容器的数据进行操作（`flatMap`）后，将重新装箱的结果再装箱，返回的是双层 App。当 A 也是一个容器时，该方法可以用于变换外部容器类型。

另外一个方法 `flip` 是一个特例，保持内部元素不变，交换外部容器类型。
## Representable

还未用到，暂时作用未知
# Applicative

```java
public interface Applicative<F extends K1, Mu extends Applicative.Mu> extends Functor<F, Mu> {
    interface Mu extends Functor.Mu {}

    <A> App<F, A> point(A a);
    <T1, T2, T3, R> Function3<App<F, T1>, App<F, T2>, App<F, T3>, App<F, R>> lift3(App<F, Function3<T1, T2, T3, R>> function)
    <A, R> App<F, R> ap(Function<A, R> func, App<F, A> arg);
    <T1, T2, T3, R> App<F, R> ap3(App<F, Function3<T1, T2, T3, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3);
    <A, B, R> App<F, R> apply2(BiFunction<A, B, R> func, App<F, A> a, App<F, B> b);
}
```
# 内置容器

DFU 提供了几种内置容器，通常都含有 `create` 方法用于创建对应容器，以及 `unbox` 方法用于从 `App<Mu, ?>` 类型的对象转换为具体的实现类型。

同时，几乎每个容器都包含一个 `Instance` 类，该类实现 `Application` 接口，用于操作容器内的数据。
## IdF

```java
public final class IdF<A> implements App<IdF.Mu, A> {
    public static final class Mu implements K1 {}

    final A value;
}
```

IdF 即 `IndentityF` 是一种最简单的容器，该容器只读的存储了一个值。

该类的静态方法中没有 `unbox` 方法，但有 `get` 方法可用于直接获取其中的值。
## Const

```java
public final class Const<C, T> implements App<Const.Mu<C>, T> {
    public static final class Mu<C> implements K1 {}

    public static <C, T> C unbox(App<Mu<C>, T> box) {
        return ((Const<C, T>) box).value;
    }

    private final C value;

    Const(C value) {
        this.value = value;
    }
}
```

Const 也是存储一个常量，但他的数据通过一个 `Monoid` 构建。`Monoid` 表示一个容器，其行为符合数学上的幺半群，自定义运算符为两个容器连接，即：
- 两个内部元素类型为 T 的容器连接后的结果仍是一个内部元素类型为 T 的同类容器
- 多个容器按顺序连接，符合结合律
- 有一个容器与其他所有容器相连接都是原容器
一个典型的实现是 List

Const 有一个 unbox 函数，但实际做的是 IdF 的 get 的事情，即取出值而不是转换为 Const 类
## ListBox

该类存储一个 List，但他的 Instance 实现了 `Traversable` 没有实现 `Application`
## Either

`Either` 本身包含两个泛型，其 App 类型为 `Either<L, R> implements App<Mu<R>, L>`。

`Either` 实现的是 `if-else` 或 `try-catch` 模型。通常情况下，右值 `Right` 是正常情况下使用的值，左值 `Left` 是右值不存在时使用的值，或异常信息。

`Either` 可以通过 `swap()` 方法交换 `Left`、`Right` 值。
## Pair

`Pair` 是一个包含两个值的容器，其 App 类型为 `Pair<F, S> implements App<Mu<S>, F>`，可以通过 `swap()` 方法交换 First、Second 值。
## Monad

```java
public interface Monoid<T> {
    T point();
    T add(final T first, final T second);
}
```

`Monand` 表示指代一种配备 “monoid” 结构的函子。

简单来说，满足以下情况：
- （单位元）存在单位元 `e: Unit -> M`，对应 `point()` 方法返回的是一个无内容的容器
- （结合律）两个容器进行某种运算后结果仍为该容器

若将该结构类比于面向对象的普通 Collection，单位元即空容器，某种运算即 addAll

该结构通常用于带有副作用的操作，如 IO
## DataResult

该类用于存储一组数据操作后结果，包含一个 Either