package game.gameobj;

import game.utils.Global;
import game.utils.Velocity;

import java.awt.*;

public class Tracker extends GameObject{

    private Velocity velocity;

    public Tracker(int x, int y, Velocity velocity){
        super(x, y, Global.UNIT_X, Global.UNIT_Y);
        this.velocity = velocity;
    }

    public Velocity velocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {
        offset(velocity.x(), velocity.y());
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

}
