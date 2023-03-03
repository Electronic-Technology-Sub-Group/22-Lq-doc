# 基础算法

## 排序

### 快排

1. 取分界
	- 取左右边界
	- 取中间点
	- 随机位置
2. 调整区间，使分界点左右全部大于等于/小于等于分界点
3. 对分界点左右两个区间递归执行 2 操作

```c++
template<typename T>
void qsort(T *values, int r, int l = 0) {
    if (l >= r) return;

    T &x = values[l];

    int i = l - 1, j = r + 1;
    while (i < j) {
        while (values[++i] < x);
        while (values[--j] > x);
        if (i < j) std::swap(values[i], values[j]);
    }
    qsort(values, j, l);
    qsort(values, r, j + 1);
}
```

### 快速选择

可以以 $O(n)$ 的复杂度查找第 k 小的数：在快排的基础上，取消对不符合要求部分的排序，逐渐逼近 k 即可

```c++
template<typename T>
bool qsearch(T *values, int k, int r, int l = 0) {
    if (l > r) return false;
    if (l == r) return true;

    T &x = values[l];

    int i = l - 1, j = r + 1;
    while (i < j) {
        while (values[++i] < x);
        while (values[--j] > x);
        if (i < j) std::swap(values[i], values[j]);
    }
    int sl = j - l + 1;
    if (k <= j ) return qsort(values, k, j, l);
    else return qsort(values, k - sl, r, j + 1);
} 
```

### 归并排序

1. 确定分界点，通常选取序列中点
2. 递归，对数组左右两边递归排序
3. 归并，将排序后的两个序列合并

```c++
template<typename T>
void msort(T *v, T* tmp, int r, int l = 0) {
    if (l >= r) return;

    int mid = (l + r) / 2;
    msort(v, tmp, mid, l);
    msort(v, tmp, r, mid + 1);

    int i = l, j = mid + 1, p = l;
    while (i <= mid && j <= r) {
        if (v[i] <= v[j]) tmp[p++] = v[i++];
        else tmp[p++] = v[j++];
    }
    while (i <= mid) tmp[p++] = v[i++];
    while (j <= r) tmp[p++] = v[j++];

    for (int k = l; k <= r; ++k) v[k] = tmp[k];
}
```

### 数对

![[Pasted image 20230225164918.png]]

分治：数对包含三种情况，假设红色与绿色两种情况都会在归并过程中计算到，则在两侧递归完成后，只需要再统计黄色情况即可

## 整数二分

整数二分的本质是给定一个边界性质使区间一分为二，使两个区间一个符合要求，一个不符合要求

有单调性的题目一定可以二分，可使用二分的题目不一定有单调性

```c++
int bsearch1(int l, int r, const std::function<bool(int)>& check) {
    int mid;
    while (l < r) {
        mid = (l + r + 1) / 2;
        if (check(mid)) l = mid;
        else r = mid - 1;
    }
    return l;
}

int bsearch2(int l, int r, const std::function<bool(int)>& check) {
    int mid;
    while (l < r) {
        mid = (l + r) / 2;
        if (check(mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
```

模板说明：使用时，先确定 check 函数，根据 check 确定更新方法是 `l=mid` 还是 `r=mid`
- 当使用 `l=mid` 时，另一种情况一定是 `r=mid-1`，此时 mid 需要补 +1
- 当使用 `r=mid` 时，另一种情况一定是 `l=mid+1`，此时 mid 不需要补 +1
- 二分模板一定是有解的（check 通过），但解不一定完全是题目解（AcWing 789 题目）

## 高精度

高精度即大整数运算，大整数指位数不小于 $10^6$ 正的整数，即大小在 $10^{10^6}$ 数量级的数值运算，常见有大整数之间的加减法和大整数与小整数的乘除法
- 利用数组（list，vector，。。。）存储每一位（节）值，下标 0 存个位便于进位
- 模拟人工运算 

### 大整数 + 大整数

```c++
vector<int> add(vector<int> &a, vector<int> &b) {
    vector<int> ret;
    auto size = max(a.size(), b.size());
    ret.reserve(size + 1);
    int sum = 0;
    for (int i = 0; i < size; ++i) {
        if (i < a.size()) sum += a[i];
        if (i < b.size()) sum += b[i];
        ret.push_back(sum % 10);
        sum /= 10;
    }
    if (sum) ret.push_back(sum);
    return ret;
}
```

### 大整数 - 大整数

大整数减法本身只考虑整数运算且结果不小于 0 的情况，因此附带大小检查方法

```c++
// 比较是否有 a >= b
bool comp(vector<int> &a, vector<int> b) {
    if (a.size() > b.size()) return true;
    if (a.size() < b.size()) return false;
    for (int i = a.size() - 1; i >= 0; --i) {
        if (a[i] != b[i]) return a[i] > b[i];
    }
    return true;
}

// 假定 a >= b
vector<int> sub(vector<int> &a, vector<int> &b) {
    vector<int> ret;
    ret.reserve(a.size());
    int t = 0;
    for (int i = 0; i < a.size(); ++i) {
        t = (i < b.size() ? a[i] - b[i] : a[i]) - t;
        ret.push_back((t + 10) % 10);
        if (t < 0) t = 1;
    }
    while (ret.size() > 1 && ret.back() == 0) ret.pop_back();
    return ret;
}
```

### 大整数 x 小整数

```c++
// 这里应保证 b > 0
vector<int> mul(vector<int> &a, int b) {
    vector<int> ret;
    ret.reserve(a.size());
    int t = 0;
    for (int i = 0; i < a.size() || t; ++i) {
        if (i < a.size()) t += a[i] * b;
        ret.push_back(t % 10);
        t /= 10;
    }
    return ret;
}
```

### 大整数 ÷ 小整数

```c++
vector<int> div(vector<int> &a, int b, int &r) {
    vector<int> ret;
    r = 0;
    for (int i = a.size() - 1; i >= 0; --i) {
        r = r * 10 + a[i];
        a.push_back(r / b);
        r %= b;
    }
    reverse(a.begin(), a.end());
    while (ret.size() > 1 && ret.back() == 0) ret.pop_back();
    return ret;
}
```

## 前缀和与差分

### 前缀和

前缀和用于在一次计算取得区间元素的和差

```c++
// 前缀和
// 下标从 1 到 n，0 用处
int a[n + 1];
int sum[n + 1] {0};
for (int i = 1; i < n; ++i)
    sum[i] = sum[i - 1] + a[i];
// 二维前缀和
int a[n + 1][m + 1];
int sum[n + 1][m + 1];
for (int i = 0; i <= n; ++i) sum[i][0] = 0;
for (int i = 0; i <= m; ++i) sum[0][i] = 0;
for (int i = 1; i <= n; ++i)
    for (int j = 1; j <= m; ++j)
        sum[i][j] = a[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
```

一维前缀和可以使用 `partial_sum` 计算：![[cpp 数学计算#partial_sum]]
### 差分

差分是前缀和的逆运算。差分表示为：已知数列 $a_n = \{ a_1, a_2, \cdots, a_n \}$，构造数组 $b_n=\{b_1, b_2, \cdots, b_n\}$，使 $a_i=b_1+b_2+\cdots +b_i$，称 $a_n$ 是 $b_n$ 的前缀和，$b_n$ 是 $a_n$ 的差分，$b_i=a_i-a_{i-1}$

差分的作用在于快速大量的对原数组连续区间内所有数进行加减运算的操作
1. 以 $O(n)$ 的复杂度求取原数组 $\{a_n\}$ 的差分数组 $\{b_n\}$
2. 对 $\{b_n\}$ 对应元素进行加减操作，$b_i=b_i+C$ 操作对应 $a_i, a_{i+1}, \cdots, a_n$ 每个元素都加 C
3. 以 $O(n)$ 的复杂度求取计算后的数组的前缀和 $\{c_n\}$

## 二进制位

### 求第 k 位是几

1. 将第 k 位移至末尾：位移操作
2. 取第 k 位数字：与操作

```c++
int getK(int n, int k) {
    return n >> k & 1;
}
```

### lobit

返回的是数字的最后一位 1 及之后的数字
- 树状数组
- 统计 1 的个数

| x            | lobit(x)   |
| ------------ | ---------- |
| $(1010)_2$   | $(10)_2$   | 
| $(101000)_2$ | $(1000)_2$ |

```c++
int lobit(int x) {
    return x & (-x);
}
```

- `-x` = `~x+1`
- `x & (-x)` = `x & (~x+1)`

## 整数离散化

当给定一组数的值域特别大，但数据本身个数不多，可通过将其映射到较小的值域中
- 给定元素可能存在重复元素 - 去重，使用 `unique()` 方法
- 离散化后的值 - 保序离散化，二分查找某一值的下标

## 区间合并

1. 按区间左端点排序
2. 扫描区间，对当前区间可合并区间进行合并

# 数据结构

## 静态链表

通常不使用动态链表（指针实现的链表，`new` 操作非常慢），而是使用数组模拟的链表，这种链表称为静态链表

### 单链表

单链表多用于写邻接表，用于存储图和树

```c++
int e[1000010], ne[1000010];
int head = -1, idx = 0;

void posh_front(int x) {
    e[idx] = x;
    ne[idx] = head;
    head = idx++;
}

void insert(int x, int k) {
    e[idx] = x;
    ne[idx] = ne[k];
    ne[k] = idx++;
}

void erase_next(int k) {
    ne[k] = ne[ne[k]];
}
```

### 邻接表

邻接表用于表示图，实际是多个单链表

### 双链表

双链表多用于优化某些问题

```c++
int e[1000010], l[1000010], r[1000010];
int head = -1, idx = 0;

void posh_front(int x) {
    e[idx] = x;
    l[idx] = -1;
    r[idx] = head;
    head = idx++;
}

void insert_after(int x, int k) {
    e[idx] = x;
    r[idx] = r[k];
    l[idx] = k;
    l[r[k]] = idx;
    r[k] = idx++;
}

void insert_before(int x, int k) {
    insert_after(x, l[k]);
}

void erase(int k) {
    if (l[k] != -1) r[l[k]] = r[k];
    if (r[k] != -1) l[r[k]] = l[k];
}
```

### 栈

先进后出

```c++
int stk[100010];
int tt = -1;

void push(int x) {
    stk[++tt] = x;
}

void pop() {
    tt--;
}

bool empty() {
    return tt >= 0;
}

int v() {
    return stk[tt];
}
```

### 队列

先进先出

```c++
int que[100010];
int hh = 0, tt = -1;

void enqueue(int x) {
    que[++tt] = x;
}

void dequeue() {
    que[--tt];
}

bool empty() {
    return hh >= tt;


int v() {
    return que[hh];
}
```

### 单调栈、队列

抽象模型：
 - 单调栈：给定一系列数据，找到一个数左边第一个比它小的数（AcWing 830 单调栈）
 - 单调队列：求滑动窗口中的最大最小值（AcWing 154 滑动窗口）

思路：
1. 模拟使用朴素算法暴力解决问题的方法
2. 检查内层遍历元素，删除/跳过无效元素，维持栈/队列单调性

## KMP 字符串

KMP 主要优化以下情况：给定字符串 s 和模板字符串 t，求 s 中包含多少个 t

优化方法：字符串匹配失败后，模板串向前挪动尽可能多的位置，而原串位置指针不变

![[Pasted image 20230226110603.png]]

蓝色串是字符串 s，红色串是模板串 t，当匹配失败后模板移动到黑色箭头指向位置，且字符串 s 的指针（绿色点）不动，其前提是需要满足以下橙色框中字符串相同

![[Pasted image 20230226112136.png]]

经过通过预处理（下面[[#next 数组]]小节）后，可以以 $O(1)$ 的时间直接获取模板前移长度 j，匹配所用总时间复杂度为 $O(len(s)+len(t))$，并占用额外 $O(len(t))$ 的空间复杂度

#### next 数组

*`next` 变量在 c++ 某些头文件中已被定义，可命名为 `ne`*

要想实现 KMP 算法，我们需要求出当模板 t 任意位置匹配失败时，允许模板前移的个数 j，j 取值只与模板串 t 和失败位置有关，将其关联成一歌数组称为 `next` 数组，记为 `j = next[i]`，i  失败位置，设以 i 为终点的后缀字符串为 t'，可知 `next[i]` 表示 t' 中与 t' 的一个前缀完全相同的最长后缀长度。

```c++
const char *str, *pat;
const int lenStr, lenPat;
int ne[lenPat];

// 求取 next[i]
// 此处稍后再说

// 假定 ne[lenPat] 已经求得
for (int i = 1, j = 0; i <= lenStr; ++i) {
    while (j && str[i] != pat[j + 1]) j = ne[j];
    if (str[i] == pat[j + 1]) j++;
    if (j == lenPat) {
        int p = i - lenPat + 1;
        // 匹配成功，p 是匹配成功的第一个字符在 str 中的索引
        
        j = 0;
    }
}
```

在计算 `next[]` 数组时，完全可以套用这套公式，原理类似，且要找 `next[i]` 时 `next[1]` 到 `next[i - 1]` 之前的一定已经计算出来了

```c++
// str, pat 是从下标 1 开始保存的字符串
vector<int> kmp(const char* str, const char* pat, int lenStr, int lenPat) {
    vector<int> ret;
    int ne[lenPat];
    memset(ne, 0, lenPat * 4);

    for (int i = 2, j = 0; i <= lenPat; ++i) {
        while (j && i && pat[i] != pat[j + 1]) j = ne[j];
        if (pat[i] == pat[j + 1]) j++;
        ne[i] = j;
    }

    for (int i = 1, j = 0; i <= lenStr; ++i) {
        while (j && str[i] != pat[j + 1]) j = ne[j];
        if (str[i] == pat[j + 1]) j++;
        if (j == lenPat) {
            // 这里是匹配成功的操作
            ret.push_back(i - lenPat + 1);
            j = 0;
        }
    }
    return ret;
}
```

*AcWing831 KMP字符串*

## Trie 树

用于高效存储、查找字符串集合的数据结构，要求所有串中字符类型不多；同样的，也可以用于存储可以分为有限种类的大量不同数据并快速查找

Trie 树使用树结构存储字符串的每一个字符，并在存在字符串的位置做一个标记

![[Pasted image 20230227082808.png]]

```c++
struct Trie {
    const static int N = 100010;

    int son[N][26]{}, cnt[N]{}, idx = 0;

    Trie() {
        memset(son, 0, 26 * N * 4);
        memset(cnt, 0, N * 4);
    }

    void insert(const char *str) {
        int p = 0;
        for (int i = 0; str[i]; ++i) {
            if (!son[p][str[i] - '0']) p = ++idx;
            p = son[p][str[i] - '0'];
        }
        cnt[p]++;
    }

    int count(const char *str) {
        int p = 0;
        for (int i = 0; str[i]; ++i) {
            if (!son[p][str[i] - '0']) return 0;
            p = son[p][str[i] - '0'];
        }
        return cnt[p];
    }
};
```

## 并查集

可以在近乎 $O(1)$ 的时间复杂度内快速合并集合和查询两个元素是否在同一集合中
- 初始时，每个元素位于一个单独的集合中，每个集合以树的形式存储元素
- 每个节点除了存储其本身值外还存储其父节点的值（索引）
- 每个集合对应树的父节点不同，树根父节点存储的就是当前树的编号

操作：
- 判断树根：父节点等于其本身
- 求 x 集合编号：递归查询父节点编号，直到树根
- 合并集合：查询两棵树的树根，将其中一个的父节点更改为另一个的值

路径压缩：
在以上操作中，在每次查询操作中，将被查询元素及其路径上的点的父节点全部设置为根节点，使树的高度大致维持在 2，实现多次查询有近似 $O(1)$ 的时间复杂度

按秩合并：
#todo 
优化效果不明显，基本没啥用

```c++
struct UnionSet {
    const static int N = 100010;
    int belong[N];
    UnionSet() {
        for (int i = 0; i < N; ++i) {
            belong[i] = i;
        }
    }
    
    int query(int x) {
        if (belong[x] != x)
            // 路径压缩
            belong[x] = query(belong[x]);
        return belong[x];
    }
    
    bool same(int x, int y) {
        return query(x) == query(y);
    }
    
    void merge(int x, int y) {
        if (!same(x, y)) belong[query(x)] = query(y);
    }
};
```

## 堆

堆是一个完全二叉树，对于每一个子树，根节点都是所有子树的最大/小值，可快速查找最大/最小值，以数组模拟存储

```c++
struct MinHeap {
    const static int N = 10000;

    int heap[N], len;

    void up(int x) {
        if (x > 1 && heap[x] < heap[x / 2]) {
            swap(heap[x], heap[x / 2]);
            up(x / 2);
        }
    }

    void down(int x) {
        int t = x;
        if (x * 2 <= len && heap[x * 2] < heap[t]) t = x * 2;
        if (x * 2 + 1 <= len && heap[x * 2 + 1] < heap[t]) t = x * 2 + 1;
        if (t != x) {
            swap(heap[x], heap[t]);
            down(t);
        }
    }

    void insert(int x) {
        heap[++len] = x;
        up(len);
    }

    void remove(int x) {
        heap[x] = heap[--len];
        down(x);
        up(x);
    }

    void set(int v, int x) {
        heap[x] = v;
        down(x);
        up(x);
    }

    int min() {
        return heap[1];
    }

    // 以 O(n) 时间复杂度调整整个堆
    void adjust() {
        for (int i = len / 2; i; --i) down(i);
    }
};

```

调整整个堆的时间复杂度为 $O(n)$，适用于建堆，一次性输入所有元素后调整。其余操作时间复杂度均为 $O(log(n))$

## Hash 表

将一个庞大的值域映射到一个比较小的值域范围

通过映射方法产生的新值发生冲突时，通常使用拉链法或开放寻址解决。

一般可以认为每个哈希值对应的元素个数都是常数个。且通常只会进行插入和查询操作

### Hash 方法

数字：通常使用模运算作为映射方法，取离 2 的整数次幂尽可能远质数操作时冲突概率最小。
字符串：前缀 hash，可用于判定字符串或其字串相同
 1. 预处理字符串所有前缀的 hash 值
	 1. 将连续前缀看成一个 p 进制的数，将每个字符看成数字，一般情况下不能将字符看成 0
	 2. 将其转换成十进制
	 3. 对一个相对较小的数 q 取模
	 4. 一般情况下，p=131 或 1331，$q=2^{64}$（直接使用 unsigned long long 存储，溢出即取模），出现冲突的概率很小
 2. 子串哈希：已知字符串其所有的 n 位前缀前缀哈希 $h[n]$，求从第 L 到 R 位字符构成的字串的哈希：$h[R]-h[L]\times p^{R-L+1}$

### 拉链法

使用数组模拟链表存储数据

![[Pasted image 20230227164732.png]]

```c++
struct Hash {
    const static int N = 100003;

    int v[N];
    // list
    int e[N], ne[N], idx = 1;

    Hash() {
        memset(v, -1, sizeof v);
    }

    int hash(int x) {
        return (x % N + N) % N;
    };

    void insert(int x) {
        int k = hash(x);
        ne[idx] = v[k];
        e[idx] = x;
        v[k] = idx++;
    }

    bool find(int x) {
        int k = hash(x);
        for (int i = v[k]; i != -1; i = ne[i]) {
            if (e[i] == x) return true;
        }
        return false;
    }
};

```

### 开放寻址法

![[Pasted image 20230227174539.png]]

开放寻址法的模通常需要取到给定数据量的 2-3 倍的质数，数组也要有 2-3 倍以尽可能避免冲突。开放寻址法在模后若遇到冲突，直接插入对应位置之后即可。

```c++
struct Hash {
    // null: 一个绝对娶不到的值
    const static int N = 300007, null = 0x3f3f3f;

    int v[N];

    Hash() {
        memset(v, 0x3f, sizeof v);
    }

    int hash(int x) {
        return (x % N + N) % N;
    };

    int findIdx(int x) {
        int k = hash(x);
        while (v[k] != null && v[k] != x) {
            k++;
            if (k == N) k++;
        }
        return k;
    }

    int find(int x) {
        return v[findIdx(x)] == x;
    }

    void insert(int x) {
        v[findIdx(x)] = x;
    }
};
```

## STL

vector：数组倍增实现变长数组
- 倍增：操作系统分配空间时，与申请的空间大小关系不大，与申请次数有关，因此应尽可能少请求空间次数
deque：双端队列

queue：队列
stack：栈
priority_queue：优先队列（堆，默认大根堆）
- pop()，push()，top()
- 没有 clear()

set, map, multiset, multimap：平衡二叉树（$O(log(n))$）
- upper_bound：返回大于 x 的最小数
- lower_bound：返回大于等于 x 的最小数
- count()
unordered_set, unordered_map, unordered_multiset, unordered_multimap：哈希表（$O(1)$）
- count()

bitset：位压，代替 `bool[]` 省空间
- 所有位运算、比较、位移
- `[]`
- count()
- any()，none()
- set()，reset()：将所有位设为 1/0
- set(k, v)
- flip()，flip(k)
pair：二元组
- make_pair(a, b)
- {a, b}

支持比较运算：
vector：字典序
pair：以 first 为第一关键字，second 为第二关键字

# 图论

树与图

## DFS BFS

都可以对整个空间搜索
- DFS
	- 使用 stack 存储，额外占用 $O(h)$ 空间
	- 不具有最短路性质
	- 适用于直接暴力搜索，或内存空间比较紧张的情况
- BFS
	- 使用 queue 存储，额外占用 $O(2^h)$ 空间
	- 具有最短路性质
	- 当所有边权重都为 1 时可用于求最短路、拓扑序

## 拓扑序列

> 拓扑序列：若一个由图中所有点构成的序列 A 满足 对于图中每条边 `(x, y)`，x 总是在 y 之前出现，即所有边都是从前指向后的，则 A 为图的一个拓扑序列
> 
> 并非所有图都有拓扑序，只要存在环的有向图一定没有拓扑序列；有向无环图一定存在至少一个拓扑序，因此有向无环图又称拓扑图

> 入度：图中指向某个节点的路径个数称为该点的入度

> 出边：图中从某个节点出发的路径
> 出度：图中某个点出边的个数称为该点的出度

 1. 将入度为 0 的点入队：所有入读为 0 的点都可以作为一个拓扑序列的起始点
 2. 枚举队首 t 的所有出边删除 t 的所有到 j 的出边，若 j 的出度为 0，入队 j
 3. 只要有环，一定存在点没有入队；一个无环图一定可以将所有点入队（有向无环图一定存在入度为 0 的点）

## 最短路

约定：n 表示点数，m 表示边数

最短路题目重点在于如何建图，如何定义点和边

![[Pasted image 20230301204243.png]]

### 单源最短路

一个点到其他所有点的最短距离

#### 边权全正

dijkstra 算法基于贪心

##### 朴素 dijkstra 算法

时间复杂度 $O(n^2)$，适合于边数多的稠密数组
1. 初始化距离数组 `dist[]`，`dist[i]=∞`，`dist[0]=1`，表示起点到 i 点的最短距离
2. 定义集合 S 表示已确定最短距离的点
3. 对每个节点遍历，取不在 S 中的每个距离最近的点 t
4. 将 t 插入 S，对于 t 的所有出边 x，且 `dist[x] > dist[t] + w` 时，更新 `dist[x] = dist[t] + w`
5. 重复 n 次 3，4，使所有点都在 S 中，查询结束

```c++
/// n: 点的个数
/// dist: 从 0 号点到 i 号点的最短距离
/// ways: 第 i 号点的所有出边。重边则只保留最短的边
/// weights: 第 i 号点每条边所对应的权重（长度）
void dijkstra(int n, int *dist, vector<int> *ways, int **weights) {
    bool st[n];
    memset(dist, 0x3f, n * 4);
    memset(st, 0, n * sizeof(bool));
    dist[0] = 0;

    for (int i = 0; i < n; ++i) {
        int t = -1;
        for (int j = 0; j < n; ++j)
            if (!st[j] && (t == -1 || dist[j] < dist[t]))
                t = j;
        if (t == n) break;
        st[t] = true;
        for (int j = 0; j < ways[t].size(); ++j)
            dist[j] = min(dist[j], dist[t] + weights[t][j]);

    }
}
```

##### 堆优化 dijkstra 算法

时间复杂度 $O(mlog n)$，只是将朴素 dijkstra 中搜索最短距离的部分使用小根堆实现，适用于稀疏图

*使用 STL 优先队列实现堆，相比起手写堆，内部元素有 m 个，总时间复杂度为 $O(mlogm)$*
*但由于使用该算法时通常 $m<n^2$，其时间复杂度在数量级上与手写堆同级*

```c++
/// n: 点的个数
/// dist: 从 0 号点到 i 号点的最短距离
/// ways: 第 i 号点的所有出边
/// weights: 第 i 号点每条边所对应的权重（长度）
void dijkstra(int n, int *dist, vector<int> *ways, vector<int> *weights) {
    bool st[n];
    memset(dist, 0x3f, n * 4);
    memset(st, 0, n * sizeof(bool));
    dist[0] = 0;

    using PII = pair<int, int>;
    priority_queue<PII, vector<PII>, greater<>> heap;
    heap.emplace(0, 1);
    while (!heap.empty()) {
        auto &p = heap.top();
        int t = p.first, d = p.second;
        heap.pop();

        if (st[t]) continue;
        st[t] = true;

        for (int i = 0; i < ways[t].size(); ++i) {
            int j = ways[t][i], w = weights[t][i];
            if (dist[j] > d + w) {
                dist[j] = d + w;
                heap.emplace(dist[i], j);
            }
        }
    }
}
```

#### 存在负权边

##### Bellman-Ford

时间复杂度 $O(mn)$，基于离散数学
1. 遍历所有边，对每条边设从 a 指向 b，权重为 w
2. 松弛操作：对所有边，更新 `dist[b] = min(dist[a] + w)`
3. 迭代 n 次 1，2 步。
4. **第 k 次迭代表示从起点经过不超过 k 条边到达终点的距离**

负权回路判断：
- 第 n 次迭代时仍有路径点更新，表示路径到更新点之间一定存在负权回路

**存在负权回路的情况下不一定有最短路**

```c++
void bellman_ford(int n, int *dist, vector<int>* ways, int **weights) {  
    memset(dist, 0x3F, n * sizeof(int));  
    dist[0] = 0;  
    for (int i = 0; i < n; ++i) {  
        for (int p0 = 0; p0 < n; ++p0) {  
            for (int j = 0; j < ways[p0].size(); ++j) {  
                int p1 = ways[p0][j];  
                if (dist[p1] > dist[p0] + weights[p0][j]) {  
                    dist[p1] = dist[p0] + weights[p0][j];  
                }  
            }  
        }  
    }  
}
```

使用场景：**求经过最多 k 条边后到达终点的最短距离**
- 每次迭代只用上次的结果，以免发生串联，因此每次迭代之前需要备份所有距离
- 最后检查距离时，不存在的边不应直接检查 `0x3f3f3f3f`，由于负权边会使距离无穷的点去更新另一个点，使距离小于 `0x3f3f3f3f` 但不会小太多，可使用 `0x3f3f3f3f/2`
![[Pasted image 20230303073737.png]]

##### SPFA

时间复杂度最坏 $O(mn)$，一般情况下是 $O(m)$ 且优于 Bellman-Ford 算法
- 在 Bellman-Ford 算法基础上，剔除不需要更新的点
- 创建一个队列，堆里面保存每次距离变小的点 t，更新 t 的所有出边，被更新变小的点入队

只要没有负环就可以用 SPFA。但某些情况，如求不超过 k 条边的最短路，不能用 SPFA

```C++
void spfa(int n, int *dist, vector<int>* ways, int **weights) {
    bool st[n]; // 避免重复向队中插入元素
    memset(dist, 0x3F, n * sizeof(int));
    memset(st, 0, sizeof st);
    dist[0] = 0;
    queue<int> q; // 待更新节点
    q.push(0);
    st[0] = true;
    
    while (!q.empty()) {
        int p0 = q.front();
        q.pop();
        st[p0] = false;
        for (int i = 0; i < ways[p0].size(); ++i) {
            int p1 = ways[p0][i];
            if (dist[p1] > dist[p0] + weights[p0][i]) {
                dist[p1] = dist[p0] + weights[p0][i];
                if (!st[p1]) {
                    q.push(p1);
                    st[p1] = true;
                }
            }
        }
    }
}
```

如果题目数据没有特别卡数据，SPFA 大多数情况下可以过 dijkstra 算法的题目且且比他更快。被卡时只能用堆优化版 dijkstra（多见于稀疏网格图）

SPFA 也可以用于判断负环，原理与 bellman-Ford 算法相同

```C++
/// spfa 判断负环
/// return: 存在负环则返回 true
bool spfa_ring(int n, vector<int>* ways, int **weights) {
    bool st[n];
    int cnt[n], dist[n];
    memset(dist, 0, sizeof dist);
    queue<int> q;
    for (int i = 0; i < n; ++i) {
        st[i] = true;
        q.push(i);
    }
    cnt[0] = 0;
    
    while (!q.empty()) {
        int p0 = q.front();
        q.pop();
        st[p0] = false;
        for (int i = 0; i < ways[p0].size(); ++i) {
            int p1 = ways[p0][i];
            if (dist[p1] > dist[p0] + weights[p0][i]) {
                dist[p1] = dist[p0] + weights[p0][i];
                cnt[p1] = cnt[p0] + 1;
                if (cnt[p1] >= n) return true;
                if (!st[p1]) {
                    q.push(p1);
                    st[p1] = true;
                }
            }
        }
    }
    return false;
}
```

### 多源汇最短路

> 源点：起点
> 汇点：终点

起点和终点不确定，使用 Floyd 算法，时间复杂度 $O(n^3)$，同样要求不能有负权回路

原理：基于动态规划，状态为 `d[k][i][j]` 表示从 i 到 j 只经过 `0-k` 的中间点所用的最短距离

1. 使用邻接矩阵存储图的所有边 `d[i][j]`
2. 三重 n 次循环 k, i, j 更新距离 `d[i][j]=min(d[i][j], d[i][k]+d[k][j])`

```c++
void floyd(int n, int **dist) {
    for (int k = 0; k < n; ++k) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
            }
        }
    }
}
```

该算法也存在负权边更新无穷的问题，同样使用 `0x3F3F3F3F / 2` 判断

### 总结

1. 多源汇只能用 Floyd 算法，通常 n 很小（几百）
2. m、n 都很大（10w），一般选择 SPFA 或堆优化 dijkstra 算法
3. 稠密图，n 较小 m 很大，接近 $m=n^2$，朴素 dijkstra
4. 求不超过 k 个节点，使用 Bellman-Ford 算法
5. 求是否有负权回路，使用SPFA

## 最小生成树

### 朴素 Prim 算法

时间复杂度 $O(n^2)$，适用于稠密图

```C++

```

### 堆优化 Prim 算法

时间复杂度 $O(mlogn)$，不常用，Kruskal 算法思路更清晰

优化方式与 dijkstra 相似，使用堆优化排序步骤

### Kruskal 算法

时间复杂度 $O(mlogm)$，大多数时间花费在排序上，常用于稀疏图

```C++

```

## 二分图

### 染色法

DFS，用于判断二分图，时间复杂度为 $O(m+n)$

### 匈牙利法

用于求二分图的最大匹配，最坏情况下为 $O(mn)$，但实际运行时间远小于最坏值

 > 扩展：最大流算法，二分图是最大流的一个特例

# 数学
 
# 动态规划

# 贪心
 
# 复杂度分析