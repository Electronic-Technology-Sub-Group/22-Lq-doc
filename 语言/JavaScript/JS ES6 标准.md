
# Module 语法

ES6 Module 语法在语言标准层面上支持了模块功能。与 CommonJS，CMD，AMD 等相比，可以在编译时确定模块依赖关系及输入输出变量，效率更高

Module 模块特点：
- 静态加载：编译阶段按需加载
- 默认使用严格模式

## export

`export` 关键字用于规定模块的对外接口，除 `export` 修饰的变量外，其他成员都无法在模块外（文件外）访问到。`export` 有以下两种使用方法：

- 直接修饰变量，函数或类
```javascript
export let sharedVariable = 123
export function add(x, y) {
	return x + y
}
export class AClass {}
```

- 批量导出现有变量和函数
```javascript
let a = 10;
let b = "xxx";
function add(x, y) {
	return x + y;
}
function sub(x, y) {
	return x - y;
}

export {
	a, add,                // 直接以原名导出
	b as name, sub as fsub // 以别名导出
}
```

## import

使用 `import` 加载其他模块中 `export` 的成员

```javascript
import { ... /* 成员名 */ } from '模块名或 js 文件'
import { 成员名 as 别名 } from '模块名或 js 文件'
import '模块名或 js 文件' // 只执行所加载脚本，不导入
import * from '模块名或 js 文件' // 导入所有 export 的成员
```

- `import` 导入的变量都是只读的，不允许在加载模块的脚本中改写接口
- `import` 后如果跟模块名，则必须有对应配置文件，用于查找 `js` 文件
- `import` 命令具有提升效果，所有 `import` 的语句都会提升到文档开头
- `import` 是静态执行的，因此不能使用表达式和变量，只能用字符串字面量
- 对于相同的两条 `import` 语句，只会执行一次

## 其他方法

- 默认接口

```javascript
let x = 10
export default x 
```

`exprot default` 暴露的变量只能有一个，该导出的变量可以用任意变量接收，用于接收的变量不需要 `{}` 包围，如：

```javascript
import y from '模块名'
```

**注意：每个模块中只能 `export default` 一次**

- 转发：将 `import` 和 `export` 合并

```javascript
export { foo, bar } from 'other_module'
```

以上写法等同于：

```javascript
import { foo, bar } from 'other_module'
export { foo, bar }
```

转发同样支持默认接口和 `*`

```javascript
export { default } from `foo`
export { es6 as default } from `bar`
export * as otherIdentifier from 'emmmm' // ES2020 后支持
```

## 继承

- [ ] 模块之间允许继承

## 动态加载

动态加载模块由 ES 2020 提案引入，使用 `import` 函数，并返回一个 `Promise` 对象

```javascript
import('模块名或 js 文件')
	.then (module => { ... })
	.catch (err => { ... })
```

该方法类似异步的 NodeJS `require` 方法

# Promise

 每个 Promise 代表一个异步操作，并提供 `then`，`catch`，`finally` 方法
 - `then(functionSuccessful[, functionError])`：指定成功和失败的回调函数
 - `catch(err)` 方法用于捕获错误

使用 `new Promise(function(resolve, reject))` 创建 `Promise` 对象

```javascript
let p = new Promise((resolve, reject) => {
	// 任意异步操作
	// 使用 resolve(result) 处理成功请求
	// 使用 reject(error) 处理失败请求
});
```

注意：**`Promise` 中的代码是同步执行的，只是通常里面是一个异步任务罢了**

## 回调地狱

> 回调地狱：多层回调相互嵌套，使耦合性太强，及代码冗余

 若 `then` 方法返回了一个 `Promise` 实例，则下一个 `then` 方法实质是对返回的实例的处理，实现了回调的链式调用，以解决回调地狱问题

```javascript
import thenFs from 'then-fs' // 一个使用 Promise 的文件读写库

thenFs.loadFile('/file1', 'utf-8')
	  .then(r => { /* r is file1 */; return thenfs.readFile('/file2', 'utf-8') })
	  .then(r => { /* r is file2 */; return thenfs.readFile('/file3', 'utf-8') })
	  .then(r => { /* r is file3 */; return thenfs.readFile('/file4', 'utf-8') })
      // ...
```

`catch`  可用于捕获整个调用链的异常并消除该异常。`catch` 之后的 `Promise` 链不受之前的异常影响

## 聚合

将多个 `Promise` 聚合成一个 `Promise` 并使之并行运行的操作主要有 `Promise.all(...)` 和 `Promise.race(...)` 两个方法，其区别在于：
- `all` 方法会在所有 `Promise` 都结束时调用 `then` 的回调进行处理
- `race` 方法会在任意一个 `Promise` 结束时调用 `then` 回调处理

# class

- [ ] class