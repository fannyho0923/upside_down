package game.camera;

import game.gameobj.GameObject;

import java.awt.image.BufferedImage;

public class MapInformation {
    // 記得要在地圖那邊做地圖資訊的設定唷～
    // 讀取地圖取得實際地圖大小與邊界
    // 目前以單例方式實作

    private static MapInformation mapInformation;
    private static int left;
    private static int top;
    private static int width;
    private static int height;

    private MapInformation() {
    }

    public static MapInformation getInstance() {
        if (mapInformation == null) {
            mapInformation = new MapInformation();
        }
        return mapInformation;
    }

    public void setMapInfo(final int left, final int top, final int width, final int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    //讀取地圖產生器生成的bmp圖檔，轉成鏡頭移動邊界大小，
    public void setMapInfo(final BufferedImage img) {
        left = 0;
        top = 0;
        width = img.getWidth();
        height = img.getHeight();
    }

    public void setMapInfo(final GameObject map) {
        left = map.painter().left();
        top = map.painter().top();
        width = map.painter().width();
        height = map.painter().height();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int left() {
        return left;
    }

    public int top() {
        return top;
    }

    public int right() {
        return left() + width;
    }

    public int bottom() {
        return top + height;
    }
}
