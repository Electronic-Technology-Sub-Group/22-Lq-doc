
- `calloc`：类似 `malloc`，但会自动将申请后的内存填充 0
	- `void* calloc(size_t num_elements, size_t element_size);`
- `realloc`：可以扩大或缩小已分配内存块的大小
	- `void* realloc(void* ptr, size_t new_size)`
	- 若扩大时无足够空间，则申请一块足够大的新内存并将原位置数据复制过去，返回新地址
	- 若 `ptr=NULL`，等效于 `malloc`
- `malloc`，`alloc`，`realloc` 失败则会返回 `NULL`