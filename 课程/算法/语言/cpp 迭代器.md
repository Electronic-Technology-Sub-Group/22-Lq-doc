![[ForwardIt]]

# 去重

使用 `unique()` 函数可将重复元素集中放置到迭代器尾端，之后可通过 `erase` 方法去重或进行其他处理，位于 `algorithm` 头文件中

```c++
ForwardIt unique(ForwardIt first, ForwardIt last[, BinaryPredicate p]);
```

从来自范围 $[first, last)$ 的相继等价元素组消除首元素外的元素，并返回范围的新 逻辑 结尾的尾后迭代器。通过用覆写要被擦除的元素的方式迁移范围中的元素进行移除。
- 不存在 p 时使用 `==` 运算符判断元素是否相等
- 去重之前应当保证列表中的元素是有序的

```c++
int nums[] {0, 1, 1, 2, 2, 2, 3, 4, 5, 6, 6, 7, 8, 9};
auto *p = unique(nums, nums + sizeof(nums) / sizeof(int));
// 0 1 2 3 4 5 6 7 8 9 6 7 8 9
// 0 1 2 3 4 5 6 7 8 9 [6 7 8 9]
for (const auto &item: nums) cout << item << ' ';
cout << endl;
int pos = p - nums;
// removed after 10
cout << "removed after " << pos;
```

- 可方便使用该方法配合 `erase` 进行去重

```c++
vector<int> nums;
nums.reserve(10);
for (int i = 0; i < 10; ++i) {
    nums.push_back(rand() % 10);
}
sort(nums.begin(), nums.end());
// origin nums: 0 1 2 4 4 4 7 8 8 9
cout << "origin nums:";
for (const auto &item: nums) cout << ' ' << item;
// unique nums: 0 1 2 4 7 8 9
cout << endl << "unique nums:";
auto itr = unique(nums.begin(), nums.end());
nums.erase(itr, nums.end());
for (const auto &item: nums) cout << ' ' << item;
```

# 二分查找

二分查找有 `lower_bound()` 和 `upper_bound()` 函数，位于 `algorithm` 头文件中
- lower_bound：返回指向范围 `[first, last)` 中首个不满足 `element < value` 或 `comp(element, value)` 的元素的迭代器，或者在找不到这种元素时返回 last
- upper_bound：返回指向范围 `[first, last)` 中首个满足 `value < element` 或 `comp(value, element)` 的元素的迭代器，或者在找不到这种元素时返回 last。

范围 `[first, last)` 必须已相对于表达式 `element < value` 或 `comp(element, value)` 划分，即所有令该表达式为 true 的元素必须在所有令该表达式为 false 的元素的前面。

```c++
ForwardIt lower_bound( ForwardIt first, ForwardIt last, const T& value);
ForwardIt lower_bound( ForwardIt first, ForwardIt last, const T& value, Compare comp);
ForwardIt upper_bound( ForwardIt first, ForwardIt last, const T& value);
ForwardIt upper_bound( ForwardIt first, ForwardIt last, const T& value, Compare comp);
```

