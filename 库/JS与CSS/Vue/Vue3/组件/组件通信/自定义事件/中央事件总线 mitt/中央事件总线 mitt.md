Vue 推荐使用一个空 Vue 实例作为中央事件总线媒介。

Vue.js 3.x 移除了 `$on`，`$off` 和 `$once` API，推荐使用 `mitt` 外部库实现

`mitt`：`https://www.npmjs.com/package/mitt`

* `buyer`、`seller` 元素模板，包含一个 `h1` 用于显示来自对方的消息

  ```html
  <div id="buyer">
      <h1>{{message1}}</h1>
      <button @click="transferb">我是买方，向卖方传递信息</button>
  </div>
  ```
  ```html
  <div id="seller">
      <h1>{{message2}}</h1>
      <button @click="transfers">我是卖方，向买方传递信息</button>
  </div>
  ```
* 初始化 `mitt`，创建事件总线 `bus` 实例

  ```html
  <script src="https://unpkg.com/mitt/dist/mitt.umd.js"></script>
  ```
  ```js
  const bus = mitt()
  ```
* `buyer`、`seller` app 创建，在对应方法发送事件，在 `mounted` 事件中注册事件处理器

  ```js
  const buyer = Vue.createApp({
      data() {
          return {
              message1: ''
          }
      },
      methods: {
          transferb() {
              bus.emit('on-message1', '来自买方的信息')
          }
      },
      mounted() {
          bus.on('on-message2', msg => this.message1 = msg)
      }
  }).mount('#buyer')
  ```
  ```js
  const seller = Vue.createApp({
      data() {
          return {
              message2: ''
          }
      },
      methods: {
          transfers() {
              bus.emit('on-message2', '来自卖方的信息')
          }
      },
      mounted() {
          bus.on('on-message1', msg => this.message2 = msg)
      }
  }).mount('#seller')
  ```
