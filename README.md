# EasyDanmaku
一个方便简单的Android弹幕控件，顾名思义so easy

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

---

##  changelog

#### EasyDanmaku v1.0.1

修改了字体大小的设置方式；
其他的一些优化。

#### EasyDanmaku v1.0

船新版本，**不兼容老版本**

1、重写了DanmakuView。

2、优化结构，去除冗余类。

3、修复弹幕过长导致显示内容不完整的问题。

4、加入顶部和底部弹幕。


#### EasyDanmaku v0.1
一个方便的Android弹幕控件~

顾名思义，实现起来easy，用起来也easy，（功能也很easy）代码很少，就几个文件，直接复制进项目里去吧；

其实就是用TextView+动画实现的，本来以为性能会很差，不过没想到在配置很垃圾的AndroidTV上面也很流畅，更不用说手机了，占的资源和B站的弹幕库差不多；

