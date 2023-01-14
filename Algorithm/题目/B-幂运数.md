> 天梯赛第一场训练赛
> https://ac.nowcoder.com/acm/contest/50491/B

# 原题

小明在网上获取知识的时候看到一个关于幂运数的知识点。幂运数：一个数等于另一个数的多次幂运算结果，则称该数为另一个数的幂运数。简单来说就是有两个数 $a$，$b$，当 $a = b\times k$（$k\geq 2$）时，则称 `a` 为 `b` 的幂运数。

小明要考考你，现在给你一个含有 `n` 个数的整数数列 $a_1, a_2, \cdots, a_n$

1. 对于每个数 $a_i$ 要你求出在前 `i-1` 个数中有多少个数是 $a_i$ 的幂运数。
2. 输出 $a_i$ 的幂运数的累加和。  

累加和可能很大输出时请对 99999 取模。

对于100%的数据保证
2 ≤ n ≤ 1e6
2 ≤ ai ≤ 1e8

## 输入格式

第一行一个整数，表示有 n 个数
第二行 n 个数，表示整数数列

## 输出格式

输出 n 行，每行两个整数，表示对于每一个数它所对应的幂运数的个数以及累加和（对 99999 取模）。

## 输入样例 1

```
3
8 4 2
```

## 输出样例 1

```
0 0
0 0
2 12
```

## 样例解释

8 和 4 为 2 的幂运数

## 输入样例 2

```
4
3 3 9 3
```

## 输出样例 2

```
0 0
0 0
0 0
1 9
```

## 代码限制

时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 262144K，其他语言524288K  
64bit IO Format: %lld

# 思路

搞一个 map 记录每个数的出现次数，暴力枚举即可，注意使用 `scanf` 和 `printf` 处理输入输出，否则会产生 TLE

# 答案

```c++
#include <iostream>  
#include <map>  
#include <cstdio>  
  
using namespace std;  
  
int main() {  
    int n;  
    cin >> n;  
    // 记录每个数字出现的次数
    map<int, int> records;  
    int max = 0, num;  
    int count, sum, t;  
    for (int i = 0; i < n; ++i) {  
        scanf("%d", &num);  
        count = 0;  
        sum = 0;  
        if (num <= 1000) {  
            for (t = num * num; t <= max; t *= num) {  
                count += records[t];  
                sum = (sum + t * records[t]) % 99999;  
            }  
        }  
        printf("%d %d\n", count, sum);  
        records[num]++;  
        if (num > max) max = num;  
  
    }  
    return 0;  
}
```
