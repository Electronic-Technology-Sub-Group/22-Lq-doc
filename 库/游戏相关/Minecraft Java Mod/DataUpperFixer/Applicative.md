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

‍
