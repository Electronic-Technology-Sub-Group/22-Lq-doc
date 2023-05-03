这里，我们将通过将 Vue 作为 js 引入一个网页，体验 Vue App 的功能

# 导入

Vue 可以作为一个脚本运行在网页中，而不必要使用完整的一整套 Vue/cli 脚手架服务。此时，Vue 仅用于控制当前网页中的某些控件使用

1. 引入 Vue。可以直接从 CDN 导入

```html
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
```

2. 创建 Vue APP

```javascript
const vue = Vue.createApp({})
```

3. 挂载控件，使 Vue 可以监视对应控件的状态

```javascript
vue.mount('selector') // 以选择器形式传入控件
```

此时，页面不会有任何变化，但可以通过 Vue 的调试插件看到 Vue 已经接管了我们选择的元素并将其作为应用的根节点

![[Pasted image 20230430150723.png]]

# 模板

在加载成为 Vue App 的 HTML 文档中（即之前选择器选择的部分），使用 `{{ ... }}` 包围的部分被 Vue 解析成模板，Vue 会执行其中的 JS 表达式并将结果输出到所在位置

我们在 Vue App 创建时传入了一个对象，那个对象的 `data()` 函数将返回一个对象作为上下文，在模板中的表达式可以直接访问

```javascript
const vue = Vue.createApp({
    data() {
        return {
            firstName: "Join"
        }
    }
})
```

```HTML
<!-- it will become <div>Join</div> -->
<div>{{ firstName }}</div>
```

## 数据驱动

在之前我们将来自 `data` 对象的属性映射到了 HTML 模板中。当我们需要修改其中的值时，可以直接修改对应的变量。Vue 可以自动将数据同步到对应的位置

```javascript
// vm: view model
const vm = Vue.createApp({
    data() {
        return {
            firstName: "Join"
        }
    }
})

vm.firstName = 'Bob'
```

Vue 通过创建与变量同名的 getter 和 setter 函数，代理了 `$data` 属性的数据（`$data` 即我们的 `data()` 返回的对象）。通过代理，Vue 可以即时响应变量的变化并更新页面值。

## 函数

在创建 App 的对象中增加一个 `methods` 属性，其值为一个对象，该对象内部的所有函数均能在模板中直接调用

通常，`methods` 属性中的函数可以使用 `this` 访问到 Vue 代理的变量，但箭头函数不行 -- 箭头函数使用独立的 this，无法访问到 `data` 创建的变量

```javascript
const vm = Vue.createApp({
    data() {
        return {
            firstName: "Join",
            lastName: "Bob"
        }
    },
    methods: {
	    fullName() {
		    return `${this.firstName} ${this.lastName}`
	    }
    }
})
```

```html
<div> {{ fullName() }} </div>
```

## 计算属性

使用方法计算属性时会有一些问题
- 每次只要有一个绑定数值发生变化，整个模板都会重新更新一遍，也就会调用一遍

要解决这个问题，可以将其声明为计算属性。在 `data` 对象中增加 `computed` 对象，之后在其中声明用于计算值的函数。在模板中，只写函数名，不写括号

```Javascript
const vm = Vue.createApp({
    data() {
        return {
            firstName: "Join",
            lastName: "Bob"
        }
    },
    computed: {
	    fullName() {
		    return `${this.firstName} ${this.lastName}`
	    }
    }
})
```

```HTML
<div> {{ fullName }} </div>
```

此时，计算属性将被缓存 

## 监听

在 `data` 对象中增加 `watch` 对象，其成员是一系列函数，键为属性名，函数为 `function(newVal, oldVal)`：

```javascript
const vm = Vue.createApp({
    data() {
        return {
            firstName: "Join",
            lastName: "Bob"
        }
    },
    watch: {
	    firstName(newValue, oldValue) { /* on changed */ }
    }
})
```

## 指令

指令是应用于 HTML 标签上的一系列以 `v` 开头的属性

通常，如果支持接受一个字符串的指令，接受的都是一个 JS 表达式，都可以直接访问 `data` 对象

### 加载前

`v-cloak` 指令将于 Vue 创建完成 App 后被移除，利用这些属性可以在 Vue 加载完成前为模板做一些动作 - 如 隐藏

```HTML
<div v-cloak></div>
```

```CSS
[v-cloak] {
	display: none
}
```

### 双向绑定

使用 `v-model` 指令可以完成数据和给定 HTML 元素的双向绑定 - HTML 元素，通常是 `<input>` 元素的值变化时，对应的变量也会跟着变化。同时，与该变量绑定的其他值也会发生变化

```html
<!-- 该输入框的值将与 firstName 变量始终同步，包括 JS 中的值 -->
<input type='text' v-model='firstName'>

<div> {{ firstName }}</div>
```

`v-model` 有一系列绑定修饰符，语法为 `v-model.modifier="..."`

| 修饰符 | 说明                             |
| ------ | -------------------------------- |
| number | 将返回值转换成数字               |
| trim   | 执行 `trim()` 函数去除空格       |
| lazy   | 在更新时刷新，默认每次变更都刷新 | 

### 属性绑定

 HTML 元素的属性也可以被绑定，通过 `v-bind:属性="变量名"` 指令

```javascript
const vm = Vue.createApp({
    data() {
        return {
            url: "wwww.baidu.com"
        }
    }
})
```

```HTML
<a v-bind:href="url">Baidu</a>
```

其中，`v-bind` 可省略，即只需在属性前面加一个 `:` 即可

```HTML
<a :href="url">Baidu</a>
```

注意：**属性绑定只会在 JS 端修改值后自动赋值给 HTML，不会在 HTML 属性变化时自动反映到 JS**，如：

```HTML
<input type="text" :value="input-value">
```

当该 `<input>` 元素值发生变化时，`input-value` 不会自动更新，应当使用 `v-model` 或监听事件代替

### 原始 HTML

通过 `v-html` 指令输出原始 HTML，**注意这将有一定的安全风险，一些外源的恶意代码可能通过该方法注入你的网页**。

`v-html` 指令接受一个 JS 表达式，而后将其通过类似 `innerHTML` 的方式输出到对应元素中

```HTML
<div v-html='... some html'></div>
```

### 事件绑定

使用 `v-on:事件名="JS表达式"` 绑定事件，若传入的是一个函数且没有参数，且不会产生歧义，括号可选

```HTML
<button type="button" v-on:click="age++">Increment</button>
```

其中 `v-on:` 可简写为 `@`

```HTML
<button type="button" @click="age--">Decrement</button>
```

`v-model` 可以看成 `:value` + `@change` 的简写

若调用函数，需要传入事件对象，使用 `$event` 代替

```HTML
<button type="button" v-on:click="click_the_button($event)">Button</button>
```

使用事件绑定修饰符，可以快捷的实现一些常用功能，其语法为 `v-on:event.modifier="..."`，其效果有：

| modifier | 效果                        |
| -------- | --------------------------- |
| prevent  | 禁止默认行为                |
| stop     | 阻断传播                    |
| capture  | 使用 `capture` 模式         | 
| self     | 当且仅当 target=this 时处理 |
| once     | 仅触发一次                  |
| passive  | 改善滚屏性能                |

```HTML
<a href="#" @click.prevent="...">...</a>
```

修饰符可以链式调用，通常来说顺序不是问题，但有例外：
- `passive` 和 `prevent` 不能同时使用
- `.prevent.self` 会阻止当前元素及其子元素的所有对应事件默认行为
- `.self.prevent` 仅会阻止当前元素对应事件默认行为，子元素无影响

# 绑定样式

## 绑定 class

绑定的属性与原始属性是可以并存的，如 class 属性

对于 `:class`，可以将其绑定一个对象，key 为类名，value 为一个 bool 的表达式，当条件为 true 时应用该类

```javascript
let vm = Vue.createApp({
    data() {
	    return {
		    isPurple: false
	    }
    },
    computed: {
	    circle_classes() {
		    return {
			    purple: this.isPurple
		    }
	    }
    }
})
```

```HTML
<input type='checkbox' v-bind="isPurple">
<!-- 随着前面的 input 切换 purple 类的有无 -->
<div :class="{ purple: isPurple }"></div>
<!-- 可进一步将其作为计算属性以增强可读性 -->
<div :class="circle_classes"></div>
``` 

`:class` 还允许接受一个数组，数组内存在多个对象，每个对象都遵循上述规则，以此接受多方控制

## 绑定样式

Vue 同样支持绑定单独的 CSS 样式属性，使用 `:style='{ 属性名:值, ... }'` 即可。注意其中带连字符的属性要替换成小写驼峰，或用引号包围

```javascript
let vm = Vue.createApp({
    data() {
	    return {
		    size: 10
	    }
    }
})
```

```html
<div :style='{ width: size + "px", height: size + "px", lineHeight = size + "px" }'></div>
<!-- 等效 -->
<div :style='{ width: size + "px", height: size + "px", "line-height" = size + "px" }'></div>
```

类似 `:class`，`:style` 同样支持计算属性和数组