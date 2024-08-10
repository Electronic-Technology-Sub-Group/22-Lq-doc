# Functor 与 Kind

```java
public interface Functor<F extends K1, Mu extends Functor.Mu> extends Kind1<F, Mu> {
    interface Mu extends Kind1.Mu {}
    <T, R> App<F, R> map(Function<T, R> func, App<F, T> ts);
}
```

`Functor` 意为函子，定义了一个方法 `map`，通过一个 `Function` 根据一个容器中的数据创建新数据，并将新数据打包返回。

**可以说，****`Functor`** **是整个 DFU 实现数据变换工作的核心接口**。

`Functor` 两个泛型 F 和 Mu 分别表示操作的容器类型和算子本身类型。

`Functor` 继承自 `Kind1`，而 `Kind1` 继承自 `App<Mu, F>`，`Kind1` 的子接口只有 `Functor`。`Kind1` 实现了一系列方法创建 `Product` 对象，可用于进一步的对象变换。
