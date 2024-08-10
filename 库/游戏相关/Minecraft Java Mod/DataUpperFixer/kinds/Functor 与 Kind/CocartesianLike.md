# CocartesianLike

```java
public interface CocartesianLike<T extends K1, C,  Mu extends CocartesianLike.Mu> extends Functor<T, Mu> {
    interface Mu extends Functor.Mu, Traversable.Mu {}
  
    <A> App<Either.Mu<C>, A> to(App<T, A> input);
    <A> App<T, A> from(App<Either.Mu<C>, A> input);

    default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input);
}
```

该类型表示经算子变换后的结果集和输入集之间有满射关系，但不满足双射关系，因此返回的是一个 `Either` 而不是 `Pair`
