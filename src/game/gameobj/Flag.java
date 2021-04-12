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
        actor.setRebornState(actor.velocity().isReverse());
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
