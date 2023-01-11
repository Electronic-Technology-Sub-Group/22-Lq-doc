以 `#` 开头的当前行剩余部分均为注释内容，通常来说注释内容不会影响到文档的内容。但有例外：

- `shebang`：若脚本作为 Unix 可执行脚本使用，则第一行应当以以下内容开头：

```Python
#!/usr/bin/env python3
```

该行注释指定了 Python 解释器的地址。为了与 Python 2 解释器加以区分，Python 3 的解释器名为 `python3`

- 默认情况下，Python 脚本使用 UTF-8 文件编码。若使用了其他编码，应当在第一行增加以下注释；若存在 `shebang` 注释，则应当在第二行

```python
# -*- coding: encoding -*-
```

其中，`encoding` 为 Python 支持的任何一种编码，如 Windows-1252 编码为 `cp1252`
