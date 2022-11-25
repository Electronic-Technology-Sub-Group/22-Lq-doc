1. 在项目中安装 Babel（需要 Node.js npm 环境）
```bash
npm install --save-dev babel-cil
```

2. 执行转换
```bash
babel src -d lib
```

以上语句表示将 src 目录的所有 JS 文件转义后保存到 lib 目录中

3. 集成到项目中
```json
// package.json
{
// 忽略其他内容
"script": {
    "build": "babel src -d lib"
}
// 忽略其他内容
}
```

之后就可以直接使用

```bash
npm run build
```

直接构建了。