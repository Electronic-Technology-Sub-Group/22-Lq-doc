ReactiveX 是一个响应式库

# RxJava3

`RxJava` 是 ReactiveX 在 JVM 平台上的一个实现，支持包括 Java 在内的多种 JVM 语言，如 Groovy，Kotlin，Scala，Clojure，JRuby 等

依赖：`io.reactivex.rxjava3:rxjava`，当前版本 3.1.8，WIKI 版本 3.0.4

```java
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    void run() {
        hello("Ben", "George");
    }

    void hello(String... args) {
        Flowable.fromArray(args).subscribe(s -> System.out.printf("Hello, %s!\n", s));
    }
}
```

 [How To Use RxJava · ReactiveX/RxJava Wiki (github.com)](https://github.com/ReactiveX/RxJava/wiki/How-To-Use-RxJava)

# 其他平台实现

在不同平台有不同实现：

```cardlink
url: https://reactivex.io/intro.html
title: "ReactiveX - Intro"
host: reactivex.io
```

* Java: RxJava
* JavaScript: [RxJS](https://github.com/ReactiveX/rxjs)
* C#: [Rx.NET](https://github.com/Reactive-Extensions/Rx.NET)
* C#(Unity): [UniRx](https://github.com/neuecc/UniRx)
* Scala: [RxScala](https://github.com/ReactiveX/RxScala)
* Clojure: [RxClojure](https://github.com/ReactiveX/RxClojure)
* C++: [RxCpp](https://github.com/Reactive-Extensions/RxCpp)
* Lua: [RxLua](https://github.com/bjornbytes/RxLua)
* Ruby: [Rx.rb](https://github.com/Reactive-Extensions/Rx.rb)
* Python: [RxPY](https://github.com/ReactiveX/RxPY)
* Go: [RxGo](https://github.com/ReactiveX/RxGo)
* Groovy: [RxGroovy](https://github.com/ReactiveX/RxGroovy)
* JRuby: [RxJRuby](https://github.com/ReactiveX/RxJRuby)
* Kotlin: [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* Swift: [RxSwift](https://github.com/kzaher/RxSwift)
* PHP: [RxPHP](https://github.com/ReactiveX/RxPHP)
* Elixir: [reaxive](https://github.com/alfert/reaxive)
* Dart: [RxDart](https://github.com/ReactiveX/rxdart)

* Netty： [RxNetty](https://github.com/ReactiveX/RxNetty)
* Android： [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* Cocoa： [RxCocoa](https://github.com/kzaher/RxSwift)
