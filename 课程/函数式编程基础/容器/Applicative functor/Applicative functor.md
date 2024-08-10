Applicative functor 是实现了 `ap` 方法的 `pointed functor`。该接口实现的是让不同 `functor` 相互 `apply`（即 `map`） 的功能。

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
