package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Spike extends GameObject{
    Image img;
    int num;
    public Spike(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.num = num;
        img = null;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.reborn();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), this.painter().width(), this.painter().height(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        switch (num){
            case 1:
                img = ImageController.getInstance().tryGet("/img/spikesLow.png");
                break;

            case 2:
                img = ImageController.getInstance().tryGet("/img/spikesDown.png");
                break;
        }
        return img;
    }
}