package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour;

import java.awt.*;

public class Monster extends GameObject{
    Image img;
    Tour tour;
    public Monster(int top, int left, int width, int height) {
        super(top, left, width, height);
        tour = new Tour(this, 0,5,20);
        img = ImageController.getInstance().tryGet("/img/monster.png");
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