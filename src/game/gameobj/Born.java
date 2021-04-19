package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Born extends GameObject{
    Image img;
    public Born(int left, int top) {
        super(left, top, Global.UNIT, Global.UNIT);
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}