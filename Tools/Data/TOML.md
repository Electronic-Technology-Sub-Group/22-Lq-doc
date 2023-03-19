# TOML

> Tom 的（语义）明显、（配置）最小化的语言。（Tom's Obvious, Minimal Language）

基础规定：

- TOML 大小写敏感
- TOML 文件必须是 UTF-8 编码
- 一个空白字符可以是一个 Tab（0x09）或空格（0x20）
- 一个换行符可以是 LF（0x0A）或 CRLF（0x0D0A）

TOML 文件使用 `.toml` 作为扩展名，其 MIME 类型为 `application/toml`

## 注释

TOML 支持使用注释。注释通过 # 开头。

```toml
# 这是一条注释，不包含任何内容。
```



## 键值对

键值对是 TOML 文档的基本构成。

```toml
key = "value"
```

键为直接的字面量，值可以是除了空（或注释）外的其他任何支持的值，包括字符串，数字，布尔值，时间与日期，数组，内联表等。

```toml
key = # 非法：键没有值
```

完整的键值对需要在同一行上，且一行只有一个键值对，内联表例外。有些数据支持多行，其开头也必须在同一行中

```toml
key1 = "这是一行问本"
key2 = """
这是多行文本
多行文本支持跨行，但开头的标志 \"\"\" 必须与 key 同行"""
```

不能重复定义键

```toml
key = "value1"
key = "value2" # 非法，键 key 已定义
```

### 裸键

裸键即不使用引号包裹的键。裸键可以使用任何 ASCII 字母和数字、下划线、连接线，即 [A-Za-z0-9-_]

```toml
key1 = "value 1"
key 2 = "value 2"
key-3 = "value 3"
1234 = "1234"
```

键前后的空格会作为缩进删除（trim）

### 字符串键

通过标准的字符串写法作为键，此规则可以使用更加广泛的键名

如非必要推荐使用裸键

```toml
"key" = "value"
"ʎǝʞ" = "value"
'quoted "value"' = "value"
"127.0.0.1" = "value"
```

相同的字符串键与裸键视为同一个键

```toml
key = "value"
"key" = "value" # 非法，键 key 已定义
```

字符串键允许使用空字符串

```toml
 = "value" # 非法：裸键不允许使用空键名
"" = "value" # 合法
```

### 点分隔键

键名可以包含 "."。这实际上是一张表。

```toml
name = "name"
physical.color = "orange"
physical.shape = "round"
site."google.com" = "true"
3.1415926 = "PI" # 这是个迷惑行为
```

可以理解为

```json
{
    "name": "name",
    "physical": {
        "color": "orange",
        "shape": "round"
    },
    "site": {
        "google.com": true
    },
    // 这是个迷惑行为
    "3": {
        "1415926": "PI"
    }
}
```

若一个键已经被定义，且非表，那不能将其再定义为表

```toml
name = "name"
name.first = "First Name" # 非法：name 非表
```

## 字符串

标准字符串通过 "" 包裹，并支持使用转义符

| 转义符       | 含义                        | Unicode    |
| ------------ | --------------------------- | ---------- |
| `\b`         | 退格键                      | U+0008     |
| `\t`         | Tab                         | U+0009     |
| `\n`         | 换行                        | U+000A     |
| `\f`         | 换页（form feed）           | U+000C     |
| `\r`         | 回车                        | U+000D     |
| `\"`         | 光标恢复（carriage return） | U+0022     |
| `\\`         | 反斜杠                      | U+005C     |
| `\uXXXX`     | Unicode                     | U+XXXX     |
| `\UXXXXXXXX` | Unicode                     | U+XXXXXXXX |

### 多行文本

TOML 通过 """ 定义多行文本，多行文本第一行紧跟开头的换行会被忽略。

```toml
str1 = """
Rose are red
Violets are blue"""
# str1 == str2
str2 = """Rose are red
Violets are blue"""
```

若最后一个非空白字符为未转义的 \ 时，会移除他之后的空格和换行，直到出现有意义的行

```toml
str1 = "The quick brown fox jumps over the lazy dog."
# str1 == str2
str2 = """
The quick brown \            
    
 fox jumps over \
 the lazy dog."""
```

多行文本支持使用最多两个连续双引号而不需要转义

```toml
str1 = """这里有两个引号: ""。"""
str2 = """这里有三个引号：""\"。"""
str3 = """这里有九个引号：""\"""\"""\"。"""
```

### 源文本

使用 '' 或 ''' 包裹，单行或多行文本。其中的文本不支持转义。这意味着 Windows 下的路径不需要转义了。

同样，多行文本不支持连续使用三个 '

```toml
winPath = 'C:\Users\lq2007'
quote = 'Tom "Dubs" Preston-Werner'
regex = '<\i\c*\s>'
```

## 数字

数字包含整数和浮点两种类型

### 整数

整数可以表示任何 64 位有符号整型（-2^63到2^63-1）且允许以 _ 分割，不允许前导0

```toml
int1 = 1
int2 = +1
int3 = -1
int4 = 123_456_789
int5 = 0123 # 非法：不允许使用前导 0
```

整数 0 各种表示相同

```toml
zero1 = 0
zero2 = +0
zero3 = -0 # zero1 == zero2 == zero3
```

非负整数允许使用 2/8/16 进制表示，十六进制数大小写不敏感，此时允许前导 0 但不允许 + 符号，前缀与数字间不允许使用 _

```toml
# 16 进制
hex1 = 0xDEADBEEF
hex2 = 0xDeadBeef
hex3 = 0xdead_beef

# 8 进制
oct1 = 0o1234567
oct2 = 0o755

# 2 进制
bin1 = 0b11010110
```

### 浮点

浮点数符合 IEEE 754 定义的 64 位浮点值，允许使用 e 或 E 表示 10 的指数，允许使用下划线分割

```toml
flt1 = +1.0
flt2 = 3.14
flt3 = -1.0
# 指数
flt4 = 5e10
flt5 = 5e-10
flt6 = -2E-2
```

+0.0 与 -0.0 同样有效，遵循 IEEE 754

特殊浮点数使用小写表示

```toml
sf1 = inf # 等价于 +inf
sf2 = +inf
sf3 = -inf

sf4 = nan
sf5 = +nan
sf6 = -nan
```

## 布尔

布尔使用小写定义

```toml
bool1 = true
bool2 = false
```

## 日期与时间

日期与时间实现 RFC 3339 规定

```toml
# 基础
odt1 = 1979-05-27T07:32:00Z
odt2 = 1979-05-27T00:32:00-07:00
odt3 = 1979-05-27T00:32:00.999999-07:00
# 空格替换 T
odt4 = 1979-05-27 07:32:00Z
# 忽略石头 T
ldt1 = 1979-05-27T07:32:00
ldt2 = 1979-05-27T00:32:00.999999
# 时间
ld1 = 1979-05-27

lt1 = 07:32:00
lt2 = 00:32:00.999999
```

## 数组

数组以 [] 声明，，分割，内可以包含多种不同值。[] , 可以存在空格

```toml
values = [
  1, 2, 3,
  [4, 5, 6],
  "Foo Bar", # 末尾 , 可放入
]
```

## 表

表通过 [] 定义，允许表为空

```toml
[table]
```

表名与键名的要求相同，也不允许重复定义

```toml
[dog."tater.man"]
```

每个 TOML 在处于第一个自定义的表前的内容都处于一个根表，根表没有名字。

## 内联表

提供更为紧凑的方法表示表，必须在同一行内，以 {} 包裹

```toml
name = {first = "Tom", last = "Preston-Werner"}
point = {x = 1, y = 2}
animal = {type.name = "Pug"}
```

即

```toml
[name]
first = "Tom"
last = "Preston-Werner"
[point]
x = 1
y = 2
[animal]
type.name = "Pug"
```

内联表是只读的

```toml
name = {first = "Tom"}
name.last = "Preston-Werner" # 非法：不能向内联表中添加元素
```

## 表数组

使用 [[]] 表示表数组

```toml
[[products]]
name = "Hammer"
sku = 738594937

[[products]]  # 数组里的空表

[[products]]
name = "Nail"
sku = 284758393

color = "gray"
```

等价于

```json
{
  "products": [
    { "name": "Hammer", "sku": 738594937 },
    { },
    { "name": "Nail", "sku": 284758393, "color": "gray" }
  ]
}
```

每次对表数组的引用都指向当前表数组最后一个元素

```toml
[[fruits]]
name = "apple"

[fruits.physical]  # 子表
color = "red"
shape = "round"

[[fruits.varieties]]  # 嵌套表数组
name = "red delicious"

[[fruits.varieties]]
name = "granny smith"

[[fruits]]
name = "banana"

[[fruits.varieties]]
name = "plantain"
```

```json
{
  "fruits": [
    {
      "name": "apple",
      "physical": {
        "color": "red",
        "shape": "round"
      },
      "varieties": [
        { "name": "red delicious" },
        { "name": "granny smith" }
      ]
    },
    {
      "name": "banana",
      "varieties": [
        { "name": "plantain" }
      ]
    }
  ]
}
```

## ABNF

```abnf
;; This document describes TOML's syntax, using the ABNF format (defined in
;; RFC 5234 -- https://www.ietf.org/rfc/rfc5234.txt).
;;
;; All valid TOML documents will match this description, however certain
;; invalid documents would need to be rejected as per the semantics described
;; in the supporting text description.

;; It is possible to try this grammar interactively, using instaparse.
;;     http://instaparse.mojombo.com/
;;
;; To do so, in the lower right, click on Options and change `:input-format` to
;; ':abnf'. Then paste this entire ABNF document into the grammar entry box
;; (above the options). Then you can type or paste a sample TOML document into
;; the beige box on the left. Tada!

;; Overall Structure

toml = expression *( newline expression )

expression =  ws [ comment ]
expression =/ ws keyval ws [ comment ]
expression =/ ws table ws [ comment ]

;; Whitespace

ws = *wschar
wschar =  %x20  ; Space
wschar =/ %x09  ; Horizontal tab

;; Newline

newline =  %x0A     ; LF
newline =/ %x0D.0A  ; CRLF

;; Comment

comment-start-symbol = %x23 ; #
non-ascii = %x80-D7FF / %xE000-10FFFF
non-eol = %x09 / %x20-7F / non-ascii

comment = comment-start-symbol *non-eol

;; Key-Value pairs

keyval = key keyval-sep val

key = simple-key / dotted-key
simple-key = quoted-key / unquoted-key

unquoted-key = 1*( ALPHA / DIGIT / %x2D / %x5F ) ; A-Z / a-z / 0-9 / - / _
quoted-key = basic-string / literal-string
dotted-key = simple-key 1*( dot-sep simple-key )

dot-sep   = ws %x2E ws  ; . Period
keyval-sep = ws %x3D ws ; =

val = string / boolean / array / inline-table / date-time / float / integer

;; String

string = ml-basic-string / basic-string / ml-literal-string / literal-string

;; Basic String

basic-string = quotation-mark *basic-char quotation-mark

quotation-mark = %x22            ; "

basic-char = basic-unescaped / escaped
basic-unescaped = wschar / %x21 / %x23-5B / %x5D-7E / non-ascii
escaped = escape escape-seq-char

escape = %x5C                   ; \
escape-seq-char =  %x22         ; "    quotation mark  U+0022
escape-seq-char =/ %x5C         ; \    reverse solidus U+005C
escape-seq-char =/ %x62         ; b    backspace       U+0008
escape-seq-char =/ %x66         ; f    form feed       U+000C
escape-seq-char =/ %x6E         ; n    line feed       U+000A
escape-seq-char =/ %x72         ; r    carriage return U+000D
escape-seq-char =/ %x74         ; t    tab             U+0009
escape-seq-char =/ %x75 4HEXDIG ; uXXXX                U+XXXX
escape-seq-char =/ %x55 8HEXDIG ; UXXXXXXXX            U+XXXXXXXX

;; Multiline Basic String

ml-basic-string = ml-basic-string-delim [ newline ] ml-basic-body
                  ml-basic-string-delim
ml-basic-string-delim = 3quotation-mark
ml-basic-body = *mlb-content *( mlb-quotes 1*mlb-content ) [ mlb-quotes ]

mlb-content = mlb-char / newline / mlb-escaped-nl
mlb-char = mlb-unescaped / escaped
mlb-quotes = 1*2quotation-mark
mlb-unescaped = wschar / %x21 / %x23-5B / %x5D-7E / non-ascii
mlb-escaped-nl = escape ws newline *( wschar / newline )

;; Literal String

literal-string = apostrophe *literal-char apostrophe

apostrophe = %x27 ; ' apostrophe

literal-char = %x09 / %x20-26 / %x28-7E / non-ascii

;; Multiline Literal String

ml-literal-string = ml-literal-string-delim [ newline ] ml-literal-body
                    ml-literal-string-delim
ml-literal-string-delim = 3apostrophe
ml-literal-body = *mll-content *( mll-quotes 1*mll-content ) [ mll-quotes ]

mll-content = mll-char / newline
mll-char = %x09 / %x20-26 / %x28-7E / non-ascii
mll-quotes = 1*2apostrophe

;; Integer

integer = dec-int / hex-int / oct-int / bin-int

minus = %x2D                       ; -
plus = %x2B                        ; +
underscore = %x5F                  ; _
digit1-9 = %x31-39                 ; 1-9
digit0-7 = %x30-37                 ; 0-7
digit0-1 = %x30-31                 ; 0-1

hex-prefix = %x30.78               ; 0x
oct-prefix = %x30.6F               ; 0o
bin-prefix = %x30.62               ; 0b

dec-int = [ minus / plus ] unsigned-dec-int
unsigned-dec-int = DIGIT / digit1-9 1*( DIGIT / underscore DIGIT )

hex-int = hex-prefix HEXDIG *( HEXDIG / underscore HEXDIG )
oct-int = oct-prefix digit0-7 *( digit0-7 / underscore digit0-7 )
bin-int = bin-prefix digit0-1 *( digit0-1 / underscore digit0-1 )

;; Float

float = float-int-part ( exp / frac [ exp ] )
float =/ special-float

float-int-part = dec-int
frac = decimal-point zero-prefixable-int
decimal-point = %x2E               ; .
zero-prefixable-int = DIGIT *( DIGIT / underscore DIGIT )

exp = "e" float-exp-part
float-exp-part = [ minus / plus ] zero-prefixable-int

special-float = [ minus / plus ] ( inf / nan )
inf = %x69.6e.66  ; inf
nan = %x6e.61.6e  ; nan

;; Boolean

boolean = true / false

true    = %x74.72.75.65     ; true
false   = %x66.61.6C.73.65  ; false

;; Date and Time (as defined in RFC 3339)

date-time      = offset-date-time / local-date-time / local-date / local-time

date-fullyear  = 4DIGIT
date-month     = 2DIGIT  ; 01-12
date-mday      = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
time-delim     = "T" / %x20 ; T, t, or space
time-hour      = 2DIGIT  ; 00-23
time-minute    = 2DIGIT  ; 00-59
time-second    = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
time-secfrac   = "." 1*DIGIT
time-numoffset = ( "+" / "-" ) time-hour ":" time-minute
time-offset    = "Z" / time-numoffset

partial-time   = time-hour ":" time-minute ":" time-second [ time-secfrac ]
full-date      = date-fullyear "-" date-month "-" date-mday
full-time      = partial-time time-offset

;; Offset Date-Time

offset-date-time = full-date time-delim full-time

;; Local Date-Time

local-date-time = full-date time-delim partial-time

;; Local Date

local-date = full-date

;; Local Time

local-time = partial-time

;; Array

array = array-open [ array-values ] ws-comment-newline array-close

array-open =  %x5B ; [
array-close = %x5D ; ]

array-values =  ws-comment-newline val ws-comment-newline array-sep array-values
array-values =/ ws-comment-newline val ws-comment-newline [ array-sep ]

array-sep = %x2C  ; , Comma

ws-comment-newline = *( wschar / [ comment ] newline )

;; Table

table = std-table / array-table

;; Standard Table

std-table = std-table-open key std-table-close

std-table-open  = %x5B ws     ; [ Left square bracket
std-table-close = ws %x5D     ; ] Right square bracket

;; Inline Table

inline-table = inline-table-open [ inline-table-keyvals ] inline-table-close

inline-table-open  = %x7B ws     ; {
inline-table-close = ws %x7D     ; }
inline-table-sep   = ws %x2C ws  ; , Comma

inline-table-keyvals = keyval [ inline-table-sep inline-table-keyvals ]

;; Array Table

array-table = array-table-open key array-table-close

array-table-open  = %x5B.5B ws  ; [[ Double left square bracket
array-table-close = ws %x5D.5D  ; ]] Double right square bracket

;; Built-in ABNF terms, reproduced here for clarity

ALPHA = %x41-5A / %x61-7A ; A-Z / a-z
DIGIT = %x30-39 ; 0-9
HEXDIG = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
```

