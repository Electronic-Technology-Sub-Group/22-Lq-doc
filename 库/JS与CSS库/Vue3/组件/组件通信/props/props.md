# props

可以通过 `props` 实现从父组件向子组件的单向数据传递。`props` 的值可以是任意类型。

* 在子组件属性的 `props` 块定义一个字符串数组或对象，其中包括了可接受数据名

  当 `props` 值为对象时，允许对传入的变量进行<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">数据验证</span>

  ```js
  components: {
      'children': {
          // ...
          props: ['id', 'title'],
      }
  }
  ```
* 在子组件中，可以像访问 `data` 中的变量一样访问来自父组件的属性

  ```html
  <h4>{{id}}: {{title}}</h4>
  ```
* 在父组件中，使用标签属性向子组件传递数据

  注意：HTML 属性不区分大小写，因此如果 `props` 中属性使用驼峰式的写法，标签属性应使用 `-` 连接

  * 直接使用属性名作为标签的属性名，允许使用 `v-bind:name=value` 的形式

    ```html
    <children v-for="post in posts" :id="post.id" :title="post.title"></children>
    ```
  * 可以使用 `v-bind="object"` 的形式将一个对象直接传递给组件，对象将按其属性名展开传递给子组件

    ```html
    <!-- post: {id=1, title='...'} -->
    <children v-for="post in posts" v-bind="post"></children>
    ```

下面实例中，`app` 向组件 `parent` 传递属性 `message`，`parent` 向 `children` 传递属性 `id` 和 `title`：

1. 向 app 注册 `parent`、`children` 的组件配置：

    ```js
    app.component('parent', {
        data() {
            return {
                posts: [
                    {id: 1, title: 'My journey with Vue'},
                    {id: 2, title: 'Blogging with Vue'},
                    {id: 3, title: 'Why Vue is so fun'}
                ]
            }
        },
        // parent 接收来自父组件的 message 参数
        props: ['message'],
        components: {
            // children 是 parent 的私有组件
            'children': {
                // children 接收来自父组件的 id 和 title 参数
                props: ['id', 'title'],
                template: '#children'
            }
        },
        template: '#parent'
    })
    app.mount('#message-post-demo')
    ```
    ![[image-20240523162854-qaifng8.png]]
2. 创建 `children`、`parent` 组件模板，并在网页中使用

    ```html
    <template id="parent">
        <h4>{{message}}</h4>
        <children v-for="post in posts" :id="post.id" :title="post.title"></children>
        =========
        <children v-for="post in posts" v-bind="post"></children>
    </template>
    ```
    ```html
    <template id="children">
        <h4>{{id}}: {{title}}</h4>
    </template>
    ```
    ```html
    <div id="message-post-demo">
        <parent message="来自父元素的消息"></parent>
    </div>
    ```
