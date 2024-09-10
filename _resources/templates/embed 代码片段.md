<%*
function parse_path(path) {
    path = path.replaceAll('\\', '/')
    while (path.startsWith('/')) {
        path = path.substring(1)
    }
    while (path.endsWith('/')) {
        path = path.substring(0, path. length - 1)
    }
    return path
}
let filepath = await tp.system.prompt('代码文件：')
let dot = filepath.lastIndexOf(".")
let defaultLang = "plant"
if (dot >= 0) {
    defaultLang = filepath.slice(dot + 1)
}
let lang = await tp.system.prompt('语言：', defaultLang, true)
-%>
```embed-<% lang %>
PATH: "vault://_resources/codes/<% parse_path(filepath) %>"
LINES: "<% await tp.system.prompt('行号：') %>"
```