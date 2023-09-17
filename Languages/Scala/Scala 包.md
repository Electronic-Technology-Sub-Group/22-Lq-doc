- Scala 文件头部声明一个或多个包名创建包
  
  ```scala
  package users
  class User
  ```

- 使用 package 代码块

  ```scala
  package users {
      package administrators {
          class AdminUser
      }
      package normalusers {
          class NormalUser
      }
  }
  ```

- 使用 import 导入其他包的成员 包括 类，Trait
	- 函数等，同包成员不需要导入
	- `scala`，`java.lang`，`object Predef` 包已被默认导入
	- Scala 可以在任意位置导入，若存在命名冲突且从项目根目录导入，使用 `_root_`

```scala
import users._ // 导入 users 包所有成员
import users.User // 导入 users 包的 User 类
import users.{User, UserPreferences} // 导入 users 包的部分成员
import users.{UserPreferences => UPrefs} // 导入 users 包的 UserPreferences 成员并为其设置别名
import _root_.users._
```

> [!hint]
> Scala 包命名惯例为：
>  - 包名与文件目录名相同，但不强求
>  - 包名应全部小写
>  - 前三层包一般为 `<top-level-domain>.<domain-name>.<project-name>`
