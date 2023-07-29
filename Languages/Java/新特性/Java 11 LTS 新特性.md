[JDK11新特性解读 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/45412682)

# 语法

## Lambda Var

允许在 lambda 表达式中使用 `var` 类型推断

```java
// interface: (int, int) -> void
(var x, var y) -> { /*...*/ }
```

# API

## HTTP Client

`java.net.http` 模块。API 上有点类似于 Apache Http Client

## Curve25519 及 Curve448 密钥协议

新接口 `XECPublicKey` 和 `XECPrivateKey` 接口

```java
public void curve25519()   
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException {  
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("XDH");  
    NamedParameterSpec paramSpec = new NamedParameterSpec("X25519"); // curve448: X448  
    kpg.initialize(paramSpec);  
    KeyPair kp = kpg.generateKeyPair();  
    // 公钥  
    KeyFactory kf = KeyFactory.getInstance("XDH");  
    BigInteger u = ...; // u  
    XECPublicKeySpec pubSpec = new XECPublicKeySpec(paramSpec, u);  
    PublicKey pubKey = kf.generatePublic(pubSpec);  
      
    // 私钥  
    KeyAgreement ka = KeyAgreement.getInstance("XDH");  
    ka.init(kp.getPrivate());  
    ka.doPhase(pubKey, true);  
    byte[] secret = ka.generateSecret();  
}
```

## 移除某些已弃用内容

- CORBA
- JavaEE：JAX-WS，JAXB，JAF，Common Annotations，通过 maven 仓库重新引用
- Nashorn 引擎
- Pack200 Tools

# 运行单一 java 文件

JDK 11 现允许直接运行单一 Java 文件程序

```bash
java HelloWorld.java
```

至此，Java 启动器支持以下四种方法启动：
- 单一 class 文件
- Jar 包中包含 main 方法的类
- 模块中包含 main 方法的类
- 单一 java 文件

# 其他

- Epsilon 垃圾收集器：什么也不做的垃圾收集器（包括 `System.gc()`），堆内存用完后直接退出，通过 `-XX:+UseEpsilonGC` 参数开启
	- 测试：性能测试，内存压力测试，VM 接口测试
	- 微服务等非常短小的 JOB 任务
	- Last-drop 延迟 & 吞吐改进
- ZGC 垃圾收集器：实验性功能，并发、压缩性、基于 region 的高效率垃圾收集器，暂停时间不会超过 10ms，通过 `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC` 启用
	- 于 [[Java 12-16 新特性#Java 15#其他]] 转正
- 基于嵌套的访问控制

> JEP181 Nest-Based Access Control，多个类同属于同一个代码块，但分散编译成多个 `class` 文件时，彼此访问各自私有成员时可直接访问（反射）

- 动态文件常量

> JEP309 Dynamic Class-File Constants，`class` 文件支持新的常量池格式 `CONSTANTDyanmic`，会将创建方法委托给 `bootstrap` 方法

- ChaCha20 与 Poly1305 加密算法
- 支持 Unicode 10
- 支持 TLS 1.3
- 飞行记录器：Flight Recorder，低开销的 Java 应用排错和 JVM 问题数据收集框架
