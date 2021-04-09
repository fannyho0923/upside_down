package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class TestMap extends GameObject {
    private final Image img;

    public TestMap() {
        super(0, 0, 1600, 600); //地圖生成位置(參數代表地圖左上角座標)&地圖大小(寬、高)
        this.img = ImageController.getInstance().tryGet("/Background.png");
    }

    //輸入座標為圖形左上角，往右往下畫圖
    @Override
    public void paint(final Graphics g) {
        g.drawImage(this.img, this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height(), null);
    }

    @Override
    public void update() {

    }
}
