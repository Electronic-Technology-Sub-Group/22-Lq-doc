用于声明在代码中使用汇编代码

```c++

extern "C" int func();

// 以汇编定义的 func 函数
asm(R"(
.globl func
    .type func, @function
    func:
        .cfi_startproc
        movl $7, %eax
        ret
        .cfi_endproc
)");

int main() {
    // do something
    // 扩展内联汇编
    int n = func();
    asm(
        "leal (%0, %0, 4), %0"
        : "=r" (n)
        : "0" (n));
    );
    // 标准内联汇编
    asm(
        "movq $60",
        "%rax\n\t",
        "movq $2",
        "%rdi\n\t",
        "syscall"
    );
}
```