列出从当前仓库提交开始，可以追溯到的所有父提交。

```bash
git log [options]
```

- options

| 参数                                     | 简写 | 说明                                  |
| ---------------------------------------- | ---- | ------------------------------------- |
| `--all`                                  |      | 显示所有提交                          |
| `--pretty=<format>`, `--format=<format>` |      | 信息显示方式                          |
| `--abbrev=<n>`                           |      | 不完全显示 40 位标识符，只显示前 n 位 |
| `--abbrev-commit`                        |      | 根据 `--abbrev` 设置显示提交的标识符  |
| `--graph`                                |      | 以图的形式显示提交记录                | 

## `<format>`

格式化方式
-   _oneline_：`<hash> <title-line>`
-   _short_：
```
commit <hash>
Author: <author>

<title-line>
```

-   _medium_
```
commit <hash>
Author: <author>
Date:   <author-date>

<title-line>

<full-commit-message>
```

-   _full_
```
commit <hash>
Author: <author>
Commit: <committer>

<title-line>

<full-commit-message>
```
