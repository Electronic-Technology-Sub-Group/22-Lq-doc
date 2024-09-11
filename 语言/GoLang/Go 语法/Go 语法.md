
```go title:main.go
package main

import "fmt"

func main() {
    fmt.Println("Hello World!")
}
```

- [[变量]]
- [[数据类型]]
- [[控制结构]]
- [[集合]]

# 编译与运行

使用 `go run main.go` 可以直接运行 `main.go` 文件

使用 `go build main.go` 可以将 `main.go` 编译打包成可执行文件

使用 `go install main.go` 可以将 `main.go` 编译并安装到 `%GOBIN%` 环境变量指向的目录中

通过设置 `GOOS` 和 `GOARCH` 分别为目标系统和平台，可以方便的进行跨平台编译