- `\0` 使用两个字节表示，保证字符串中不会出现空字符
- 在 U+FFFF 之上的字符（如 emoji 表情），使用代理对（两个字符）表示
	- ASCII 字符（0x0001-0x007F），使用一个字节保存一个字符
	- `0x0080-0x07FF`，使用两个字节表示，`0xXXXXXXXX` -> `110XXXXX 10XXXXXX`
	- `0x0000 0800-0x0000 FFFF`：三字节，`0xXXXXXXXX` -> `1110XXXX 10XXXXXX 10XXXXXX`
	- `0x0001 0000-0x0010 FFFF`：四字节，`0xXXXXXXXX` -> `11110XXX 10XXXXXX 10XXXXXX 10XXXXXX`

例：机 `0b 0110 011100 111010`
![[../_resources/images/常量池 2024-08-01 14.04.58.excalidraw]]
