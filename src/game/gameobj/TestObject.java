package game.gameobj;

import game.utils.Global;

import java.awt.*;

//此物件用來測試鏡頭自動捲動
public class TestObject extends GameObject {
    private final int moveSpeed;

    public TestObject(final int x, final int y) {
        super(x, y, 1, 1);
        this.moveSpeed = 1;
    }

    //輸入座標為圖形左上角，往右往下畫圖
    @Override
    public void paint(final Graphics g) {
        if (Global.IS_DEBUG) {
            //畫圖形測試外框
            g.setColor(Color.red); //碰撞區(藍色)
            g.drawRect(this.collider().left(), this.collider().top(), this.collider().width(), this.collider().height());
        }
    }

    @Override
    public void update() {
        offsetX(this.moveSpeed);
        offsetY(this.moveSpeed);
    }
}
