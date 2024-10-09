# systemctl

## target

```bash
systemctl get-default
systemctl set-default [target]
```

| level | target            |             |
| ----- | ----------------- | ----------- |
| 0     |                   | 关机          |
| 1     |                   | 单用户         |
| 2     |                   | 字符多用户，无网络   |
| 3     | mutli-user.target | 字符多用户       |
| 4     |                   | 字符多用户，unset |
| 5     | graphical.target  | 图形化多用户      |
| 6     |                   | 重启          |

