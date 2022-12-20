#单标签 

属性
- charset：HTML 文件字符集，通常为 UTF-8，与文件本身字符集有关
- content：属性值
- name：文档元数据
- http-equiv：编译指令

| 属性类型   | 属性名          | 属性值                                | 说明                  |
| ---------- | --------------- | ------------------------------------- | --------------------- |
| http-equiv | content-type    | `"text/html; charset=utf-8"`          | 仅适用于 HTML         |
| http-equiv | refresh         | `n`                                   | 每隔 n 秒刷新一次页面 |
| http-equiv | refresh         | `n;url="..."`                         | n 秒后跳转到 url      |
| http-equiv | X-UA-Compatible | `IE=edge`                             | IE 兼容性             |
| name       | viewport        | `width=device-width, initial-scale=1` | 移动端兼容            |
|            |                 |                                       |                       |
