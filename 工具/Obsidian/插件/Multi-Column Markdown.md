````
Text displayed above.

--- start-multi-column: ExampleRegion1  
```column-settings  
number of columns: 2  
largest column: left  
```

Text displayed in column 1.

--- end-column ---

Text displayed in column 2.

--- end-multi-column

Text displayed below.
````

# 起止点标记

多列块起始标记：

![[../../../_resources/images/20241001_214_msedge.png]]

单列结束标记：

![[../../../_resources/images/20241001_216_msedge.png]]

多列结束标记：

![[../../../_resources/images/20241001_217_msedge.png]]

# 设置标记

```column-settings
number of columns: 2
```

![[../../../_resources/images/20241001_215_msedge.png]]

| 参数名                                                                        | 参数值                                                                                                                                                  | 说明                |
| -------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- |
| `Number of Columns`<br>`Num of Cols`<br>`Col Count`                        | 数字                                                                                                                                                   | 列数                |
| `Column Size`                                                              | `Small` / `Medium` / `Large` / `Full`<br>`Standard` / `Left` / `Center` / `Middle` / `Right`<br>`First` / `Third` / `Second`<br>由 CSS 尺寸组成的数组（支持百分比） | 列宽，默认每列宽度相同       |
| `Auto Layout`<br>`Fluid Columns`<br>`Fluid Cols`                           | `true` / `on`                                                                                                                                        | 自动布局，尝试平衡列之间的内容宽度 |
| `Border`                                                                   | `disabled` / `off` / `on` / `false`                                                                                                                  | 边框                |
| `Shadow`                                                                   | `disabled` / `off` / `on` / `false`                                                                                                                  | 阴影                |
| `Col Position`<br>`Col Location`<br>`Column Position`<br>`Column Location` | `Left`<br>`Center` / `Middle`<br>`Right`                                                                                                             | 列位置               |
| `Column Spacing`                                                           | CSS 尺寸或数组，默认 `pt`                                                                                                                                    | 列间距               |
| `Overflow`                                                                 | `Scroll`<br>`Hidden`                                                                                                                                 | 内容溢出，默认 `Scroll`  |
| `Alignment`                                                                | `Left` / `Center` / `Right`                                                                                                                          | 对齐方式              |
| `Align Tables to Text Alignment`                                           | `true` / `false`<br>`on` / `off`<br>`enabled` / ` disabled `                                                                                         | 将表格与文本对齐方式对其      |
|                                                                            |                                                                                                                                                      |                   |

# 参考

```cardlink
url: https://github.com/ckRobinson/multi-column-markdown
title: "GitHub - ckRobinson/multi-column-markdown: A plugin for the Obsidian markdown note application, adding functionality to render markdown documents with multiple columns of text."
description: "A plugin for the Obsidian markdown note application, adding functionality to render markdown documents with multiple columns of text. - ckRobinson/multi-column-markdown"
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/35a4b4b4ba4b481c528dec9b8b62f52c4c56ee94fd0b5f129aceaa65ab0d97f1/ckRobinson/multi-column-markdown
```
