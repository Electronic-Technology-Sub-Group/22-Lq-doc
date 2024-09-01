> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/L

难度：Easy
# 题目
  
You are given three permutations $a,b,c$ of length $n$. Recall that a permutation is a sequence of $n$ integers from $1$ to $n$, in which each number occurs exactly once.  
There are three integers $x,y$ and $z$. Initially, all of them equal to $1$. After each second, $x,y,z$ become $a_y,b_z,c_x$ respectively and simultaneously (**note that the subscripts are** $y,z,x$).  
You need to answer multiple independent queries. For each query, you are given three integers $x',y'$ and $z'$. Tell Walk Alone the minimum time needed for tuple $(x,y,z)$ to become $(x',y',z')$ if possible.
## 输入格式
  
The first contains an integer $n\ (1\le n\le 10^5)$, indicating the length of the permutations.  
Each of the next three lines contains nnn integers. The $i$-th number of the second line, the third line and the fourth line indicate $a_i,b_i$ and $c_i\ (1 \le a_i,b_i,c_i \le n)$, respectively.  
The next line contains an integer $Q\ (1\le Q\le 10^5)$, indicating the number of queries.  
Each of the next $Q$ lines contains three integers $x',y'$ and $z'\ (1\le x',y',z'\le n)$.
## 输出格式
  
For each query, output the minimum time needed for $(x,y,z)$ to become $(x',y',z')$ in a line, or $-1$ if impossible.
## 样例
### 输入

```
5
2 1 5 4 3
3 1 4 2 5
5 4 2 1 3
5
1 1 1
2 3 5
5 5 4
3 2 3
1 4 2
```
### 输出

```
0
1
2
3
4
```
## 样例
### 输入

```
10
10 9 3 7 6 2 8 5 1 4
8 9 10 6 2 4 1 7 3 5
10 1 3 5 9 6 2 7 4 8
2
9 7 2
2 9 7
```
### 输出

```
-1
89
```
## 代码限制

时间限制：C/C++ 2秒，其他语言4秒  
空间限制：C/C++ 524288K，其他语言1048576K  
64bit IO Format: %lld
# 解析

给定长度为 n 的序列 a，b，c，三个数字 x，y，z 初始值都为 1。没经过一次变化，有
$$
\begin{cases}
x=a_y\\
y=b_z\\
z=c_x\\
\end{cases}
$$
现进行 Q 次查找，每次给出一个三元组 $x', y', z'$，求该组 x y z 会在第几次变化时出现，如果无法出现则输出 -1，1 1 1 为 0
# 思路

# 答案

```cpp

```