类似于 Java，C# 的泛型也以 `<>` 包围。但 C# 的泛型类型是真泛型，编译器为每个泛型生成对应类，而不是 Java 泛型擦除实现。
- 泛型类的静态成员只在相同泛型的类中共享
# 命名约定

- 以 T 做前缀
- 没有特殊要求，泛型允许用任何类型替代，且只有一个泛型类型，可用 T 作为泛型类型名称
- 泛型类型有特殊要求，或有至少两个泛型类型，就应给泛型类型使用描述性名称
# 默认值与空类型

由于泛型可以为值类型，所以不能传入 null，而是使用 default(T) 作为泛型的空类型

可空类型 `T?` 的泛型使用 `Nullable<T>` 结构体
# 协变与抗变

- 协变：参数类型是协变的，泛型类型用 out 修饰，即协变的
- 抗变：返回值是抗变的，泛型类型用  in 修饰，即抗变的
# 泛型约束

若泛型类需要调用泛型类型的方法，必须在 类 或者 方法的声明末尾使用泛型约束：`<T> where T: 约束1, 约束2, 约束3....`

| 约束              | 说明                        |
|:---------------:|:-------------------------:|
| where T: struct | 结构约束，类型 T 必须是值类型          |
| where T: class  | 类约束，类型 T 必须为引用类型          |
| where T: IFoo   | 指定类型 T 必须实现 IFoo 接口       |
| where T: Foo    | 指定类型 T 必须派生自基类 Foo        |
| where T: new()  | 构造函数约束，指定类型 T 必须有一个默认构造函数 |
| where T1: T2    | 类型 T1 派生自泛型类型 T2          |

```csharp
public interface IDocument {
    string Title { get; set; }
    string Content { get; set; }
}

public class DocumentManager<TDocument> where TDocument: IDocument {

    private readonly Queue<TDocument> documentQueue = new Queue<TDocument> 

    public void DisplayAllDocuments() {
        foreach (TDocument doc in documentQueue) {
            Console.WriteLine(((IDocument)doc).Title)
        }
    }
}
```