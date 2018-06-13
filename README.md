# EasyDanmaku
一个方便的Android弹幕控件~

顾名思义，实现起来easy，用起来也easy，（功能也很easy）代码很少，就几个文件，直接复制进项目里去吧；

其实就是用TextView+动画实现的，本来以为性能会很差，不过没想到在配置很垃圾的AndroidTV上面也很流畅，更不用说手机了，占的资源和B站的弹幕库差不多；

### 食用方法：
#### 1、在布局中引入
```
    <top.littlefogcat.danmakulib.danmaku.DanmakuContainer
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
#### 2、代码中使用
```
    mDanmakuContainer = findViewById(R.id.container);
    
    // 显示
    mDanmakuContainer.show(danmaku);
```
so easy
