# 迭代渲染 v-for

可以使用 `v-for` 遍历一个数组或对象，结合 `in` 操作符使用，形式为 `v-for="item in list"`

* 遍历数组：第一个参数是数组对象，第二个参数是下标索引

  支持数组更新方法 `push`，`pop`，`unshift`，`shift`，`splice`，`sort`，`reverse` 等方法的同步更新

  ```html
  <div id="hello-vue">
      <ul>
          <li v-for="(item, index) in items">
              {{index}} -- {{item}}
          </li>
      </ul>
  </div>
  ```

  ```html
  <div id="hello-vue">
      <ul>
          <li v-for="user in users">
              {{user.uname}}
          </li>
      </ul>
  </div>
  ```
* 遍历对象：第一个参数是属性值，第二个是属性名，第三个是索引

  ```html
  <div id="hello-vue">
      <ul>
          <li v-for="(value, key, index) in myObject">{{++index}}. {{key}}={{value}}</li>
      </ul>
  </div>
  ```
* 遍历数字：`v-for="i in n"` 相当于从 1 到 `n` 的遍历

  注意下标从 1 开始到 n，共 n 个数字

  ```html
  <div id="hello-vue">
      <ul>
          <li v-for="i in 100">{{i}}</li>
      </ul>
  </div>
  ```
* 多层循环时，外层循环的结果可以在内层循环中直接使用

  ```html
  <ul v-for="numbers in myData">
      <li v-for="n in myNumbers(numbers)">{{n}}</li>
  </ul>
  ```

  ![[image-20240522174936-kn4rtwu.png]]

  ```js
  Vue.createApp({
      data() {
          return {
              myData: [[111, 200, 300, 44, 55], 
                       [600, 777, 88, 999, 1011]]
          }
      },
      methods: {
          myNumbers(numbers) {
              return numbers.filter(n => n > 100)
          }
      }
  }).mount("#myFor")
  ```

‍
