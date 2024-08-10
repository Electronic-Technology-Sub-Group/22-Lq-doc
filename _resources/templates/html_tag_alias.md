<%*
var type1 = await tp.system.suggester((s) => s, ['双标签', '单标签'], false, '双标签');
var type2 = await tp.system.prompt("类别", '');
var ori = await tp.system.prompt('原名');
%><% '#' + type1 %> <% type2 == '' ? '' : `#${type2}` %> <% '#' + '强调别名' %>

渲染效果等价于 `<% '<' + ori + '>' %>` 标签，当强调该标签内元素重要性时使用该版本

<% '![' + '[' + ori + ']]' %>