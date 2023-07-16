# transition

使用 CSS `transition` 系列属性生成的动画可以通过 `<transition>` 元素配合 CSS 实现。该标签在对应动画发生时会自动为其中的元素添加相应的类以便动画播放

该标签多用于元素进入和离开时的动画，且支持 `transition` 或 `animation` 动画。

## CSS 动画

1. 将待添加动画的元素放到 `<transition></transition>` 标签之间
2. 为 `<transition>` 元素添加一个属性 `name`
3. 编辑对应 CSS 部分效果，类选择器命名规则：`[name]-[type]-[state]`
	- `[name]`：`<transition>` 标签的 `name` 属性名
	- `[type]`：元素行为，`enter` 表示元素进入，`leave` 表示离开，`appear` 表示初次渲染
	- `[state]`：动画状态，动画未开始时为 `from`，结束后为 `to`，进行中为 `active`

其他属性
- `mode`：各类动画的播放顺序，如 `out-in` 表示先播放元素删除的动画，后播放元素添加的动画
- `duration`：显式设置当前动画持续时间。当有多个动画时默认为最长的那个
- `type`：显式设置动画类型，与动画监听的事件有关，可选 `animation` 或 `transition`
- `appear`：该属性没有值。当添加该属性时，若元素一开始就在网页中有显示，则也会播放进入动画

一些问题
- 当动画控制的元素较多时，可能会有一些混乱，可以为每个元素添加不同的 `key` 属性
- 尽量使用 `opacity`，`transform` 等不会触发 DOM 重绘或能使用 GPU 的属性以提高性能

### transition

```html
<template>
  <!-- 用于切换显示/隐藏状态 -->
  <button type="button" @click="flag = !flag">Toggle</button>
  <Transition name="fade">
    <!-- 动画元素 -->
    <h2 v-if="flag">Hello World!</h2>
    <h2 v-else="flag">Another hello!</h2>
  </Transition>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      flag: false
    }
  },
}
</script>

<style>
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 500ms;
}
</style>

```

### animation

```html
<template>
  <button type="button" @click="flag = !flag">Toggle</button>
  <transition name="zoom">
    <h2 v-if="flag">Hello World!</h2>
  </transition>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      flag: false
    }
  }
}
</script>

<style>

.zoom-enter-active {
  animation: zoom-in 500ms linear forwards;
}

.zoom-leave-active {
  animation: zoom-out 500ms linear forwards;
}

@keyframes zoom-in {
  from {
    transform: scale(0, 0);
  }

  to {
    transform: scale(1, 1);
  }
}

@keyframes zoom-out {
  from {
    transform: scale(1, 1);
  }

  to {
    transform: scale(0, 0);
  }
}
</style>
```

## JavaScript 动画

实现 JavaScript 动画主要依赖于几个事件（钩子）：
![[Pasted image 20230522174645.png]]

监听这些事件，为其绑定相关函数即可。除此之外还有一组 `before-cancelled` 与 `after-cancelled` 表示动画被取消时触发。

可以为 `<transition>` 增加 `:css="false"` 属性，禁用 CSS 动画检查，提高效率

参数：第一个参数为动画元素，`leave`，`enter` 事件还有第二个参数 `done`，用于手动指定动画结束
- 使用 `:css="false"` 可以拒绝 CSS 加载，从而完全使用 JS 控制动画，此时 `done` 参数多作为动画对象的 `onfinish` 事件回调
- 动画：通过 el.animate 创建动画，详见 WebAPI 中的 animate 函数

```html
<template>
  <button type="button" @click="flag = !flag">Toggle</button>
  <transition 
	  @enter="enter" @leave="leave"
	  :css="false">
    <h2 v-if="flag">Hello World! - JS</h2>
  </transition>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      flag: false
    }
  },
  methods: {
    enter(el, done) {
      const anim = el.animate(
        [{ transform: 'scale3d(0, 0, 0)' }, {}],
        { duration: 1500 }
      )
      anim.onfinished = done
    },
    leave(el, done) {
      const anim = el.animate(
        [{}, { transform: 'scale3d(0, 0, 0)' }],
        { duration: 1500 }
      )
      anim.onfinished = done
    }
  }
}
</script>
```

# transition-group

类似于 `<transition>`，但 `<transition-group>` 用于为 `v-for` 属性生成的列表设置动画。

`<transition-group>` 与  `<transition>` 多数行为、属性、用法一致，区别如下：
- `<transition-group>` 没有 `mode` 属性，因此不能设置添加/移除动画的播放顺序。
- `<transition-group>` 增加 `[name]-move` 类（及配套 js 回调），当某一个元素位置发生变化时触发
	- `[name]-move` 仅在触发 enter/leave 动画播放时触发，因此需要保证操作发生后瞬间有位置改动
		- `remove` 使用 `opacity` 作为过渡属性时，由于移除动画播放时不会直接更改元素位置，无法触发
		- 将 `.[name]-leave-active` 类的定位方式更改为绝对定位（absolute）即可解决问题

```html
<script>
export default {
  name: "App",
  data() {
    return {
      flag: true,
      numbers: [1, 2, 3, 4, 5]
    }
  },
  methods: {
    addItem() {
      // 随机待插入数字
      const num = Math.floor(Math.random() * 100 + 1)
      // 随机插入位置
      const idx = Math.floor(Math.random() * this.numbers.length)
      this.numbers.splice(idx, 0, num)
    },

    removeItem(index) {
      // 移除
      this.numbers.splice(index, 1)
    }
  }
}
</script>

<template>
  <button @click="addItem">Add</button>

  <ul>
    <!-- 动画标签-->
    <transition-group name="fade">
      <li v-for="(number, index) in numbers" :key="number" @click="removeItem(index)">{{ number }}</li>
    </transition-group>
  </ul>
</template>

<style scoped>
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-to,
.fade-leave-from {
  opacity: 100%;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 500ms;
}

/* 绝对定位，修复删除动画 */
.fade-leave-active {
  position: absolute;
}

.fade-move {
  transition: all 500ms;
}
</style>
```

# 动画库 Animate.css

[Animate.css](https://animate.style/)是一个内置了多种动画的CSS库。使用时仅需使用 `[name]-[type]-[state]-class="..."` 重置下动画类名与库匹配即可。

该 CSS 库可使用 `.animate__animated { animation-during:xxx }` 覆盖持续时间

```html
<script>
export default {
  name: "App",
  data() {
    return {
      flag: true
    }
  },
  methods: {
    toggle() {
      this.flag = !this.flag
    }
  }
}
</script>

<template>
  <button @click="toggle">Toggle</button>
  <transition mode="out-in"
              enter-active-class="animate__animated animate__bounceIn"
              leave-active-class="animate__animated animate__backOutDown">
    <h1 v-if="flag">Hello World</h1>
    <h1 v-else>Another World</h1>
  </transition>
</template>

<style scoped>
.animate__animated {
  animation-duration: 1s;
}
</style>
```

注意需要引入 `animation.css` 库

```javascript
// npm install animate.css --save
import "animate.css"
```