# AGENTS.md

## 项目结构
- Android/Kotlin 弹幕库（实验性质）。两个 Gradle 模块：`:easydanmaku`（库）与 `:sample`（演示 App）。
- `:easydanmaku` 源码中内嵌了自研的 SurfaceView UI 框架 `top.littlefogcat.esus`（ESUS），它不是独立模块，拥有自己的 `View`/`ViewGroup`/绘制流程；不要与 `android.view.*` 混用。简介见 `easydanmaku/src/main/java/top/littlefogcat/esus/README.md`。
- 示例入口：`MainActivity` → `SampleActivity`（视频+弹幕）/ `PressureTestActivity`（ESUS 压力测试）。示例弹幕是 B 站格式 XML（`sample/src/main/res/raw/comments.xml`），由 `DanmakuLoader` 解析。

## 构建与验证
- 必须使用 JDK 11：Gradle Wrapper 固定为 7.0.2，AGP 7.0.3，二者都不支持 Java 17+。本机 PATH 中的 `java` 是 21，但 `gradlew.bat` 读取 `JAVA_HOME`（当前指向 JDK 11），不要直接改用系统 Java 21。
- 常用命令（PowerShell）：
  - `.\gradlew.bat :sample:assembleDebug` → `sample/build/outputs/apk/debug/`
  - `.\gradlew.bat :sample:installDebug`
  - `.\gradlew.bat :easydanmaku:assembleRelease`
  - `.\gradlew.bat :easydanmaku:testDebugUnitTest --tests "top.littlefogcat.easydanmaku.ExampleUnitTest"`
  - `.\gradlew.bat :easydanmaku:lint`
- 首次构建需要联网下载 Gradle 7.0.2；`local.properties` 指向本机 SDK，已 gitignore，不要提交。
- 无 CI、无 ktlint/detekt、无发布配置。唯一的单元测试是 `2+2=4` 占位；instrumentation 测试的 runner 仍是旧的 `android.support.test...` 且包名断言过期，`connectedAndroidTest` 不能作为有效验证。
- 版本号在根 `build.gradle` 的 `allprojects.ext` 中。Kotlin 插件实际版本是 classpath 里的 `1.6.10`；`ext.kotlin_version = '1.3.72'` 已无人使用，升级时改 classpath。
- `sample/build.gradle` 会在 `sample/sign.config.gradle` 存在时自动启用签名，后者引用 `C:\Android\jjy.jks`；没有该 keystore 的机器上 sample 构建会失败。不要复制或提交其中的凭据。

## 渲染模型（最易踩坑）
- `DanmakuView` 是 SurfaceView，不经过 Android 原生 View 树，也不响应 `invalidate()`。ESUS 在后台 `HandlerThread` 上通过 `lockCanvas` 绘制自己的 View 树。
- 画面完全由 `DanmakuView.time`（Long，毫秒）驱动；每帧需在 `setActionOnFrame { danmakuView.time = videoPosition }` 中更新。`time` 不变就不会重绘。
- 播放结束或不再使用时调用 `finish()`：停止 Choreographer 回调、销毁 SUS 线程并清屏。
- `Danmakus.Options` / `Danmakus.Globals` 是进程级单例；`Globals.baseTextSize` 应由宿主按屏幕设置（默认 60f）。多个 `DanmakuView` 会共享 `DanmakuPools` 与全局 locator 状态（已知 TODO），不要假设多实例安全。
- `sendDanmaku()` 只修改 `time`，最终落到空实现的 `addDanmakuImmediately()`，实际不会显示；需要真正添加请用 `addDanmaku()`（仍可能因放不下被丢弃）。`setDisplayArea(Rect)` 是未实现的 TODO，调用会抛异常。

## 数据与复用
- `setDanmakus()` 接收 `DanmakuItem`。`DanmakuResolverImpl` 用只按 `time` 比较的 `TreeSet`：会自动排序，但相同时间戳的条目会被视为重复而丢弃（示例 loader 虽用永不为 0 的比较器保留重复项，进入 resolver 后仍会被丢弃）。
- `Danmaku` 由 `DanmakuPools` 复用；移除时 reset，`onDetached()` 会 `recycle()` 头像 Bitmap。不要共享头像 Bitmap，也不要在移除后继续持有同一个 `Danmaku` 实例。
- `DanmakuItem.time` 单位是毫秒；示例中 B 站 XML 的秒值会乘以 1000。
