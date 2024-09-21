`v-model` 可以在 `<input>`、`<textarea>` 和 `<select>` 元素上完成双向绑定

* 对应元素忽略 `value`、`checked`、`selected` 的初始值，使用 Vue 实例作为初始数据
* 双向绑定：对应元素数据改变时将直接反映到 Vue app 属性值，反之亦然

> [!note] v-model 实际触发 `update:modelValue` 事件，并携带 `modelValue` 类型数据

```js
Vue.createApp({
    data() {
        return {
            uname: '',
            introduction: '',
            singers: [],
            sex: '男',
            single: '中国',
            moreselect: ['中国'],
            options: [
                {text: '中国', value: '中国'},
                {text: '英国', value: '英国'},
                {text: '日本', value: '日本'},
                {text: '美国', value: '美国'},
            ]
        }
    }
}).mount("#v-model-databinding")
```

![[image-20240523135040-85v76sg.png]]

```html
<label>用户名：<input v-model="uname"></label>
<p>输入的用户名：{{uname}}</p>
<label>个人简介：<textarea v-model="introduction"></textarea></label>
<p>输入的简介：{{introduction}}</p>
<p>备选歌手：
    <input type="checkbox" id="zhangsan" value="张三" v-model="singers">
    <label for="zhangsan">张三</label>
    <input type="checkbox" id="lisi" value="李四" v-model="singers">
    <label for="lisi">李四</label>
    <input type="checkbox" id="wangwu" value="王五" v-model="singers">
    <label for="wangwu">王五</label>
    <input type="checkbox" id="chenheng" value="陈恒" v-model="singers">
    <label for="chenheng">陈恒</label>
    <br>
    你喜欢的歌手：{{singers}}
</p>
<p>
    性别：
    <input type="radio" id="male" value="男" v-model="sex">
    <label for="male">男</label>
    <input type="radio" id="female" value="女" v-model="sex">
    <label for="female">女</label>
    <br>
    你的性别是：{{sex}}
</p>
<p>
    <label>国籍：<select v-model="single">
        <option v-for="option in options" 
                :value="option.value">{{option.text}}</option>
    </select></label>
    <br>
    你的国籍：{{single}}
</p>
<p>
    <label>备选国家：<select v-model="moreselect" multiple>
        <option v-for="option in options" 
                :value="option.value">{{option.text}}</option>
    </select></label>
    <br>
    你去过的国家：{{moreselect}}
</p>
```

# 修饰符

|修饰符|说明|
| --------| ----------------------------------------------------|
|`lazy`|输入框不会立即同步到变量，而是在 `change` 事件发生后同步|
|`number`|自动将输入框内值转换为数字|
|`trim`|自动去除输入的前后空格|

‍
