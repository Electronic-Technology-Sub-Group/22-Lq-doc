- 时间复杂度：最坏情况下为 <% '$' + await tp.system.prompt("时间复杂度：", "O(n^2)", true) + '$' %>
- 空间复杂度：<% '$' + await tp.system.prompt("空间复杂度：", "O(n)", true) + '$' %>
- 排序方法：<% await tp.system.suggester((s)=>s, ["比较排序", "其他排序"], true) %>
- 稳定性：<% await tp.system.suggester((s)=>s, ["稳定", "不稳定"], true) %>





```c++
// 准备数据：n 个数字
int count;
int values[count];


```