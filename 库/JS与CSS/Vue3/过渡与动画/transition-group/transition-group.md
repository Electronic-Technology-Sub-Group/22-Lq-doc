# transition-group

`<transition-group>` 标签用于一组元素之间的动画，如列表等

* 不同于 `<transition>`，默认以 `<span>` 元素渲染
* 不切换特有元素，因此过渡模式不可用
* 内部元素需要带有 `key` 属性
* CSS 过渡动画类应用于内部元素而不是组件本身

`<transition-group>` 用法与 `<transition>` 类似，只是多了一个 `v-move` 类用于进行列表元素的移动动画
