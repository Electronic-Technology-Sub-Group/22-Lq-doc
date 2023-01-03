决定 CSS 在执行之前和执行之后如何将属性应用于目标
- none：不播放动画时（包括播放完成时），取消动画的 CSS，还原成元素本身的属性，默认
- forwards：保留动画播放完成时的状态（根据 direction 和 iteration count，可能是 0% 或 100% 帧）
- backwards：应用动画时和播放完毕立即应用动画的额第一帧，并在整个 delay 时间段维持
- both：同时遵循 forwards 和 backwards