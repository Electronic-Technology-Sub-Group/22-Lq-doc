排序相关方法位于 `algorithm` 头文件中

![[RandomIt]]

![[Compare]]

# sort

```cpp
void sort(RandomIt first, RandomIt last[, Compare comp]);
```

对 $[first, last)$ 范围的数据进行就地排序。迭代元素要求实现 `<` 运算符，或提供 `Compare comp` 比较器

排序结果不稳定，即不保证相同元素排序之后维持原本的顺序；C++ 未指定排序实现，仅要求排序的最坏复杂度为 $O(N\cdot \log N)$

# nth_element

```cpp
void sort(RandomIt first, RandomIt middle, RandomIt last[, Compare comp]);
```

对 $[first, last)$ 范围的数据进行部分的就地排序，使 `middle` 位置的元素与排序好的 `middle` 位置元素相同，即 `middle` 之前的元素总是小于他，之后的元素总是大于他

不保证 `[first, middle)` 和 `(middle, last)` 区间元素之间的大小关系

排序结果不稳定，即不保证相同元素排序之后维持原本的顺序；C++ 未指定排序实现，仅要求排序的平均复杂度为 $O(N)$，常使用内省选择算法（快排+堆排）

# stable_sort

```cpp
void stable_sort(RandomIt first, RandomIt last[, Compare comp]);
```

对 $[first, last)$ 范围的数据进行就地排序。迭代元素要求实现 `<` 运算符，或提供 `Compare comp` 比较器

排序结果稳定，即可以保证相同元素排序之后维持原本的顺序；C++ 未指定排序实现，仅要求排序的最坏复杂度为 $O(N\cdot \log N^2)$；若有额外的空间占用，则复杂度为 $O(N\cdot \log N)$

# partial_sort

```cpp
void partial_sort(RandomIt first, RandomIt middle, RandomIt last[, Compare comp]);
```

对 $[first, last)$ 范围的数据进行部分的就地排序，保证 $[first, middle)$ 范围内的元素是已排序好的，且总是小于 $(middle, last)$，$(middle, last)$ 的顺序不保证

排序结果不稳定，即不保证相同元素排序之后维持原本的顺序；C++ 未指定排序实现，仅要求排序的复杂度约为 $N_1\cdot \log N_2$，$N_1=last-first$，$N_2=middle-first$
