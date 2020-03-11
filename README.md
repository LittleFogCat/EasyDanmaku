# EasyDanmaku
一个方便简单的Android弹幕控件，顾名思义so easy。

![Easy Danmaku.gif](https://github.com/LittleFogCat/EasyDanmaku/blob/master/readme2.gif)
![Easy Danmaku.gif](https://github.com/LittleFogCat/EasyDanmaku/blob/master/readme1.gif)

- 原理简单，其实就是用TextView+动画实现的，纯java实现，方便修改和扩展；
- 体积小，占用资源少，运行流畅，实测在同屏100个弹幕的情况下占的资源和[B站的弹幕库](https://github.com/bilibili/DanmakuFlameMaster)相差无几；
- 使用方便，仅用几行代码即可完成弹幕发送；

## 1. 用法两部曲
### 1.1 在布局中引入一个FrameLayout作为弹幕容器
```xml
    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### 1.2 代码中使用
```java
    // 获得DanmakuManager单例
    DanmakuManager dm = DanmakuManager.getInstance();
    
    // 设置一个FrameLayout为弹幕容器
    FrameLayout container = findViewById(R.id.container);
    dm.setDanmakuContainer(container);
   
    // 发送弹幕
    Danmaku danmaku = new Danmaku();
    danmaku.text = "666"; 
    dm.send(danmaku);
```

**就是这么easy**

---

## changelog

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

