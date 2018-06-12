package top.littlefogcat.danmakulib.danmaku;

/**
 * Created by jjy on 2018/6/6.
 */

public class Danmaku {
    private String content; // 弹幕内容
    private int color; // 弹幕颜色
    private int type;// 0滚动，1顶部，2底部
    private int size;// 字体大小0中1小

    public Danmaku(String content, int color) {
        this(content, color, 0, 0);
    }

    public Danmaku(String content, int color, int type, int size) {
        this.content = content;
        this.color = color;
        this.type = type;
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
