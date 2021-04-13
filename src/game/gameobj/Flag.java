package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Flag extends GameObject{
    Image img;
    public Flag(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/save_flag_1x1.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.setRebornX(actor.painter().left());
        actor.setRebornY(actor.painter().top());
        actor.setRebornState(false);
        // 依據人物最後碰到的位置重設, 待修正固定值
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
