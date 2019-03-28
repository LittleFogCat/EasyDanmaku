# EasyDanmaku
一个方便简单的Android弹幕控件，顾名思义so easy

### 用法两部曲
#### 1、在布局中引入一个FrameLayout作为弹幕容器
```xml
    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

#### 2、代码中使用
```java
    // 1、设置一个FrameLayout为弹幕容器
    FrameLayout container = findViewById(R.id.container);
    DanmakuManager.getInstance().setDanmakuContainer(container);
   
    // 2、发送弹幕
    Danmaku dam = new Danmaku();
    dam.text = "666"; 
    DanmakuManager.getInstance().send(dam);
```

#### 就是这么easy

---

#### 如果想要更easy，你可以直接在布局中引入DanmakuLayout
```xml
    <top.littlefogcat.danmakulib.danmaku.DanmakuLayout
        android:id="@+id/danmakuLayout"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:background="#eee" />
```
#### 然后
```java
    mDanmakuLayout = findViewById(R.id.danmakuLayout);
    mDanmakuLayout.send("test");
```

#### 不过我不推荐这么做，因为这个Layout有很多问题，比如旋转屏幕之后就显示异常之类的，有兴趣的话可以自己改着玩。


---
---


## EasyDanmaku v1.0
船新版本，**不兼容老版本**

1、重写了DanmakuView。

2、优化结构，去除冗余类。

3、修复弹幕过长导致显示内容不完整的问题。

4、加入顶部和底部弹幕。



---


## EasyDanmaku v0.1
一个方便的Android弹幕控件~

顾名思义，实现起来easy，用起来也easy，（功能也很easy）代码很少，就几个文件，直接复制进项目里去吧；

其实就是用TextView+动画实现的，本来以为性能会很差，不过没想到在配置很垃圾的AndroidTV上面也很流畅，更不用说手机了，占的资源和B站的弹幕库差不多；

### 使用方法：
#### 1、在布局中引入一个FrameLayout作为弹幕容器
```xml
    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
#### 2、代码中使用
```
    // 设置容器
    FrameLayout container = findViewById(R.id.container);
    DanmakuManager.getInstance().setRootView(container);
    
    // 显示弹幕
    DanmakuManager.getInstance().show(new Danmaku("666", Color.RED));
```

**就是这么easy**
