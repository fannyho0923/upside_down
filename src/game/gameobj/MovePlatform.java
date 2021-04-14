package game.gameobj;

import game.controller.ImageController;
import game.utils.Tour;
import java.awt.*;


public class MovePlatform extends GameObject{
    private Image img;
    private Tour tour;
    public MovePlatform(int top, int left, int width, int height) {
        super(top, left, width, height);
        this.tour = new Tour(this, -10, 0, 20);
        img = ImageController.getInstance().tryGet("/img/elevator_1x1.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
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