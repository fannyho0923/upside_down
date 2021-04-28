package game.gameobj;

import game.utils.Global;
import game.utils.Vector;

import java.awt.*;

public class Tracker extends GameObject{

    private Vector velocity;

    public Tracker(int x, int y, Vector velocity){
        super(x, y, Global.UNIT_X, Global.UNIT_Y);
        this.velocity = velocity;
    }

    public Vector velocity() {
        return velocity;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {
        offset((int)velocity.x(), (int)velocity.y());
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

}
