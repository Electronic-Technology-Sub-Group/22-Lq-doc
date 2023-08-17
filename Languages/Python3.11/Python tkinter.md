# 组件

## 框架

框架组件为一个容纳其他组件的容器，包括 `Frame` 和 `LabelFrame` 两种
- `tkinter.ttk.Frame`：一个空容器
- `tkinter.ttk.LabelFrame`：一个带标签的容器，并有一圈线围起来
	- `text`：标签文本
	- `labelanchor`：显示标签位置，包括 `nesw` 的排列组合，遵循上北下南，左东右西
	- `labelwidget`：显示标签组件，默认是 Label，也可以是别的

框架内组件和布局是由具体组件决定的，每个 Widget 组件第一个参数需要传入父组件，一般接受 `tkinter.Tk` 对象表示根组件，或其他容器，表示组件所在容器

## 列表 Listbox

列表组件为 `tkinter.Listbox` 类
### 方法

### 属性

## 复选框 Checkbutton

复选框组件为 `tkinter.ttk.Checkbutton` 类
- `onvalue`：选中时的值，默认为 1
- `offvalue`：未选中时的值，默认为 0
- `command`：点击事件回调函数
- `variable`：变量绑定，可接受 `tkinter.XxxVar`，如 `tkinter.IntVar`

## 对话框

### messagebox

信息对话框，位于 `tkinter.messagebox` 包中

- 询问类，含有多个返回按钮，除 `askretrycancel` 图标为叹号，其余图标为问号
	- `askyesno`：是/否，返回 bool
	- `askquestion`：是/否，返回 `messagebox.YES` 或 `messagebox.NO`
	- `askokcancel`：确定/取消，返回 bool
	- `askyesnocancel`：是/否/取消，取消时返回 None，否则返回 bool
	- `askretrycancel`：重试/取消，返回为 bool，True 表示重试
- 信息类，图标不同，仅有一个按钮，返回值都为 `messagebox.OK`
	- `showwarning`：警告，图标为叹号
	- `showerror`：错误，图标为红叉
	- `showinfo`：信息，图标为蓝 i

参数：
- title：弹框标题
- message：显示信息
- options：其他选项

### filedialog

文件选择对话框，位于 `tkinter.filedialog` 包中
- `askopenfile`：选择文件并返回打开的 IO 流，取消则返回 None
- `askopenfiles`：可选择多个文件并返回打开的 IO 流列表
- `askopenfilename`：选择文件并返回路径字符串，取消则返回空字符串
- `askopenfilenames`：选择多个文件并返回字符串列表，取消则返回空字符串
- `askdirectory`：选择目录并返回路径，取消则返回空字符串
- `asksaveasfile`：保存文件对话框，返回打开的 IO 流，取消则返回 None
- `asksaveasfilename`：保存文件对话框，返回文件路径，取消则返回空字符串

参数：
- 打开对话框（`askopen...`，`askdirectory`）
	- `mustexist`：（目录）是否必须存在
- 保存对话框（`asksaveas...`）
	- `confirmoverwrite`：
- 通用
	- `mode`：模式字符串，默认 `'r'`
	- `defaultextension`：给定扩展名，若输入的文件没有扩展名时自动补充，带有 `.` 后缀
	- `filetype`：文件类型，`List[str, Union[str, List[str]]]`，第一个值为类型名称，第二个值为可选的扩展名
	- `initialdir`：初始化目录，默认是当前目录或上次打开的目录
	- `initialfile`：初始化文件
	- `title`：标题
	- `parent`：打开位置，默认在根窗口

### colorchooser

颜色选择对话框，位于 `tkinter.colorchooser` 包中，通过 `colorchooser.askcolor()` 打开

该窗口返回一个二元组
- 第一个值为包含三个数字的元组，分别为 RGB 值
- 第二个值为一个字符串，为 `#` 开头的十六进制颜色值字符串

参数
- `title`：标题
- `parent`：打开位置，默认在根窗口

# 事件

任何一个 `tkinter` 或 `tkinter.ttk` 中的组件都可以通过 `widget.bind('事件', 事件处理函数)` 绑定事件

## 事件名

tkinter 事件名格式为 `<modifier-type-detail>` 三部分
- modifier：事件修饰符，组合键（Alt，Shift）、双击（Double）等
- type：事件类型，键盘 Key，鼠标 Button，Motion，Enter，Leaves，Relase，其他如 Configure 等
- detail：具体细节，可省略

| 事件类型 | 事件格式             | 事件解释                                                                      |
| -------- | -------------------- | ----------------------------------------------------------------------------- |
| 鼠标事件 | `<Button-1>`         | 鼠标点击（1-左键，2-中键，3-右键）                                            |
|          | `<Double-Button-1>`  | 鼠标双击（1-左键，2-中键，3-右键）                                            |
|          | `<B1-Motion>`        | 鼠标拖动（1-左键，2-中键，3-右键）                                            |
|          | `<ButtonRelease-1>`  | 鼠标按下之后释放（1-左键，2-中键，3-右键）                                    |
|          | `<Enter>`            | 鼠标进入控件范围（widget），不是键盘按键                                      |
|          | `<Leave>`            | 鼠标离开控件范围（widget）                                                    |
| 键盘事件 | `<Key>`/`<KeyPress>` | 任意键盘按键（键值会以char的格式放入event对象）                               |
|          | `<Return>`           | 对应键盘按键                                                                  |
|          | `<Cancel>`           |                                                                               |
|          | `<BackSpace>`        |                                                                               |
|          | `<Tab>`              |                                                                               |
|          | `<Shift_L>`          |                                                                               |
|          | `<Control_L>`        |                                                                               |
|          | `<Alt_L>`            |                                                                               |
|          | `<Home>`             |                                                                               |
|          | `<Left>`             |                                                                               |
|          | `<Up>`               |                                                                               |
|          | `<Down>`             |                                                                               |
|          | `<Right>`            |                                                                               |
|          | `<Delete>`           |                                                                               |
|          | `<F1>`               |                                                                               |
|          | `<F2>`               |                                                                               |
| 组件事件 | `<Configure>`        | 如果widget的大小发生改变，新的大小（width和height）会打包到event发往handler。 |
|          | `<Activate>`         | 当组件从不可用变为可用                                                        |
|          | `<Deactivate>`       | 当组件从可用变为不可用                                                        |
|          | `<Destroy>`          | 当组件被销毁时                                                                |
|          | `<Expose>`           | 当组件从被遮挡状态变为暴露状态                                                |
|          | `<Map>`              | 当组件由隐藏状态变为显示状态                                                  |
|          | `<Unmap>`            | 当组件由显示状态变为隐藏状态                                                  |
|          | `<FocusIn>`          | 当组件获得焦点时                                                              |
|          | `<FocusOut>`         | 当组件失去焦点时                                                              |
|          | `<Property>`         | 当组件属性发生改变时                                                          |
|          | `<Visibility>`       | 当组件变为可视状态时                                                          | 

# 布局
