package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Spikes extends GameObject{
    Image img;
    public Spikes(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        switch (num){
            case 1:
                img = ImageController.getInstance().tryGet("/img/spikesUp.png");
                break;
            case 2:
                img = ImageController.getInstance().tryGet("/img/spikesDown.png");
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), this.painter().width(), this.painter().height(), null);
    }

    @Override
    public void update() {

    }
}
