---
icon: SiObsidian
---
提供多标签页视图和四象限视图
- 使用 `minitabs` 类型代码块创建
- 代码块中第一行表示视图类型，包括 `tabs`，`tabsButtom` 和 `fourQuadrant`
- 使用 `===` 或 `---` 分割多个标签页或象限，但两种标记不能混用

````
```minitabs
页面类型
标签...
```
````
# 多标签页视图

多标签页使用 `tabs` 或 `tabsButtom`，下一行开始使用 \`\` 包围标签字符串，不同标签之间使用空格分隔

- 标签在上方

> [!col]
>> [!col-md]
>> `````
>> ````minitabs
>> tabs
>> `页1` `页2` `页3`
>> ===
>> # 这是第一页
>> ===
>> > [!note]
>> > 这是第二页
>> ===
>> ```
>> 这是第三页
>> ```
>> ````
>> `````
>
>> [!col-md]
>> `````
>> ````minitabs
>> tabs
>> `页1` `页2` `页3`
>> ---
>> # 这是第一页
>> ---
>> > [!note]
>> > 这是第二页
>> ---
>> ```
>> 这是第三页
>> ```
>> ````
>> `````

![[Obsidian_20240719_132.gif]]
- 标签在下方

> [!col]
>> [!col-md]
>> `````
>> ````minitabs
>> tabsBottom
>> `页1` `页2` `页3`
>> ===
>> # 这是第一页
>> ===
>> > [!note]
>> > 这是第二页
>> ===
>> ```
>> 这是第三页
>> ```
>> ````
>> `````
>
>> [!col-md]
>> `````
>> ````minitabs
>> tabsBottom
>> `页1` `页2` `页3`
>> ---
>> # 这是第一页
>> ---
>> > [!note]
>> > 这是第二页
>> ---
>> ```
>> 这是第三页
>> ```
>> ````
>> `````

![[Obsidian_20240719_134.gif]]
# 四象限视图

四象限视图使用 `fourQuadrant`

````
```minitabs
fourQuadrant
===
# 第一象限
===
# 第二象限
===
# 第三象限
===
# 第四象限
```
````

![[Pasted image 20240719180103.png]]
# 参考

```cardlink
url: https://github.com/ssjy1919/Obsidian-minitabs
title: "GitHub - ssjy1919/Obsidian-minitabs: Obsidian tabs"
description: "Obsidian tabs. Contribute to ssjy1919/Obsidian-minitabs development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/0492609902be4e016c55da80fe8853ae637a06ba4478ad3b73f874593e2469e4/ssjy1919/Obsidian-minitabs
```
