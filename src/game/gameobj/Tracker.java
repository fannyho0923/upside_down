package game.gameobj;

import game.utils.Global;
import game.utils.Vector;

import java.awt.*;

public class Tracker extends GameObject{

    private Vector velocity;
    private int goalX;
    private int goalY;

    public Tracker(int x, int y, Vector velocity){
        super(x, y, Global.UNIT_X, Global.UNIT_Y);
        this.velocity = velocity;
        goalX = x; // for actor trig, and move slowly~
        goalY = y;
    }

    public Vector velocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
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
