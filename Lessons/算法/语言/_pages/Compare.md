`Compare` 表示比较函数，C++ 的比较函数一般满足以下关系：
- 接收两个相同类型参数，均由 `const &` 修饰
- 返回 `bool` 类型值或可隐式转换为 `bool`，且满足
	- 总是满足 `comp(a, a) = false`
	- 若 `comp(a, b) = true`，则 `comp(b, a) = false`
	- 若 `comp(a, b) && comp(b, c)` ，则 `comp(a, c)`
- 返回值为 `true` 时表示第一参数先于第二参数