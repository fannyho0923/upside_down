package game.gameobj;

import game.controller.ImageController;
import game.utils.Velocity;

import java.awt.*;

public class Conveyor extends GameObject{
    // 另外載入, 不要放在GameObj Arr, 鏡頭轉換時要重新創建

    private Image img;
    private int shift_x;
    public Conveyor(int top, int left, int width, int height, int shift_x) {
        super(top, left, width, height);
        this.shift_x = shift_x;
        img = ImageController.getInstance().tryGet("/img/lift_right_2x1.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.offsetX(shift_x);
    }

    @Override
    public boolean isExist() {
        return true;
    }

    @Override
    public void setExist(boolean isExist) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}