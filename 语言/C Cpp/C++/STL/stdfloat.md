---
语言: cpp
语法类型: 标准库
---
扩展浮点类型，根据 IEEE754 标准产生的浮点类型定义

|类型别名|类型标准|字面量后缀|
| ----------| -------------------| ------------|
|`std::float16_t`|IEEE754 binary16|`f16`|
|`std::bfloat16_t`|标准 BF16|`bf16`|
|`std::float32_t`|IEEE754 binary32|`f32`|
|`std::float64_t`|IEEE754 binary64|`f64`|
|`std::float128_t`|IEEE754 binary128|`f128`|
注意：
* 几种类型永远都不是 `float`，`double`，`long double` 的别名（与 `cstdint` 不同）
* 所有浮点类型与标准浮点类型都可以互相显示转化（`static_cast`）
* 隐式转换：`{float16_t, bfloat16_t} < float32_t = float < float64_t = double < long double < float128_t`
* 其他标准库、特性验证宏中应用到浮点类型的地方都有相应扩展

‍
