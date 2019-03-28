package top.littlefogcat.danmakulib.danmaku;

/**
 * Created by LittleFogCat.
 */

public class Danmaku {
    public static final String COLOR_WHITE = "#ffffffff";
    public static final String COLOR_RED = "#ffff0000";
    public static final String COLOR_GREEN = "#ff00ff00";
    public static final String COLOR_BLUE = "#ff0000ff";
    public static final String COLOR_YELLOW = "#ffffff00";
    public static final String COLOR_PURPLE = "#ffff00ff";

    public String text;// 文字
    public TextSize textSize = TextSize.normal;// 字号：标准、小号、大号
    public Mode mode = Mode.scroll;// 模式：滚动、顶部、底部
    public String color = COLOR_WHITE;// 默认白色

    public enum TextSize {
        normal, small, large
    }

    public enum Mode {
        scroll, top, bottom
    }

    public Danmaku() {
    }

    public Danmaku(String text, TextSize textSize, Mode mode, String color) {
        this.text = text;
        this.textSize = textSize;
        this.mode = mode;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "text='" + text + '\'' +
                ", textSize=" + textSize +
                ", mode=" + mode +
                ", color='" + color + '\'' +
                '}';
    }
}
