> 天梯赛第一场训练赛
> https://ac.nowcoder.com/acm/contest/50491/C

# 原题

小红有选择困难症。

现在老师给了小红一段数列，要求小红选出最喜欢的几段，但是小红有着选择困难症，所以她想找出几段数字之和相等的。

现在给定一个由 n 位数字组成的序列 $a_1, a_2, \cdots, a_n$。

其中，每个数字都是 $[0,9]$ 之一。

小红现在想考考你，请你判断一下，能否将该数列截断为两个或更多个**非空**部分，且要求每一部分的各位数字之**和**都相等。

例如，350178 可以截断为 3 个部分 350、17、8，并且满足 3+5+0=1+7=8。

所有测试点满足 2≤n≤100，0≤ai≤9

## 输入格式

第一行包含一个整数 n。
第二行包含 n 个数字 $a_1, a_2, \cdots, a_n$，数字之间不含空格。

## 输出格式

如果可以按要求截断数列，则输出 YES，否则输出 NO

## 输入样例 1

```
5
73452
```

## 输出样例 1

```
YES
```

## 输入样例 2

```
4
1248
```

## 输出样例 2

```
NO
```

## 代码限制

时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 262144K，其他语言524288K  
64bit IO Format: %lld

# 思路

求出平均值后遍历匹配即可。做题的时候想多了，以为每一位可以自由组合

# 答案

```c++
#include <iostream>  
  
using namespace std;  
  
int main() {  
    int n;  
    cin >> n;  
    int nums[n], sum = 0;  
    while (getchar() != '\n');  
    for (int i = 0; i < n; ++i) {  
        nums[i] = getchar() - '0';  
        sum += nums[i];  
    }  
  
    bool ok = false;  
    for (int i = 2; i <= n && !ok; ++i) {  
        ok = sum % i == 0;  
        if (ok) {  
            int ave = sum / i, add = 0, ptr = 0;  
            for (int j = 0; j < i - 1; ++j) {  
                while (add < ave) {  
                    add += nums[ptr++];  
                }  
                if (add > ave) {  
                    ok = false;  
                    break;
                }  
                add = 0;  
            }  
        }  
    }  
  
    cout << (ok ? "YES" : "NO");  
    return 0;  
}
```