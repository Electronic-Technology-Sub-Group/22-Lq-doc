以列表语法定义目录结构

``````col
`````col-md
````
```dirtree
- /components
	- App.tsx
	- App.css
- config.json
- /utils
	- converter.ts
	- parser.ts
```
````
`````
`````col-md
```dirtree
- /components
	- App.tsx
	- App.css
- config.json
- /utils
	- converter.ts
	- parser.ts
```
`````
``````
中间的空白行将被忽略，其他内容将打断连续的目录结构

``````col
`````col-md
````
```dirtree
- a
  - b
  - c
- d

- 1
- 2
  - 3
    - 4
sometext
- 1
- 2
  - 3
    - 4
```
````
`````
`````col-md
```dirtree
- a
  - b
  - c
- d

- 1
- 2
  - 3
    - 4

sometext

- 1
- 2
  - 3
    - 4
```
`````
``````
