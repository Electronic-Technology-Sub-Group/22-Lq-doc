#双标签 #表单

在[[form]]中，该标签用于为表单元素提供更大的活动区域，点击该标签的元素相当于点击其绑定的表单元素，且焦点也会被转移到对应表单元素中。

```HTML
<label for="id"></label>
```

属性
- for：若被修饰的元素不在 `label` 之内，则需要该属性指定表单元素，其值为绑定的表单元素的 `id` 

效果：

<label for="cb">123</label>
<input type="checkbox" id="cb"/> click!
