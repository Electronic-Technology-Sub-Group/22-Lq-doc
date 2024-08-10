# $route

`$route` 对象：记录路由相关信息

|属性|类型|说明|
| ------| ------| --------------------------------------|
|`path`|`string`|当前路由路径|
|`params`|`object\|{}`|一个 `key:value` 对象，包含所有((20240529140108-lghljgd "动态参数"))|
|`query`|`object\|{}`|一个 `key:value` 对象，包含所有 ((20240529135814-wt9eub5 "URL 查询参数"))|
|`hash`|`string\|""`|当前路由哈希值（不带 `#`）|
|`fullPath`|`string`|解析后的完整 URL，包含查询参数和哈希|
|`matched`|`object[]`|当前匹配路径中包含的所有片段对应配置|
|`name`|`string`|当前路径名称|
|`meta`|`object`|路由元信息|
