向一个元素应用动画的复合属性（简写属性），

```CSS
div {
    animation: 动画名称 动画时间 插值器 延迟时间 重复次数 动画方向 结束状态
}
```

各属性之间的顺序可以不同，也不必要列出所有属性，但存在多个可以解析成时间的参数时，第一个被分配给*动画时间*，第二个被分配给*延迟时间*

多组不同动画使用 `,` 分隔；使用独立属性设置时，也可以以 `,` 分隔数值控制不同动画

应尽可能使用 `animation` 复合属性而不是各个分散的属性，以避免兼容性问题

- 动画名称：独立属性为[[animation-name]]，**必须指定**
- 动画时间：独立属性为[[animation-duration]]，**必须指定**
- 插值器：独立属性为[[animation-timing-function]]
- 延迟时间：独立属性为[[animation-delay]]
- 重复次数：独立属性为[[animation-iteration-count]]
- 动画方向：独立属性为[[animation-direction]]
- 结束状态：独立属性为[[animation-fill-mode]]

其他状态相关子属性
- 播放状态：独立属性为[[animation-play-state]]
- 时间轴：独立属性为[[animation-timeline]]