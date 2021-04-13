package game.gameobj;

import game.controller.ImageController;

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
        actor.block(this);
        actor.offsetX(shift_x);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}