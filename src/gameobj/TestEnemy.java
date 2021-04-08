package gameobj;

import controller.ImageController;
import utils.Global;

import java.awt.*;

public class TestEnemy extends GameObject {
    public Image img;
    public int d; //d一次位移的距離

    public TestEnemy(final int x, final int y, final int d) {
        super(x, y, 48, 44);
        this.d = d;
        this.img = ImageController.getInstance().tryGet("/enemy1.png");
    }

    //輸入座標為圖形左上角，往右往下畫圖
    @Override
    public void paint(final Graphics g) {
        g.drawImage(this.img, this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height(), null);
        if (Global.IS_DEBUG) {
            //畫圖形測試外框
            g.setColor(Color.red); //繪圖區(紅色)
            g.drawRect(this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height());
            g.setColor(Color.blue); //碰撞區(藍色)
            g.drawRect(this.collider().left(), this.collider().top(), this.collider().width(), this.collider().height());
        }
    }

    @Override
    public void update() {
        offsetY(5);
        offsetX(this.d);
    }
}
