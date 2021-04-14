package game.gameobj;

import java.awt.*;

import game.controller.ImageController;
import game.utils.Tour;

public class Ghost extends GameObject{

    private Image img;
    private Tour tour;

    public Ghost(int x, int y){
        super(x, y, 128,128);
        img = ImageController.getInstance().tryGet("/img/ghost.png");
        tour = new Tour(this,10,0,20);
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),null);
    }

    @Override
    public void update() {
        tour.update();
    }
}
