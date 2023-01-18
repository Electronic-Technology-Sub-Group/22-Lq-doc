[Mermaid | Diagramming and charting tool](https://mermaid.js.org/)

`Mermaid` 是一个基于 JavaScript 的图表绘制工具，最终编译成 HTML，可在 Markdown 中使用 `mermaid` 类型的代码块表示，可通过 https://mermaid.live/ 在线可视化编写

*Obsidian 支持不太好，详见 渲染异常 tag*

# 注释

mermaid 注释使用 `%%` 开头，包含之后的一整行

```mermaid
graph LR
%% 这是一行注释
A --> B %% 从 A 指向 B
```
# 图

- [[Mermaid 流程图 flowchat]]
- [[Mermaid 时序图 sequenceDiagram]]
- [[Mermaid 类图 classDiagram]] #未完成 ：[Class diagrams | Mermaid](https://mermaid.js.org/syntax/classDiagram.html)

# 配置与扩展

暂未发现在 Obsidian 中设置 JS 和 CSS 的方法，以后如果研究了 Obsidian 插件和主题看看有没有相关内容吧。

## flowchart

### CLI 配置

```
mermaid.flowchartConfig = {
    width:100%
}
```

### CSS 样式

可通过 CSS 直接创建对应的样式类

```CSS
.className > rect {
    // 样式集
}
```

## sequenceDiagram

### CSS 类

| Class        | Description                                                 |
| ------------ | ----------------------------------------------------------- |
| actor        | Style for the actor box at the top of the diagram.          |
| text.actor   | Styles for text in the actor box at the top of the diagram. |
| actor-line   | The vertical line for an actor.                             |
| messageLine0 | Styles for the solid message line.                          |
| messageLine1 | Styles for the dotted message line.                         |
| messageText  | Defines styles for the text on the message arrows.          |
| labelBox     | Defines styles label to left in a loop.                     |
| labelText    | Styles for the text in label for loops.                     |
| loopText     | Styles for the text in the loop box.                        |
| loopLine     | Defines styles for the lines in the loop box.               |
| note         | Styles for the note box.                                    |
| noteText     | Styles for the text on in the note boxes.                   |

### CLI 配置
```
mermaid.sequenceConfig = { ... }
```

可用配置包括：
| Parameter         | Description                                                                                                                                | Default value                  |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------ |
| mirrorActors      | Turns on/off the rendering of actors below the diagram as well as above it                                                                 | false                          |
| bottomMarginAdj   | Adjusts how far down the graph ended. Wide borders styles with css could generate unwanted clipping which is why this config param exists. | 1                              |
| actorFontSize     | Sets the font size for the actor's description                                                                                             | 14                             |
| actorFontFamily   | Sets the font family for the actor's description                                                                                           | "Open Sans", sans-serif        |
| actorFontWeight   | Sets the font weight for the actor's description                                                                                           | "Open Sans", sans-serif        |
| noteFontSize      | Sets the font size for actor-attached notes                                                                                                | 14                             |
| noteFontFamily    | Sets the font family for actor-attached notes                                                                                              | "trebuchet ms", verdana, arial |
| noteFontWeight    | Sets the font weight for actor-attached notes                                                                                              | "trebuchet ms", verdana, arial |
| noteAlign         | Sets the text alignment for text in actor-attached notes                                                                                   | center                         |
| messageFontSize   | Sets the font size for actor<->actor messages                                                                                              | 16                             | 
| messageFontFamily | Sets the font family for actor<->actor messages                                                                                            | "trebuchet ms", verdana, arial |
| messageFontWeight | Sets the font weight for actor<->actor messages                                                                                            | "trebuchet ms", verdana, arial |

### JS 配置
```javascript
mermaid.initialize({
    sequence:{
        showSequenceNumbers: true // 开启消息自动编号
    }
})
```