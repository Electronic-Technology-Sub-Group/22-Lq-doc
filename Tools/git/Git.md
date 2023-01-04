[Git](https://git-scm.com)

# 版本控制

## 分类

- 集中式版本控制：SVN
- 分布式版本控制：git

## 使用场景

- 备份与回退
- 协同开发
- 分制管理

优点：
- 速度快，存储文件变更
- 设计简单
- 允许非线性开发，可同时存在多分支并方便合并到主分支
- 分布式存储，去中心化
- 提交记录
- 管理高效

# 设置

git 的设置分为全局设置和局部设置，使用 [[git config]] 命令修改。全局设置影响当前计算机全局的默认设置，局部设置仅影响当前目录所在的仓库的设置。

## 用户设置

修改 `user` 属性，确定提交者的身份：

```bash
git config --global user.name "用户名"
git config --global user.email "邮箱"
```

## 命令行设置

### 命令别名

*对于 cmd，可使用 doskey 设置*

| 别名    | 命令                                                   |
| ------- | ------------------------------------------------------ |
| git-log | git log --pretty=oneline --all --graph --abbrev-commit |

### GitBash 中文乱码

```bash
git config --golbal core.quotepath false
```

```
# ${git_home}/etc/bash.bashrc 文件最后：
exprt LANG="zh_cn.UTF-8"
exprt LC_ALL="zh_cn.UTF-8"
```

# 使用方法

![[Pasted image 20221213005607.png]]

- [[基本操作]]包括 git 仓库的创建和文件提交操作，以及在不同提交记录之间切换的方法
- [[分支操作]]包括分支的创建、切换、合并，冲突解决等内容
- [[远程仓库]]
- 其他应用
	- [[Git 仓库服务器端搭建]]：描述如何在一个远程服务器中创建一个远程仓库
	- [[Github SSH 登录]]