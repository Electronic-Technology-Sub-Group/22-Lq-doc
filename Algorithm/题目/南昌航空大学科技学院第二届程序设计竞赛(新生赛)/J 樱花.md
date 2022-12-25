
链接：[https://ac.nowcoder.com/acm/contest/48725/J](https://ac.nowcoder.com/acm/contest/48725/J)  
来源：牛客网  
  
# 原题

## 题目描述

**爱意东升西落**

求方程：
$$
\dfrac{1}{x} + \dfrac{1}{y} = \dfrac{1}{n!}
$$
的正整数解的组数，答案对 $10^9+7$ 取模

## 输入描述:

输入只有一行一个整数，表示 $n$。

## 输出描述:

输出一行一个整数表示正整数解的组数模 $10^9+7$ 的值。

**示例1**

## 输入

```
2
```

## 输出

```
3
```

## 说明

> 样例一：共有三个数对 $(x,y)$ 满足条件，分别是 $(3,6)$, $(4,4)$ 和 $(6,3)$。
> 
> 对于 100% 的数据，保证 $1≤n≤10^6$。

**示例2**

## 输入

```
1439
```

## 输出

```
102426508
```

# 思路

## 定义及公式

> 标准分解式：将质因数分解的结果，按质因数大小从小到大排列，并将相同的质因数的连乘积以指数形式表示，这种形式称为标准分解式

> 算术基本定理：任何一个大于 1 的自然数 N，若 N 不为素数， N 可以唯一分解成有限个质数的乘积，且分解出的形式是 N 的标准分解式

设 $N>1$，且 N 不是质数，则存在质数数列 $\{P_n\}$，正整数数列 $\{a_k\}$，满足

$$
N=\prod_{i=1}^kP_i^{a_i}
$$

而对于阶乘，设 $\{P_k\}$ 为 $n!$ 的素数质因数数列，在 $n!$ 的标准分解式中，有 $P_m≤n<P_{m+1}$，$k ≤ m$，素数 $P_k$ 的最高指数为
$$
P_k(n!) = \sum_{i=1}^k[\dfrac{n}{P_i}]
$$
则 $n!$值为
$$
n! = \prod_{i=1}^mP_i^{P_i(n!)}
$$

## 化简

将等式通分化简可得：
$$
xy-n!(x+y)=0
$$
等式两边同时加上 $(n!)^2$，分解因式得
$$
(n!)^2-n!(x+y)+xy=(n!)^2
$$
$$
(x-n!)(y-n!)=(n!)^2
$$
将等式右边 $(n!)^2$ 以算数基本定理拆分得
$$
c_k=P_k(n!)=\sum_{i=1}^k[\dfrac{n}{P_i}]
$$
$$
(x-n!)(y-n!)=\prod_{i=1}^kP_1^{2\times c_i}
$$

因此，$(x-n!)$ 有 $\prod_{i=1}^k(2\times c_i+1)$ 种取值，方程组也有这么多组解。

为防止数据溢出，可以对每一次计算都进行取模

# 答案

```c++
#include <iostream>  
#include <algorithm>  
  
using namespace std;  
  
int main() {  
    int n;  
    cin >> n;  
  
    // 质数筛筛出不超过 n 的质数个数  
    // 数字 i 的最小质因数, 1<=i<=n  
    int v[n + 1];  
    for (auto &i: v) i = 0;  
    // 筛出来的质数集  
    int prime[n + 1];  
    int count = 0;  
    // 线筛  
    for (int i = 2; i <= n; ++i) {  
        if (v[i] == 0) {  
            prime[++count] = i;  
            v[i] = i;  
        }  
        for (int j = 1; j <= count   
                        // 数字 i 已被更小的指数标记过，则 i 的倍数必然有跟小的质数  
                     && prime[j] <= v[i]   
                        // 越界  
                     && i * prime[j] <= n; ++j) {  
            v[i * prime[j]] = prime[j];  
        }  
    }  
  
    // 计算质数的指数 c
    int c[n + 1];  
    for (auto &i: c) i = 0;  
    for (int i = 1; i <= n; ++i) {  
        for (int j = i; j != 1; j /= v[j]) {  
            c[v[j]]++;  
        }  
    }  
    // 结果  
    long long result = 1;  
    for (int i = 1; i <= n; ++i) {  
        result = (long long) result * (2 * c[i] + 1) % 1000000007;  
    }  
    cout << result;  
    return 0;  
}
```
