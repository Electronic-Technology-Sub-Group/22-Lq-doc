# 组合式 API

通过多组件可以将多个功能分解到不同组件实现，但当多个组件集合到一起时，按组件类型的配置往往会导致功能上配置的混乱。

可以使用组合 API 重新组织组件逻辑，将部分逻辑转移到 app 或组件的 `setup(props, context)` 函数中。在该函数中执行的操作可用的 API 称为组合式 API

`setup` 在组件创建之前调用，因此无法访问状态、计算属性、方法等，无法使用 `this`

组合式 API 中将大量使用<span data-type="text" parent-style="color: var(--b3-card-info-color);background-color: var(--b3-card-info-background);">响应式属性</span>，用于替代 `data`

```js
Vue.create({
    data()  { return { ... } },
    setup(props, context) { .... }
})
```

* `props`：通过 HTML 标签传入的属性

  ```js
  const SetupTestingComponent = {
      // 控件需要一个 abook { id, title } 参数
      props: ['abook'],
      setup(props) {
          console.log(props.abook.id)
          mybook = props.abook
          // 将 abook 暴露给 template
          return { mybook }
      },
      template: '{{mybook}}'
  }
  ```

  ```html
  <div id="app">
      <setup-testing :abook="book"></setup-testing>
  </div>
  ```

  `props` 是响应式参数，故不能通过 ES6 解构（将丧失响应性）。如果需要解构则使用 `Vue.toRefs(props)`
* `context`：当前上下文对象，包括 `{ attrs, slots, emit, expose }` 四个属性

  * `attrs`：等同于 `$attrs`，所有属性
  * `slots`：等同于 `$slots`，所有内部 `<slot>` 标签
  * `emit`：等同于 `$emit`，发布自定义事件的方法
  * `expose`：用于暴露公共 `property` 的函数

  `attrs`，`slots` 是带状态对象，随组件更新而更新，因此不能用 ES6 的解构

  `context` 是普通对象，可以正常解构

# 返回值

`setup()` 方法可以返回一个对象或函数

* 对象：若 `setup` 返回一个对象，该对象将暴露给模板渲染，模板可以直接访问该对象的属性

  ```js
  Vue.createApp({})
      .component('setup-testing', {
          setup() {
              // 创建一个响应式值
              const readersNumber = Vue.ref(1000)
              // 创建一个响应式对象
              const book = Vue.reactive({ title: '好书' })
              return { readersNumber, book }
          },
          template: '<div>{{readersNumber}}</div>'
      })
      .mount('#app')
  ```

  ```html
  <div id="app">
      <setup-testing></setup-testing>
  </div>
  ```

  * 使用 `Vue.ref` 创建一个响应式值
  * 使用 `Vue.reactive` 创建一个响应式对象
* 函数：`setup` 可以返回一个渲染函数。该函数中返回一个 `Vue.h` 的调用结果，可以使用同一个作用域中声明的响应状态

  ```js
  Vue.createApp({})
      .component('setup-testing', {
          setup() {
              const readersNumber = Vue.ref(1000)
              const book = Vue.reactive({ title: '好书' })
              return () => Vue.h('div', 
                                 [readersNumber.value, book.title])
          }
      })
      .mount('#app')
  ```

  ```html
  <div id="app">
      <setup-testing></setup-testing>
  </div>
  ```

  ![[image-20240527020803-yjrc4nh.png]]

‍
