#java9 

通过 JShell API，可以模拟在 JShell 中执行一段代码的结果，代码片段是 String 字符串

|类|获取|作用|
| --------------| ------| ----------------------|
|JShell| `JShell.create()` |表示一个 JShell 会话|
|SnippetEvent| `JShell#eval` |一次执行事件|
|Snippet| `SnippetEvent#snippet` |一个代码片段|

```java
import jdk.jshell.*;

void main() {
    // JShell 表示一个会话
    try (JShell shell = JShell.create()) {
        for (SnippetEvent event : shell.eval("int i = 100;")) {
            // Snippet 表示一个代码片段
            Snippet snippet = event.snippet();
            // Snippet: Snippet:VariableKey(i)#1-int i = 100;
            System.out.println("Snippet: " + snippet);
            // Kind: VAR
            System.out.println("Kind: " + snippet.kind());
            // Sub-Kind: VAR_DECLARATION_WITH_INITIALIZER_SUBKIND
            System.out.println("Sub-Kind: " + snippet.subKind());
            // Previous Status: NONEXISTENT
            System.out.println("Previous Status: " + event.previousStatus());
            // Current Status: VALID
            System.out.println("Current Status: " + event.status());
            // Value: 100
            System.out.println("Value: " + event.value());
        }
    }
}
```
