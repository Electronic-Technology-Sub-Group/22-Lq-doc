# lift

lift：一个函数在调用的时候，如果被 `map` 包裹了，那么它就会从一个非 functor 函数转换为一个 functor 函数。这个过程称为 `lift`

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
