package top.littlefogcat.easydanmaku;

import android.app.Application;

import top.littlefogcat.danmakulib.utils.ScreenUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.init(this, 1920, 1080);
    }
}
