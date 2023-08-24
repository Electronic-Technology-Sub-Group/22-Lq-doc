C# 类按照命名空间的逻辑组织，使用 `namespace` 声明，命名空间与文件的组织方式无关且可嵌套
   
```csharp
// S1.S2.S3.ClassName
namespace S1 {
    namespace S2 {
        namespace S3 {
            class ClassName {
                ...
            }
        }
    }
}

namespace S1.S2.S3 {
    class ClassName {
        ...
    }
}
```

使用 `using` 可声明命名空间的别名
   
```csharp
using AAA.BBB.CCC = ABC; // 可直接用 ABC 代替 AAA.BBB.CCC
```

可以使用 `using static` 引入静态成员
   
```csharp
using static System.Console;

// 相当于 System.Console.WriteLine("Hello World!")
WriteLine("Hello World!");    
```

C# 下常见命名空间有：
- System.Collections：·集合
- System.Data：访问数据库
- System.Diagnostics：诊断信息
- System.Globalization：全球化, 本地化
- System.IO：文件 IO
- System.Net：核心网络
- System.Threading：线程和任务
- System.Web：ASP.BET
- System.Windows：带有 WPF 的 Windows 桌面应用程序