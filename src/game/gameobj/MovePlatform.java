package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour2;

import java.awt.*;
import game.utils.Vector;


public class MovePlatform extends GameObject{

    Image img;
    int num;
    private Tour2 tour;
    private Vector vector;
    private Vector velocity;

    public MovePlatform(int left, int top, int width, int height, int num) {
        super(left, top, width, height);
        this.vector = new Vector(2,0);
        this.velocity = new Vector(0,0);
        this.num = num;
        this.tour = new Tour2(this, vector,200, 0);
        this.setSecondCollider(new Rect(left-1,top-1,width+2,width+2));
    }

    public void setVelocity(Vector vector){
        this.velocity = vector;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void secondCollisionEffect(Actor actor){
        //actor.offset((int)this.velocity.x(),(int)this.velocity.y());
        actor.shift(velocity);
        System.out.println(velocity.x());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        tour.update();
        this.offset((int)velocity.x(),(int)velocity.y());
    }

    public Image getImage(){
            switch (num){
                case 1:
                    img = ImageController.getInstance().tryGet("/img/tile_0330.png");
                    break;
                case 2:
                    img = ImageController.getInstance().tryGet("/img/tile_0331.png");
                    break;
                case 3:
                    img = ImageController.getInstance().tryGet("/img/tile_0332.png");
                    break;
            }
        return img;
    }
}