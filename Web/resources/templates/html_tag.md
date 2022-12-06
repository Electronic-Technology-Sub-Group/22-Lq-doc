<%*
var type1 = await tp.system.suggester((s) => s, ['双标签', '单标签'], false, '双标签');
var type2 = await tp.system.prompt("类别", '');
var title = tp.file.title;
var params = await tp.system.prompt(`${title} 属性`, '');
params = params == '' ? '' : ` ${params}`;
var value = await tp.system.prompt("内容", '');
var exp = `<${title}${params}${value == '' ? '/>' : (`>${value}</${title}>`)}`;
var parent = await tp.system.prompt("父标签", 'body');
var use = await tp.system.prompt("作用");
%><% '#' + type1 %> <% type2 == '' ? '' : `#${type2}` %>

在<% '[' + '[' + parent + ']]' %>中，该标签用于<% use %>

```HTML
<% exp %>
```

效果：

<% exp %>
