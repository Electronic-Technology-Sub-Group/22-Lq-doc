# create-vue

Vue 包含大量外置工具以供使用，我们可以通过 `create-vue` 工具创建按步骤创建一个 Vue 项目。在控制台中输入 `npm init vue@latest` 即可。

```bash
npm init vue@latest
```

若之前没有安装 `create-vue`，控制台会提示安装对应的插件。确认安装后会提示选择使用的相关配置

![[Pasted image 20230521202836.png]]

之后按提示进入项目目录安装依赖即可

```bash
cd [your-project-name]
npm install
```

![[Pasted image 20230521203026.png]]

项目文件结构如图：

![[Pasted image 20230521203035.png]]

项目的默认配置包括：
- `plugin-vue` 插件用于将 vite 与 vue 绑定
- 用于 `vite` 的源文件根目录快捷方式 `@`

各目录说明：
- `public`：项目静态资源
- `src`：源码

# 组件

Vue 组件是一系列以 `.vue` 为扩展名的文件，仅有一个文件的组件成为单文件组件。

单文件组件通常分为 HTML、JS、CSS 三大组成部分，最后全部编译成一个 JS，即渲染函数
- HTML 模板：位于 `<template>` 标签中
- JavaScript 脚本：位于 `<script>` 标签中。若需要声明式语法应添加 `setup` 属性
- CSS 样式：位于 `<style>` 标签中

单文件组件可以直接被 `import` 到网页中，Vue 应用的主组件使用 `createApp` 加载

```javascript
import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
```

## 创建组件

### 模板

创建一个 `.vue` 文件即可。单文件组件的最低要求是必须有一个 `<template>` 标签，即至少要有 HTML 部分（当然这部分可以为空）。一下内容保存成一个 vue 文件即一个合法的 Vue 组件：

```html
<template>
    <p>Hello World!</p>
</template>
```

`<template>` 即[[Vue - 模板语法|前面]]写在 HTML 中的模板，或 `template` 属性值。

### 初始化脚本

添加 `<script>` 标签，并在其中导出一个对象即可。该对象即[[Vue - 模板语法|前面]] `createApp` 中传入的初始化对象。

```HTML
<script>
export default {
}
</script>
```

通常，该对象会包含一个 `name` 属性用于调试

```html
<template>
    <p>{{ msg }}</p>
</template>

<script>
export default {
    name: 'MyApp',
    data() {
        return {
            msg: 'Hello World!'
        }
    }
}
</script>
```

![[Pasted image 20230521210124.png]]

## 子组件

子组件通常放在 `src/components` 目录中，与主程序基本相同。相关模板可通过 `Vue VSCode Snippets` 插件快速生成

![[Pasted image 20230521210603.png]]

```html
<template>
    <div>

    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>
```

### 注册

将控件注册到 Vue App 中有两种方法
- JavaScript 中，在 `mount` 方法前调用  `component` 方法将控件注册到 App 全局
	- 特点：控件在整个 App 全局可用（有点算缺点吧）
	- 缺点：增加打包优化的难度

```javascript
// main.js
import './assets/main.css'

import { createApp } from 'vue'
import MyApp from './MyApp.vue'

// 导入控件
import Greeting from '@/components/Greeting.vue'

createApp(MyApp)
	// 注册控件
    .component(Greeting)
    .mount('#app')
```

- `<script>` 标签中，在导出对象的 `components` 属性中加入子控件，则子控件只能在当前控件中使用

```html
<template></template>

<script>
// 导入控件
import Greeting from '@/components/Greeting.vue'

export default {
    name: 'MyApp',
    components: {
	    // 注册控件
        Greeting
    }
}
</script>
```

### 使用

在注册控件后，即可在 `<template>` 中添加该控件。像使用 HTML 标签一样添加即可

```html
<template>
    <Greeting></Greeting>
</template>
```

1. 控件名：如果注册的是全局，那使用注册的名称；如果是局部，使用 `components` 对象的键
2. 大小写：可以直接使用控件名，也可以使用全小写形式，中间以 `-` 为连字符

## 样式

直接在控件或子控件中添加 `<script>` 块用于样式设置

在 `<script>` 标签中添加 `scoped` 属性可使样式只应用于当前组件，否则将对网页全局生效。该属性通过 Shadow DOM 实现

### Shadow Dom

该技术可以使一个 DOM 上的控件彼此隔离，样式彼此不互通
- 并非所有浏览器都支持 Shadow DOM，Vue 通过在对应元素上增加单独的属性增强兼容性

### 扩展 CSS

Vue 允许使用 SASS 等扩展 CSS 语法，需要在 `style` 标签上增加 `lang="scss"` 等属性，支持 SCSS，LESS 等大多数常用 CSS 扩展语法

注意：需要运行 `npm i sass --save-dev` 等命令安装对应组件以支持对应格式

```html
<style scoped lang="scss">
p {
    color: red;

    &:hover {
        color: darken(#CC4444, 15%)
    }
}
</style>
```

## 组件间通信

### props

Vue 不允许同级组件之间通信，数据只能从父组件的 `props` 发送至子组件

![[Pasted image 20230522011417.png]]

`prop`，即属性，我们通过为控件添加属性来接收来自父控件的数据

```html
<template>
    <h3>Hey!</h3>
    <greeting></greeting>
    // 为 User 控件添加一个属性，并绑定父控件 age 变量
    // 稍后就可以通过 age 属性访问到该变量
    <user :age="age"></user>
</template>

<script>
import Greeting from '@/components/Greeting.vue'
import User from '@/components/User.vue'

export default {
    name: 'MyApp',
    components: {
        Greeting, User
    },
    data() {
        return {
            age: 20
        }
    }
}
</script>
```

在子控件中，可以通过 `props` 属性定义可接受的变量列表

```html
<template>
    <div>
	    <!-- 可以直接访问 age 属性 -->
        <p>The user is {{ age }} years old</p>
    </div>
</template>

<script>
export default {
    name: 'User',
    // 接受一个 age 属性
    props: ['age']
}
</script>

<style lang="scss" scoped>

</style>
```

**限制：只能由父元素发送给子元素；子控件无法更改属性值**

#### 数据验证

Vue 支持对传入的数据增加数据校验，包括类型、值范围等。只需要将 `props` 转换成一个对象，并在其中定义即可
- type：数据类型，支持任何原生 js 类型和构造函数，或其数组表示接受几种类型均可
![[Pasted image 20230522081228.png]]

- required：是否必须。boolean

```javascript
export default {
    props: {
        age: {
            type: Number,
            required: true
        }
    }
}
```

- default：默认值
- validator：自定义验证函数，接受一个参数，返回一个 bool
	- **注意：该方法运行于控件创建之前，因此控件的其他数据和方法无法被访问到**

#### 回调

由于 props 支持传入方法，即使子控件无法直接更改父控件的值，我们也可以通过传入一个能改变值的函数间接修改

```html
<!-- User.vue -->
<template>
    <div>
		<p>The user is {{ age }} years old</p>
	    // 绑定按钮事件
        <button type="button" @click="updateAge">Update</button>
    </div>
</template>

<script>
export default {
    name: 'User',
    props: {
	    // 设置传入值类型
        updateAge: Function
    }
}
</script>

<!-- App.vue -->
<template>
    <h3>Hey!</h3>
    <user :age="age" :updateAge="addAge"></user>
</template>

<script>
import User from '@/components/User.vue'

export default {
    name: 'MyApp',
    components: {
        User
    },
    data() {
        return {
            age: 20
        }
    },
    methods: {
        addAge() {
            ++this.age;
        }
    },
}
</script>
```

### event

除了 props 直接传递值，我们也可以通过自定义事件的方法发送数据

发送事件通过 `this.$emit(name: string[, param])` 方法实现

```html
<!-- User.vue -->

<template>
    <div>
        <p>The user is <span @click="onClickAge">{{ age }}</span> years old</p>
    </div>
</template>

<script>
export default {
    name: 'User',
    props: ["age"],
    methods: {
        onClickAge() {
	        // 触发事件
            this.$emit("age-update")
        }
    }
}
</script>

<style lang="scss" scoped></style>
```

事件可以直接用 `@event-name` 的形式监听即可

```html
<!-- App.vue -->
<template>
    <h3>Hey!</h3>
    <greeting :age="age"></greeting>
    <!-- 监听事件 -->
    <user :age="age" @age-update="++age"></user>
</template>
```

- 监听事件中的代码位于父控件中，因此能正常更新数据
- 事件的发送者和接收者都是同一个控件，因此不需要注册
	- 如果要求其他控件接受该事件，需要在控件的初始化对象中增加 `emits` 属性

推荐使用 Event 而非 props 回调函数的方式更新数据。二者在性能上差距不大，但 Event 在代码一致性上比较好，而且在调试时也可以看到所有事件的触发时间和值等详细信息

### slots

slots 插槽允许将一部分 HTML 插入到组件中，即 `<component-name></component-name>` 之间的部分。例如，我们有一个表单，为了表单各元素风格一致，我们创建了一个 `Form.vue` 组件，注册为 `AppForm` 以避免与 HTML 的 form 冲突：

```html
<template>
  <app-form></app-form>
</template>

<script>

import Form from '@/components/Form.vue'

export default {
  name: "App",
  components: {
    AppForm: Form
  }
}
```

但我们期望的是风格统一的表单，而不是将所有表单都写死在一个控件里，即如下形式：

```html
<template>
  <app-form>
    <div class="help">
      <p>This is some help text.</p>
    </div>
    <div class="fields">
      <input type="text" placeholder="email">
      <input type="text" placeholder="username">
      <input type="password" placeholder="password">
    </div>
    <div class="buttons">
      <button type="submit">Submit</button>
    </div>
  </app-form>
</template>
```

我们可以在控件中使用 `<slot>` 标签作为占位符，用以替代中间部分

```html
<!-- Form.vue -->
<template>
    <form>
        <slot>No form to renderer</slot>
    </form>
</template>
```

`<slot>` 标签中间也可以插入内容，当没有可替换的内容时使用

#### 具名 slot

一个控件可以有多个 `slot`，为每个 `slot` 增加 name 属性，并在外部使用 `v-slot` 指定使用哪个 slot
- 使用 `v-slot:name` 区分每个 slot
- slot 只适用于 `<template>` 标签中，因此需要将内容放在 `<template>` 标签下

```html
<!-- Form.vue -->
<template>
    <form>
        <div class="help">
	        <!-- slot: help -->
            <slot name="help">No help message</slot>
        </div>
        <div class="fields">
	        <!-- slot: fields -->
            <slot name="fields">No fields</slot>
        </div>
        <div class="buttons">
	        <!-- slot: buttons -->
            <slot name="buttons">No buttons</slot>
        </div>
    </form>
</template>

<!-- App.vue -->
<template>
  <app-form>
    <!-- 使用 template 放置各 slot 数据 -->
    <template v-slot:help>
      <p>This is some help text.</p>
    </template>
    <template v-slot:fields>
      <input type="text" placeholder="email">
      <input type="text" placeholder="username">
      <input type="password" placeholder="password">
    </template>
    <template v-slot:buttons>
      <button type="submit">Submit</button>
    </template>
  </app-form>
</template>
```

具名 slot 和匿名 slot 可以同时存在

## 动态组件

### component

动态组件指允许在不同组件间切换的组件的情况，使用 `<component>` 标签实现
- 使用 `is` 属性指定组件名称

1. 进行控件切换的前置准备：创建一个下拉列表，为两个选项绑定对应的值，期望是根据下拉列表选择不同的组件显示。

```html
<template>
  <select v-model="componentName">
    <option value="About">About</option>
    <option value="Home">Home</option>
  </select>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      componentName: 'Home'
    }
  },
}
</script>
```

2. 创建两个组件 `components/About.vue` 和 `components/Home.vue` 并注册到 APP 中

```html
<script>
import About from '@/components/About.vue'
import Home from '@/components/Home.vue'

export default {
  components: {
    About, Home
  },
}
</script>
```

3. 使用 `<component>` 标签加载组件

```html
<template>
  <component :is="componentName"></component>
</template>
```

### keep-alive

`<component>` 有一个问题是，当切换到其他组件时，现有组件将被销毁（可以触发 `unmounted` 事件）。这导致每次切换都会丢失当前数据。

在 `<component>` 标签外套一层 `<keep-alive>` 标签可将不显示的组件仍保留在内存当中，防止组件卸载和销毁。

通过 Vue 插件可以看到，在 `KeepActive` 标签中的元素，不显示的会标记 `inactive` 属性而没有被删除 - 然而在 元素 标签并没有发现标记了 `inactive` 的元素，说明该元素的确从 DOM 树中移除了，但仍保存在内存中

![[Pasted image 20230522160841.png]]

使用 `<keep-alive>` 标签的 `<component>` 标签切换时，被切换的两个元素不触发 `unmounted` 和 `mounted` 生命周期事件，而是触发 `activated` 和 `deactivated` 生命周期。