用于在堆上释放内存的[[关键字]]和[[运算符]]，可以释放使用[[new]]申请的堆内存，但不会修改原[[指针]]的值。

```c++
int *a = new int;

int **b = new int[10];

int count = 20;
int **c = new int[count];

delete a;
delete [] b, c;
```

可以直接释放[[nullptr]]，这时不会进行任何操作。但若被释放的指针不是[[nullptr]]且对应的内存不是由[[new]]申请的，或已经被释放了，则会抛出异常。

注意，使用 `delete` 释放后的内存，其值是不可预测的，应当立即将指针赋值为[[nullptr]]以免出问题。

```c++
int *a = new int;
delete a;
a = nullptr;
```