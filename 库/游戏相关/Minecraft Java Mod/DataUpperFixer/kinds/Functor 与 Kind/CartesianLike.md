# CartesianLike

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
