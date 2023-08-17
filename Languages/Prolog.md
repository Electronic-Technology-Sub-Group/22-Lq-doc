Prolog 是一门声明式语言，通过提供事实和推论，由计算机作出推断。数据以逻辑形式存在，其基本构建单元包括：
- 事实：关于真实世界的断言（XXX是YYY，XXX喜欢YYY 等简单逻辑）
- 规则：关于真实世界中一些事实的推论（若 XXX 是 YYY，则 XXX 是 ZZZ 等推论）
- 查询：关于真实世界的一个问题（XXX 是 YYY 吗）

将外部文件载入解释器环境使用 `[]`

```prolog
["文件名"].
```

优势：
- 第一种用于自然语言识别的语言，可以通过自然语言，应用基于事实和推论的知识库表达复杂的不精确的语言
- 方便游戏中对玩家和怪物的建模，为不同敌人构筑不同行为
- 使用 RDF（Resource Description Language，资源描述语言）提供对资源的基本描述并创建为知识库
- AI
- 调度
劣势：
- 逻辑编程非通用编程
- 当策略需要大量计算时需要进行大量计算
# 基本语法

- 每一条语句使用 `.` 结束
- 使用 `%` 表示注释
- 若一个词以小写开头，表示一个符号（字面量，Ruby Symbol等）
- 若一个词以以大写或下划线开头，表示一个变量，且无须声明
## 事实

表示多个事物之间的关系

```prolog
关系(符号1, 符号2, 变量3, ...).
```
## 规则

表示多个条件的集合。一条规则被称为 `规则名/参数名`

```prolog
规则(变量1, 变量2, 变量3, ...) :- 条件集合
```

例：

```prolog
friend(X, Y) :- \+(X = Y), likes(X, Z), likes(Y, Z).
```

以上规则称为 `friend/2` 涉及 X Y Z 三个变量，需要传入 X Y 两个变量，有三个条件需要满足，其中后两个代表一条条件：
- 满足条件 X != Y
- 存在 Z，同时满足 likes(X, Z)，likes(Y, Z)

若存在多条同名规则（规则名、传入变量数目都相同），则表示或条件，几个规则中只要有一个满足即可，因此可以创建递归规则

```prolog
father(zeb, john_boy_sr).
father(john_boy_sr, john_boy_jr).

ancestor(X, Y) :- father(X, Y).
ancestor(X, Y) :- father(X, Z), ancestor(Z, Y).
```
- Y 是 X 的祖先 -- Y 是 X 的父亲，或 存在 X 的父亲 Z，Y 是 Z 的祖先

![[Pasted image 20230816171551.png]]
## 查询
### 判断

直接使用事实或规则内加符号，可获取 `yes` 或 `no` 的结果（true，false）

```prolog
likes(wallace, cheese).
likes(grommit, cheese).
likes(wendolene, sheep).

friend(X, Y) :- \+(X = Y), likes(X, Z), likes(Y, Z).
```

![[Pasted image 20230816105404.png]]
### 填空

直接使用事实或规则加符号，其中待查询的条件使用变量即可
- 多个结果使用 空格，Tab，`;` 等显示下一条，回车结束
- 当无其他满足条件的内容时，按情况显示 yes（true）或 no（false）
	- yes：在剩余部分中没有其他可选项
	- no：在未经更多计算的情况下不能立刻判断是否还有更多可选项

```prolog
food_type(velveeta, cheese).
food_type(ritz, cracker).
food_type(spam, meat).
food_type(sausage, meat).
food_type(jolt, soda).
food_type(twinkie, dessert).

flavor(sweet, dessert).
flavor(sweet, soda).
flavor(savory, meat).
flavor(savory, cheese).

food_flavor(X, Y) :- food_type(X, Z), flavor(Y, Z).
```

![[Pasted image 20230816110919.png]]
## 合一

合一即 `=` 运算符。该运算符与通常的编程语言不同，更类似于其他语言的 `==` 运算符。

```prolog
dorothy(X, Y, Z) :- X = lion, Y = tiger, Z = bear.
```

![[Pasted image 20230816113123.png]]
`_` 可以与任何值合一

![[Pasted image 20230816172345.png]]

而相较于其他语言的 `=`，在 Prolog 使用 `变量 is 值`
# 容器

Prolog 容器主要是列表和元组，
- 列表：变长容器，使用 `[]` 创建，如 `[1, 2, 3]`
- 元组：定长容器，使用 `()` 创建，如 `(1, 2, 3)`
## 容器合一

两个容器的合一条件为：
- 容器类型必须相同，列表只能和列表合一，元组只能和元组合一
- 容器内元素个数相同
- 容器内元素可以合一，且顺序相同

![[Pasted image 20230816173257.png]]

- 合一可以使用变量，且当且仅当相同值可以使用相同变量

![[Pasted image 20230816173648.png]]

- 列表合一可以使用 `|` 解构，`|` 前的变量将取前 n 个元素，剩余元素将被归为 `|` 后的变量，直到空列表

![[Pasted image 20230817000218.png]]
## 数学计算

Prolog 使用 `变量 is 值` 赋值，再配合递归实现循环

```prolog
count(0, []).
count(Count, [Head|Tail]) :- count(TailCount, Tail), Count is TailCount + 1.

sum(0, []).
sum(Total, [Head|Tail]) :- sum(Sum, Tail), Total is Sum + Head.

average(Average, List) :- sum(Sum, List), count(Count, List), Average is Sum / Count.
```
- count：计算列表长度：`[]` 为 0，每次解构个数 +1
- sum：计算元素和：`[]` 为 0，每次解构加 Head
- average：计算平均数：查询 sum 与 count，之后相除

![[Pasted image 20230817001700.png]]
## append

Prolog 内置列表的 `append` 规则，该规则接收 3 个列表变量，检查第三个变量是否为前两个变量组合的结果。

通过正向和反向使用该规则，可以实现以下功能：
- 组合检查
![[Pasted image 20230817003513.png]]

- 拼接列表
![[Pasted image 20230817003524.png]]

- 列表减法
![[Pasted image 20230817003553.png]]

- 子序列
![[Pasted image 20230817003620.png]]

要实现这样的功能也不难：
- 若 A 为 `[]`，检查 B = C 即可
- 析构 A 与 C，判断 Head 是否相等，递归检查剩余列表

```prolog
concatenate([], List, List).
concatenate([Head|Tail], List, [Head|Tail2]) :-
  concatenate(Tail, List, Tail2).
```