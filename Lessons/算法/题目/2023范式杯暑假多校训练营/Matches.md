> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/H

难度：Easy #todo 
# 题目

Walk Alone has two rows of matches, each of which contains nnn matches. He defines the distance between them as $d=\sum_{i=1}^n |a_i-b_i|$, where aia_iai​ and bib_ibi​ represent the height of the $i$-th match in the first row and the second row, respectively. Walk Alone wants to beautify the matches by shortening the distance, and you are asked to find out the minimum distance after performing **at most one swap within one row**. Note that the height can be negative.
## 输入格式
  
The first line contains one integer $n\ (1\leq n \leq 10^6)$, denoting the number of matches in each row.  
  
The second and the third line both contains nnn integers, representing the height of matches in the first row and the second row, respectively. All of these numbers satisfy $|a_i|, |b_i| \leq 10^{9}$.
## 输出格式
  
Print one line containing a single integer, the minimum distance you can get.
## 输入样例

```
3
1 2 3
3 2 1
```
## 输出样例

```
0
```
## 输入样例

```
4
4 3 9 9
7 3 7 3
```
## 输出样例

```
5
```
## 样例解释
  
In the second example, one of the best strategies is to swap $a_1$​ and $a_4$​, and the minimum distance is $|9-7|+|3-3|+|9-7|+|4-3|=5$.
## 代码限制
  
时间限制：C/C++ 2秒，其他语言4秒  
空间限制：C/C++ 524288K，其他语言1048576K  
64bit IO Format: %lld
# 解析

给定两个数组 a，b，可以选择一个数组交换一对数字也可以不交换，求 $\sum_1^i{|a_i-b_i|}$ 最小是多少
# 思路

设交换 $a_i$ 与 $a_j$，则有
$$
\Delta=|a_i-b_i|+|a_j-b_j|-|a_i-b_j|-|a_j-b_i|
$$
从数轴上看，$a_x$，$b_x$，$a_y$，$b_y$ 交换后，$\Delta$ 变化如下，故而比较反序包络和反序相交两种情况即可

![[Pasted image 20230809144252.png]]

1. 排序：将每组 a b 对应数字组成数对，按 b 组数字从小到大排序
2. 将排好的数对分成两组，$S=\{(a,b)|a<b\}$，$T=\{(a,b)|a>b\}$
3. 按下面的方法计算，二分找区间，找到 $\Delta$ 最大值
	1. 反序相交：任意 S 中的元素，在 T 中找到包络当前区间的区间
	2. 反序包络：任意 S 中的元素，在 T 中找到相交范围的最大线段
# 答案

```c++
#include<bits/stdc++.h>

using namespace std;
typedef long long ll;
typedef pair<ll, ll> pll;

int main() {
    ios::sync_with_stdio(false);
    
    int n;
    cin >> n;
    vector<pll> a(n), tmp;
    for (int i = 0; i < n; ++i) cin >> a[i].first;
    for (int i = 0; i < n; ++i) cin >> a[i].second;

    vector<ll> Sx, Sy, len;
    vector<pll> T;
    ll sum = 0, ans = 0; // ans维护重叠部分，最后答案就是sum-2*ans

    for (auto[x, y]: a) {
        sum += abs(x - y);
        // 分成两类
        if (x < y) tmp.emplace_back(x, y);
        else if (x > y) T.emplace_back(y, x);
    }

    sort(tmp.begin(), tmp.end());
    ll r = -1e18;
    // 消除正向相互包络关系——不难发现正向相互包络关系不会对答案有任何正贡献
    for (auto x: tmp) {
        if (x.second <= r) continue;
        r = x.second;
        Sx.push_back(x.first), Sy.push_back(x.second);
        len.push_back(x.second - x.first);
    }

    auto query = [&](ll x, ll y) {
        ll n = Sx.size(), ret = 0;
        ll l = upper_bound(Sx.begin(), Sx.end(), x) - Sx.begin();
        ll r = lower_bound(Sy.begin(), Sy.end(), y) - Sy.begin();
        if (l > 0) ret = max(ret, min(y, Sy[l - 1]) - x); // 找反序交叉
        if (r < n) ret = max(ret, y - max(x, Sx[r])); // 找反序交叉
        if (r > l) ret = max(ret, *max_element(len.begin() + l, len.begin() + r)); // 找反序包络
        return ret;
    };

    for (auto x: T) ans = max(ans, query(x.first, x.second));
    cout << sum - ans * 2 << endl;
    return 0;
}

```