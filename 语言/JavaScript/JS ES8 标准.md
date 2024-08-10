# async

`async` 和 `await` 关键字用于简化 `Promise` 的处理
- `async`  关键字修饰函数，使该函数只能被 `await` 调用或返回 `Promise`
- `await` 关键字修饰被 `async` 修饰的函数内的异步代码，用于生成链式调用，该关键字使在函数内等待一个返回 `Promise` 的函数返回其结果

```javascript
import thenFs from 'then-fs'

async function getAllFiles() {
	console.log("Start")
	const r1 = await thenFs.readFile("/file1", 'utf-8');
	console.log(r1)
	const r2 = await thenFs.readFile("/file2", 'utf-8');
	console.log(r2)
	const r3 = await thenFs.readFile("/file3", 'utf-8');
	console.log(r3)
	const r4 = await thenFs.readFile("/file4", 'utf-8');
	console.log(r4)
}

let af = getAllFiles()
```

以上代码相当于：

```javascript
import thenFs from 'then-fs'

function getAllFiles() {
	console.log("Start")
	return thenFs.readFile("/file1", 'utf-8')
				 .then(r1 => {
					 console.log(r1)
					 return thenFs.readFile("/file2", 'utf-8')
				 })
				 .then(r2 => {
					 console.log(r2)
					 return thenFs.readFile("/file3", 'utf-8')
				 })
				 .then(r3 => {
					 console.log(r3)
					 return thenFs.readFile("/file4", 'utf-8')
				 })
				 .then(r4 => console.log(r4))
}

let af = getAllFiles()
```

因此，`async` 方法内部可以保证顺序执行，**但方法外部代码与方法内部代码是异步的**（因为实质是 `Promise` 链式调用）
