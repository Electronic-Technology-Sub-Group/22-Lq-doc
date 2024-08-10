# Traversable

```java
public interface Traversable<T extends K1, C,  Mu extends Traversable.Mu> extends Functor<T, Mu> {
    interface Mu extends Functor.Mu, Traversable.Mu {}
  
    <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input);

    default <F extends K1, A> App<F, App<T, A>> flip(Applicative<F, ?> applicative, App<T, App<F, A>> input);
}
```

`Traversable` 接口描述这么一种行为：`traverse`，对输入容器的数据进行操作（`flatMap`）后，将重新装箱的结果再装箱，返回的是双层 App。当 A 也是一个容器时，该方法可以用于变换外部容器类型。

另外一个方法 `flip` 是一个特例，保持内部元素不变，交换外部容器类型。
