package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour;

import java.awt.*;

public class Monster extends GameObject{
    Image img;
    Tour tour;
    int num;
    public Monster(int top, int left, int width, int height, int num) {
        super(top, left, width, height);

        this.num = num;
        switch(num){
            case 1:
                img = ImageController.getInstance().tryGet("/img/tile_0245.png");
                tour = new Tour(this, 0,5,20);
                break;
            case 2:
                img = ImageController.getInstance().tryGet("/img/tile_0383.png");
                tour = new Tour(this, 0,5,20);
                break;
            case 3:
                img = ImageController.getInstance().tryGet("/img/tile_0260.png");
                this.offsetY(80);
                tour = new Tour(this, 0,0,10);
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.reborn();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        tour.update();
    }
}