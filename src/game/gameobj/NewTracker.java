package game.gameobj;

import game.utils.Global;

import java.awt.*;

public class NewTracker extends GameObject{

    private Velocity velocity;
    private int goalX;
    private int goalY;

    public NewTracker(int x, int y, Velocity velocity){
        super(x, y, Global.UNIT_X, Global.UNIT_Y);
        this.velocity = velocity;
        goalX = x; // for actor trig, and move slowly~
        goalY = y;
    }

    public Velocity velocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    @Override
    public void CollisionEffect(GameObject gameObject) {

    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {
        offset(velocity.x(), velocity.y());
    }
}
