将本地仓库推送到远程仓库

```bash
git push [options] [远程仓库名 [本地分支名][:远程分支名]]
```

意为将 `本地分支名` 对应的分支推送到 `[远程仓库名]` 的 `[远程分支名]` 。

| 选项             | 简写 | 说明                                                                   |
| ---------------- | ---- | ---------------------------------------------------------------------- |
|                  | `-F` | 忽略远程分支与本地分支的冲突，强制推送到远程分支                       |
| `--set-upstream` |      | 关联本地分支与远程分支，之后可以省略远程仓库名，本地分支名和远程分支名 |
