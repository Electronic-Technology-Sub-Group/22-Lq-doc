# Failed to parse source for import analysis because the content contains invalid JS syntax. Install @vitejsplugin-vue to handle .vue files.

```bash
12:57:54 [vite] Pre-transform error: Failed to parse source for import analysis because the content contains invalid JS syntax. Install @vitejs/plugin-vue to handle .vue files.
```

Vite 正常安装了 `plugin-vue`，但需要配置：

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
})
```
