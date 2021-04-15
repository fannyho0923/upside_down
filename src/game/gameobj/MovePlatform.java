package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour;

import java.awt.*;
import game.utils.Vector;


public class MovePlatform extends GameObject{

    Image img;
    int num;
    private Tour tour;
    private Vector v;

    private int count;
    private int step1;
    private int step2;
    public MovePlatform(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.v = new Vector(5,0);
        this.num = num;
        this.tour = new Tour(this,(int)v.x(),(int)v.y(),30);
    }

//    count %= (step * 4);
//        if(count < step | count >= step * 3){
//        obj.offset(dx, dy);
//        count++;
//    }else{
//        obj.offset(-dx,-dy);
//        count++;
//    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
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