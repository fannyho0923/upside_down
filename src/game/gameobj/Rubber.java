package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Rubber extends GameObject{
    Image img;
    public Rubber(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/reverse_h_4x1.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        //actor.beBlock(this);
        if(actor.velocity().y() < 0){ //up
            actor.setY(this.collider().bottom()+1);
        }else if (actor.velocity().y() > 0){
            actor.offsetY(this.collider().top() -actor.collider().height() -1);
        }
//        actor.velocity().setY(-actor.velocity().y());
//        actor.velocity().setDy(0);
        actor.velocity().stopY();
        actor.velocity().gravityReverse();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}