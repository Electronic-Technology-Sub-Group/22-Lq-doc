表示一组数据源，可提供给诸如 `<input type="text" list="数据源 id">` 的标签使用，内部每组数据使用[[option]]标签定义，且通常设置 `display:none`

```HTML
<input type="text" list="data">  
  
<datalist id="data" style="display: none">  
  <option value="1">选项 1</option>  
  <option value="2">选项 2</option>  
  <option value="3">选项 3</option>  
</datalist>
```

![[Pasted image 20221209190135.png]]