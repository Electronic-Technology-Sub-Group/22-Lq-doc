入门版图形库

目前只支持 VS，Clion 及其他基于 Mingw64 g++ 的 IDE 需要特殊版本的文件：
[在 CLion、Dev-C++ 或 Code::Blocks 下面配置 EasyX（2022-9-1 更新） - CodeBus](https://codebus.cn/bestans/easyx-for-mingw)

不过我更喜欢为每个项目配置单独的依赖环境，需要手动配置下 `CMakeLists.txt` 如下：

```cmake
# add_executable 之前
include_directories(include) # include: 头文件目录  
link_directories(lib)        # lib: libeasyx.a 所在目录
link_libraries(libeasyx.a)
```

1. [[EasyX 基本概念]]