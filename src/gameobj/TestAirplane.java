package gameobj;

import controller.ImageController;
import utils.Global;

import java.awt.*;

public class TestAirplane extends GameObject {
    private static final int MOVE_SPEED = 4;
    private final Image img;
    private Global.Direction dir;

    public TestAirplane(final int x, final int y) {
        super(x, y, 80, 50, x, y, 77, 45);
        this.dir = Global.Direction.NO;
        this.img = ImageController.getInstance().tryGet("/airplane1.png");
    }

    public static int getMoveSpeed() {
        return MOVE_SPEED;
    }

    @Override
    public void update() {
        switch (this.dir) {
            case LEFT :
                if (!touchLeft()) {
                    offsetX(-MOVE_SPEED);
                }
                break;
            case UP :
                if (!touchTop()) {
                    offsetY(-MOVE_SPEED);
                }
                break;
            case RIGHT :
                if (!touchRight()) {
                    offsetX(MOVE_SPEED);
                }
                break;
            case DOWN :
                if (!touchBottom()) {
                    offsetY(MOVE_SPEED);
                }
                break;
        }
    }

    //往左是0 上是1 右是2 下是3
    public void changeDir(final int direction) {
        this.dir = Global.Direction.values()[direction];
    }

    //輸入座標為圖形左上角，往右往下畫圖
    @Override
    public void paint(final Graphics g) {
        g.drawImage(this.img, this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height(), null);
        if (Global.IS_DEBUG) {
            //畫圖形測試外框h
            g.setColor(Color.red); //繪圖區(紅色)
            g.drawRect(this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height());
            g.setColor(Color.blue); //碰撞區(藍色)
            g.drawRect(this.collider().left(), this.collider().top(), this.collider().width(), this.collider().height());
        }
    }
}
