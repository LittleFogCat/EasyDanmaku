# EasyDanmaku

一个方便简单的Android弹幕控件，顾名思义so easy。

**注意！**本库尚未完善，仅供共同学习进步，勿要用在正式项目！

![Easy Danmaku.gif](https://github.com/LittleFogCat/EasyDanmaku/blob/master/readme2.gif)
![Easy Danmaku.gif](https://github.com/LittleFogCat/EasyDanmaku/blob/master/readme1.gif)

## 1. 用法

### 1.1 在布局中引入一个DanmakuView

```html

<top.littlefogcat.easydanmaku.ui.DanmakuView
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

### 1.2 设置数据

```kotlin
val danmakuView: DanmakuView = findView()
val danmakus: Collection<DanmakuItem> = getDanmakus()
danmakuView.setDanmakus(danmakus)
```

### 1.3 通过设置时间更新弹幕

DanmakuView使用子线程绘制，根据时间确定当前弹幕。在设置弹幕数据之后，只需设置时间即可刷新界面。

通过`setActionOnFrame`设置每一帧的动作，在其中更新时间，即可刷新界面。当然，自定义Timer固定时间刷新也是可以的。

```kotlin
// 使用setActionOnFrame刷新界面
danmakuView.setActionOnFrame {
    val progress = getVideoProgress() // 获取播放进度
    danmakuView.time = progress
}

// 或者使用Timer
timer(initialDelay = 0L, period = 16L) {
    val progress = getVideoProgress()
    danmakuView.time = progress
}
```

**就是这么easy**

## 2. changelog

**EasyDanmaku v0.2.0 - 2021.12.20**

- 使用kotlin全部重写；
- 使用SurfaceView重新实现；

**EasyDanmaku v0.1.3**

- 优化DanmakuView缓存复用的机制；

**EasyDanmaku v0.1.2**

- 添加了横竖屏切换的显示；
- 修复了可能导致显示异常的bug；

**EasyDanmaku v0.1.1**

- 修改了字体大小的设置方式；
- 其他的一些优化。

**EasyDanmaku v0.1.0**

- 重写了DanmakuView。
- 优化结构，去除冗余类。
- 修复弹幕过长导致显示内容不完整的问题。
- 加入顶部和底部弹幕。

**EasyDanmaku v0.0.0**

一个方便的Android弹幕控件~

顾名思义，实现起来easy，用起来也easy，（功能也很easy）代码很少，就几个文件，直接复制进项目里去吧！

