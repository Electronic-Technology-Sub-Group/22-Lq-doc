<%*
function parse_path(path) {
    path = path.replaceAll('\\', '/')
    while (path.startsWith('/')) {
        path = path.substring(1)
    }
    while (path.endsWith('/')) {
        path = path.substring(0, path.length - 1)
    }
    return path
}
let filepath = await tp.system.prompt('代码文件：')
let start = await tp.system.prompt('start：', '')
let end = await tp.system.prompt('end：', '')
let lang = await tp.system.prompt('语言：', '')
start = parseInt(start)
end = parseInt(end)
-%>
```reference
file: "@/_resources/codes/<% parse_path(filepath) %>"
<%* if (lang != '') { -%>
lang: "<% lang %>"
<%* } -%>
<%* if (!isNaN(start)) { -%>
start: <% start %>
<%* } -%>
<%* if (!isNaN(end)) { -%>
end: <% end %>
<%* } -%>
```