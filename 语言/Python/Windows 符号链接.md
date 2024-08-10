依赖于 `pywin32` 库，并需要管理员权限

```python
import win32file

def make_symbol_link(src: str, dst: str, is_directory: bool):
    """
    创建符号链接
    :param src: 源文件/目录
    :param dst: 目标文件/目录
    :param is_directory: 是否为目录
    :return: None
    """
    print(f"link {src} <= {dst}")
    win32file.CreateSymbolicLink(dst, src, 1 if is_directory else 0)
```