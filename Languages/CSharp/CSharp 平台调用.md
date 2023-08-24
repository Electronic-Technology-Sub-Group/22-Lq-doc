# 获取平台方法

在 VS 开发者命令行中提供了一个 dumpbin 工具可导出一个 `dll` 的方法

```bash
dumpbin /exports c:\windows\system32\kernel32.dll | more
```
# Win32 类型

Win32API 类型使用 Hungarian 表示法

| 缀    | 类    型                                   |
| ----- | ------------------------------------------ |
| sz    | 指向一个以零字符结尾的字符串中的第一个字符 | 
| str   | 字符串                                     |
| i     | int                                        |
| n     | 数或int                                    |
| ui    | Unsigned int                               |
| c     | char                                       |
| w     | WORD（unsigned short）                     |
| dw    | DWORD（unsigned long）                     |
| fn    | 函数指针（function pointer）               |
| d     | Double                                     |
| by    | byte                                       |
| l     | long                                       |
| p     | pointer                                    |
| lp    | long pointer                               |
| lpstr | 指向字符串的long pointer                   |
| h     | 句柄（handle）                             |
| m_    | 类成员（class member）                     |
| g_    | 全局型（global type）                      |
| hwnd  | 窗口的句柄（Window handle）                |
| hdc   | Windows设备上下文（device context）的句柄  |
# 平台调用

例：在 MSDN 上查到的 `CreateHardLinkW` 方法的声明如下：
```c
BOOL CreateHardLinkW(
  [in] LPCWSTR               lpFileName,
  [in] LPCWSTR               lpExistingFileName,
       LPSECURITY_ATTRIBUTES lpSecurityAttributes
);
```

则在使用时可以这样用：
- Windows 句柄：32 或 64 位值。根据句柄类型和安全类型，不允许一些值
	- .Net 1.0：IntPtr
	- .Net 2.0：SafeHandle 类型，可通过 `Microsoft.Win32.SafeHandles` 获取

```csharp
[SecurityCritical]
internal static class NativeMethods
{
	// 声明 CreateHardLinkW 原型和位置
    [DllImport("kernel32.dll",
        SetLastError = true,
        EntryPoint = "CreateHardLinkW",
        CharSet = CharSet.Unicode)]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool CreateHardLink(
        [In, MarshalAs(UnmanagedType.LPWStr)] string newFileName,
        [In, MarshalAs(UnmanagedType.LPWStr)] string existingFileName,
        IntPtr securityAttributes);

    // 调用 CreateHardLink 方法并进行异常检查
    internal static void CreateHardLink(string oldFileName, string newFileName)
    {
	    
        if (!CreateHardLink(newFileName, oldFileName, IntPtr.Zero))
        {
            var ex = new Win32Exception(Marshal.GetLastWin32Error());
            throw new IOException(ex.Message, ex);
        }
    }
}

public static class FileUtility
{
    [FileIOPermission(SecurityAction.LinkDemand, Unrestricted = true)]
    public static void CreateHardLink(string oldNameFile, string newFileName)
    {
        NativeMethods.CreateHardLink(oldNameFile, newFileName);
    }
}
```
