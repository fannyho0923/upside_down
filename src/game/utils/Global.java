package game.utils;

public class Global {
    //是否為網路模式
    public static boolean IS_INTERNET = false;
    //是否為Server
    public static boolean IS_SERVER = false;
    //測試模式
    public static final boolean IS_DEBUG = true;
    //視窗大小
    public static final int WINDOW_WIDTH = 960;//800
    public static final int WINDOW_HEIGHT = 640;//672
    //資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final long NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次更新要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final long LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;
    public static final int SPACE = 10;
    public static int UNIT_X = 32;
    public static int UNIT_Y = 32;

    public static final float GRAVITY = 0.6f;
    public static final int continueJump = 2;

    public static final int ENTER = 100;



    public static int random(final int min, final int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN,
        NO
    }


}