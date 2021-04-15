package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour;
import game.utils.Vector;
import java.awt.*;


public class MovePlatform extends GameObject{

    Image img;
    int num;
    private Tour tour;
    private Vector velocity;
    public MovePlatform(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.velocity = new Vector(-2,0);
        this.tour = new Tour(this, (int)velocity.x(), (int)velocity.y(), 20);
        this.num = num;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
        actor.offset((int)velocity.x(),(int)velocity.y());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        tour.update();
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