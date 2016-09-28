# 基于 giiwa 框架的 Message 模块
关于giiwa， 请参阅 http://giiwa.org

内部消息系统模块，提供的后台管理，并向其他模块提供Message API和widget。
<h1>功能介绍</h1>
<ul>
<li>收件箱widget页面</li>
<li>消息提醒widget页面</li>
<li>发件箱widget页面</li>
<li>收发消息API</li>
</ul>

<h1>开发使用</h1>
<ul>
<li>下载所有源码，然后直接导入Eclipse， 修改...</li>
<li>进入项目目录， 直接运行 ant编译打包, 会生成 message_1.0.1_????.zip </li>
<li>在你安装的giiwa 服务器中， 进入后台管理->系统管理->模块管理->上传模块，然后重启giiwa</li>
<li>重启后，进入后台管理->消息管理->消息管理／收件箱， 或直接在浏览器中输入/message.html从示例页面开始。</li>
</ul>

message模块为第三方模块开发提供内部消息API。
