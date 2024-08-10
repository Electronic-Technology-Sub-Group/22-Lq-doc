# script setup 写法

# 组件配置

* `<script setup>` 中任何变量、方法都会映射到组件空间

  通过 `import` 导入的属性和方法也可以被直接访问到

  * 任何属性都可以直接被模板访问
  * 任何通过 `ref`、`reactive` 创建的响应式变量等效于 `data` 中的属性
  * 任何方法都等效于 `methods` 中的方法
  * 任何无参方法都等效于 `computed` 中的计算属性
* 任何通过 `import` 直接导入的组件都相当于通过 `components` 注册组件
* 任何调用 `beforeCreate(fn)`，`created(fn)` 等方法传入的函数都相当于注册到了生命周期钩子函数

# 组件通信

* props：使用 `defineProps` 接收父组件传递的 `props` 值
* 自定义事件：使用 `defineEmits` 向父组件抛出自定义事件
* 使用 `defineExpose` 将数据暴露给父组件，父组件通过在子组件上添加 `ref` 属性接收

> `src/App.vue`：父组件
>
> ```html
> <template>
>   <div>
>     <HelloWorld :info="msg" constv="88" @myAdd="myAddAction" @myDel="myDelAction" ref="comRef" />
>     <button @click="getSon">获取子组件数据</button>
>   </div>
> </template>
> <script setup>
>   // 注册 HelloWorld 子组件
>   import HelloWorld from '@/components/HelloWorld.vue'
>   // 与子组件的数据通信
>   import { ref } from 'vue'
>   const msg = '传递给子组件的数据'
>   const myAddAction = msg => alert(`子组件新增按钮传值 ${msg}`)
>   const myDelAction = msg => alert(`子组件删除按钮传值 ${msg}`)
>   const comRef = ref()
>   const getSon = () => {
>     console.log("性别：", comRef.value.prop1)
>     console.log("其他信息：", comRef.value.prop2)
>   }
> </script>
> ```
>
> `src/components/HelloWorld.vue`：子组件
>
> ```html
> <template>
>   <div>
>     <h4>接收父属性传值</h4>
>     <h4>info: {{ info }}</h4>
>     <h4>constv: {{ constv }}</h4>
>   </div>
>   <div>
>     <button @click="add">新增</button>
>     <button @click="del">删除</button>
>   </div>
>   <div>
>     <h4>性别: {{ prop1 }}</h4>
>     <h4>其他信息: {{ prop2 }}</h4>
>   </div>
> </template>
>
> <script setup>
> import {ref, reactive} from 'vue'
> const prop1 = ref('男')
> const prop2 = reactive({
>   uname: '陈恒',
>   age: 88
> })
>
> import {defineProps, defineEmits, defineExpose} from 'vue'
> defineProps({
>   info: {
>     type: String,
>     default: '------'
>   },
>   constv: {
>     type: String,
>     default: '0'
>   }
> })
> const myEmits = defineEmits(['myAdd', 'myDel'])
> const add = () => myEmits('myAdd', '向父组件传递的新增数据')
> const del = () => myEmits('myDel', '向父组件传递的删除数据')
> defineExpose({ prop1, prop2 })
> </script>
> ```
>
> <iframe src="/widgets/widget-excalidraw/" data-src="/widgets/widget-excalidraw/" data-subtype="widget" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width: 869px; height: 454px;"></iframe>

# Vue Router

* `useRouter()` 等效于 `this.$router`
* `useRoute()` 等效于 `this.$route`

```js
import { useRouter } from 'vue-router'

const router = userRouter()
router.push(...)
```

# this

`getCurrentInstance()` 获取一个 `proxy` 可以访问 `this` 中的一些成员如 `$ref` 等

```js
const proxy = getCurrentInstance()
const cancel = () => proxy.refs.loginForm.resetFields()
```
