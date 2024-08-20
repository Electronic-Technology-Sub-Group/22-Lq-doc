将列表语法转换成其他形式视图的插件，通过在列表之前引用添加转换函数完成

```cardlink
url: https://linczero-github-io.pages.dev/MdNote_Public/ProductDoc/AnyBlock/README.show.html
title: "README"
description: "README General (通用) Chinese problem (中文语言问题和网站访问问题) 语言问题：文档是多语言的（zh/en），不用担心 访问失败/被墙：本文的默认网站链接指向 github.io，如果国内有不能访问的朋友，将网站链接部分的 linczero.github.io 替换成 linczero-github-io.pages...."
host: linczero-github-io.pages.dev
image: https://tse1-mm.cn.bing.net/th/id/R-C.4bbce1406f4442c1360edde26baa894d?rik=iHeUeby0jS5lnw&riu=http%3a%2f%2fp8.qhmsg.com%2fdr%2f270_500_%2ft01dbb76ff833d0a159.jpg&ehk=yggnC0t62%2b6DEVjvBgs%2fXJuuexBucd66FTc5gL0Gw%2fA%3d&risl=&pid=ImgRaw&r=0
```

[list2table|addClass(ab-table-fc)|addClass(ab-table-likelist)]

- 插件效果
  - 使用该插件的正常渲染效果
- 无插件效果
  - 用md软件在无该插件的环境下的渲染效果
  - 展示了近乎无入侵式的插件语法，在无插件环境下，你的md文档依旧可读优雅，而不是一坨代码！<br>
    这一点和callout的设计理念的相似的
- md源码
  - 用记事本打开源笔记后的效果
  - 方便你查看效果是怎么写出来的<br>
    也同样告诉你：该插件并不会导致源码变得丑陋和不可读
[list2table]

- Gymnosperm<br> 裸子植物
  - Cypress<br> 松树
    - Chinese pine<br> 油松
    - Buddhist pine<br> 罗汉松
    - masson pine<br> 马尾松
    - Pinus koraiensis<br> 红松
  - Ginkgo<br> 柏树| (**This** is ~~just~~ a `style` *test*)<br> (**这** ~~仅仅~~ 是一个 `样式` *测试*)
  - Angiosperms<br> 银杏
- Angiosperm<br> 被子植物
  - Sunflower<br> 向日葵
  - Lotus<br> 荷花
  - Chrysanthemum<br> 菊花 | Chamomile<br> 甘菊
[list2tableT]

- Cypress | 松树
- Ginkgo  | 柏树
- Angiosperms | 银杏
- Sunflower | 向日葵
- Lotus | 荷花
- Chrysanthemum | 菊花
[list2lt]

- < Company name<br>公司名| Superior section<br>上级部门| Principal<br>负责人| Phone<br>电话
- ==ABC head office==| | | 
  - **Shanghai branch**| ==ABC head office==  | ZhangSan| 13&xxxxxxxx
    - Marketing section| **Shanghai branch** | LiSi| 
    |self    |father  |mother  |
    |--------|--------|--------|
    |201xxxxx|202xxxxx|203xxxxx|
      - Marketing Division 1| | | 
      - Marketing Division 2| | | 
    - Sales section| **Shanghai branch** | WangWu| 15&xxxxxxxx
  - *Beijing branch*| ==ABC head office==  | ChenLiu| 16&xxxxxxxx
    - Technical division| *Beijing branch* | OuYang| 17&xxxxxxxx
    - Finance| *Beijing branch* | HuangPu| 18&xxxxxxxx
[list2dt]

- vue-demo/
  - build/， 项目构建(webpack)相关代码
  - config/， 配置目录，包括端口号等。我们初学可以使用默认的
  - node_modules/， npm 加载的项目依赖模块
  - src/， 这里是我们要开发的目录
    - assets/， 放置一些图片，如logo等
    - components， 目录里面放了一个组件文件，可以不用
    - App.vue， 项目入口文件，我们也可以直接将组件写这里，而不使用 components 目录
    - main.js， 项目的核心文件。
  - static/， 静态资源目录，如图片、字体等
  - test/， 初始测试目录，可删除
  - .eslintignore
  - .gitignore， git配置
  - .index.html， 首页入口文件，你可以添加一些 meta 信息或统计代码啥的
  - package.json， 项目配置文件
  - READED.md， 项目的说明文档，markdown 格式<br>手动换行测试<br>自动换行测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试k
  - ...

[list2astreeH|code()]

- vue-demo/
	- build/
	- config/
	- src/
		- assets/
			- a/
				- b
		- components
	- .babelrc
	- .editorconfig
	- ...

[list2pumlWBS]

- vue-demo/
  - build/
  - config/
  - node_modules/
  - src/
    - assets/
      - < a
        - b
        - < c
      - d
      - e
    - components
    - App.vue
    - main.js
  - static/
  - test/

[list2ut]

- < 水果
  - 颜色
  - 英文
- 苹果
  - 红色
  - apple
- 香蕉
  - 黄色
  - banana
- 橘子
  - 橙色
  - orange

[list2ut]

- 苹果
  - 颜色: 红色
  - 英文: apple
- 香蕉
  - 颜色: 黄色
  - 英文: banana
- 橘子
  - 颜色: 橙色
  - 英文: orange

[list2mdtimeline]

- 1840年6月

  - 英军发动鸦片战争

- 1842年8月

  - 清政府与英国签订《南京条约》:
    1）中国割让香港岛给英国;
    2）赔款洋银2100万元;
    3）开放广州、厦门、福州、宁波、上海五处为通商口岸;

    > [!note]+ 《南京条约》影响
    >
    > 1. 中国近代史上第一个不平等条约，给中国人民带来深重的灾难，开创了列强以条约形式侵略和奴役中国的恶例;
    > 2. 中国的国家主权和领土完整遭到破坏，逐步沦为半殖民地半封建社会;
    > 3. 中国社会的主要矛盾由地主阶级与农民阶级的矛盾，演变为帝国主义和中华民族的矛盾（主要矛盾)、封建主义和人民大众的矛盾;
    > 4. 反侵略反封建成为中国人民肩负的两大历史任务;
    > 5. 中国逐渐开始了反帝反封建的资产阶级民主革命。

- 1841年5月

  - 三元里人民的抗英斗争，是中国近代史上中国人民第一次大规模的反侵略武装斗争。

    > ![ ](https://tse1-mm.cn.bing.net/th/id/R-C.4bbce1406f4442c1360edde26baa894d?rik=iHeUeby0jS5lnw&riu=http%3a%2f%2fp8.qhmsg.com%2fdr%2f270_500_%2ft01dbb76ff833d0a159.jpg&ehk=yggnC0t62%2b6DEVjvBgs%2fXJuuexBucd66FTc5gL0Gw%2fA%3d&risl=&pid=ImgRaw&r=0)

- 1851年1月

  - 洪秀全金田村发动起义，建号太平天国。1853年3月，占领南京,定为首都,改名天京，正式宜告太平天国农民政权的建立。颁布《天朝天亩制度》、天平军北伐

[list2mdtab]

- linux
  - 可以通过执行以下命令在终端中使用 apt 包安装程序：
    ```shell
    apt-get install python3.6
    ```
- windows
  - 转到官方 Python 站点，并导航到最新版本。在撰写本文时，即 `3.10.6`。
  - 下载适用于您平台的二进制文件。执行二进制。
  - 除了将 Python 添加到 `PATH` 之外，您不需要选择任何选项，因为默认安装程序具有您需要的一切。只需单击“安装”即可。
- macOS
  - 转到官方 Python 站点，并导航到最新版本。在撰写本文时，即 `3.10.6`。
  - 下载适用于您平台的二进制文件。执行二进制。
  - 在 Mac 上，这将默认在 dmg 安装程序中完成。

