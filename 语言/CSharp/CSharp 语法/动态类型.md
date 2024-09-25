# dynamic 类型

`dynamic` 类型：允许编写时期忽略编译期间类型检查代码，编译器假定 dynamic 类型的对象定义的任何操作都是有效的
# DLR

Dynamic Language Runtime，动态语言运行时，允许添加动态语言，并使 C# 具备这些动态语言相同的某些动态功能

通过 DLR ScriptRuntime 为应用程序添加脚本功能，使应用程序可以利用脚本完成工作。支持语言包括 IronPython，IronRuby 等，需要通过 NuGet 添加，下面例子以 Python 为例

1. 启动 ScriptRuntime 对象，设定环境的全局状态

```c#
var rt = ScriptRuntime.CreateFromConfiguration();
```

2. 设置合适的 ScriptEngine：完成执行脚本代码的工作；提供 ScriptSource 和 ScriptScope
	1. 创建 ScriptSource：允许访问脚本，表示脚本的源代码
	2. 创建 ScriptScope：名称空间。用于给脚本传入传出参数
	3. 设置变量
	4. 生成 dynamic 对象，手动调用

```c#
var rt = ScriptRuntime.CreateFromConfiguration();

var engine = rt.GetEngine("Python");
var source = engine.CreateScriptSourceFromFile("脚本文件");
var scope = engine.CreateScope();
scope.SetVariable("变量名", "变量值");
source.Execute(scope);
```

或

```c#
var rt = ScriptRuntime.CreateFromConfiguration();
dynamic pyObject = rt.UseFile("脚本文件");
```
# DynamicObject

从 DynamicObject 类派生，并复写某些方法，自定义动态对象
- `TryGetMember`：自定义属性 get 方法
- `TrySetMember`：自定义属性 set 方法
- `TryInvokeMember`：自定义方法调用

```csharp
public class WroxDynamicObject: DynamicObject
{
    private Dictionary<string, object> _dynamicData = new Dictionary<string, object>();
    
    public override bool TryGetMember(GetMemberBinder binder, out object result)
    {
        bool success = false;
        result = null;
        if (_dynamicData.ContainsKey(binder.Name))
        {
            result = _dynamicData[binder.Name];
            success = true;
        }
        else
        {
            result = "Property Not Found!";
            success = false;
        }
        return success;
    }
    
    public override bool TrySetMember(SetMemberBinder binder, object value)
    {
        _dynamicData[binder.Name] = value;
        return true;
    }
    
    public override bool TryInvokeMember(InvokeMemberBinder binder, object[] args, out object result)
    {
        dynamic method = _dynamicData[binder.Name];
        result = method(args);
        return result != null;
    }
}
```
# ExpandoObject

直接创建的动态对象
