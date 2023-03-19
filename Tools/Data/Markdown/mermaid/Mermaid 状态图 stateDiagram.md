状态图通过 `stateDiagram` 或 `stateDiagram-v2` 创建，语法类似 `plantUml`
- `stateDiagram`
```mermaid
stateDiagram
[*] --> Still
Still --> [*]

Still --> Moving
Moving --> Still
Moving --> Crash
Crash --> [*]
```
- `stateDiagram-v2`
```mermaid
stateDiagram-v2
[*] --> Still
Still --> [*]

Still --> Moving
Moving --> Still
Moving --> Crash
Crash --> [*]
```
渲染效果基本相同
# 方向

方向使用 `direction [方向]`，详见[[Mermaid 流程图 flowchat#方向]]
# 状态

状态定义可以有以下三种方法：
- `stateId`：当状态 id 与显示内容相同时，直接声明即可
- `state [描述信息] as [stateId]`，描述信息需要用 `""` 包围
- `[stateId]: [描述信息]`，此处描述信息不需要引号
- `[*]`：开始与结束，取决于位于箭头前还是箭头后
```mermaid
stateDiagram-v2
direction LR
State1
s2: State 2
state "State 3" as s3
[*] --> State1
State1 --> s2
s2 --> s3
s3 --> [*]
```
# 箭头

箭头表示状态的转移，使用 `状态1 --> 状态2 [: 信息]`
```mermaid
stateDiagram-v2
[*] --> First: Begin
```
# 复合状态

复合状态指在一个状态中包含多个子状态，复合状态允许嵌套
```
state 复合状态名 {
  ...
}
```
```mermaid
stateDiagram
[*] --> First
state First {
    [*] --> Second
    state Second {
        [*] --> InnerSecond
        InnerSecond --> Third
        state Third {
            [*] --> InnerThird
            InnerThird --> [*]
        }
    }
}
First --> [*]
```
# 判断

使用 `state 节点名 <<choice>>` 创建选择节点
```mermaid
stateDiagram-v2
state if_state <<choice>>
[*] --> IsPositive
IsPositive --> if_state
if_state --> False: if n < 0
if_state --> True: if n > 0
False --> [*]
True --> [*]
```
# 分支

使用 `state [节点名] <<fork>>` 创建分支节点，`state [节点名] <<join>>` 创建合并节点
```mermaid
stateDiagram-v2

state fork_state <<fork>>
[*] --> fork_state
fork_state --> State2
fork_state --> State3

state join_state <<join>>
State2 --> join_state
State3 --> join_state
join_state --> State4
State4 --> [*]
```
# 并行

使用 `--` 单独一行表示几个并行的任务
```mermaid
stateDiagram-v2
    [*] --> Active

    state Active {
        [*] --> NumLockOff
        NumLockOff --> NumLockOn : EvNumLockPressed
        NumLockOn --> NumLockOff : EvNumLockPressed
        --
        [*] --> CapsLockOff
        CapsLockOff --> CapsLockOn : EvCapsLockPressed
        CapsLockOn --> CapsLockOff : EvCapsLockPressed
        --
        [*] --> ScrollLockOff
        ScrollLockOff --> ScrollLockOn : EvScrollLockPressed
        ScrollLockOn --> ScrollLockOff : EvScrollLockPressed
    }
```
# 笔记

为某个节点添加笔记信息：
```
note [left/right] of [节点名]
信息...
end note
```
```mermaid
stateDiagram
StateA
note left of StateA
    Important information!
    You can write notes...
end note
```
# 样式

自定义样式同样使用 [[Mermaid 流程图 flowchat#自定义样式#节点]] 实现