package game.gameobj;

import game.utils.Global;
import java.awt.*;

public class Born extends GameObject{
    public Born(int left, int top) {
        super(left, top, Global.UNIT, Global.UNIT);
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void update() {

    }
}