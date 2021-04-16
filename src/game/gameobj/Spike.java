package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class Spike extends GameObject{
    Image img;
    public Spike(int top, int left, int width, int height, int num) {
        super(top,left,width,height);

        switch(num){
            case 1:
                this.collider().offsetY(10);
                this.collider().offsetHeight(10);
                img = ImageController.getInstance().tryGet("/img/tile_0183.png");
                break;
            case 2:
                this.collider().offsetY(-10);
                this.collider().offsetHeight(-10);
                img = ImageController.getInstance().tryGet("/img/tile_0183_R.png");
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        if(actor.getState() == Actor.State.ALIVE){
            AudioResourceController.getInstance().shot("/sound/spike.wav");
            actor.dead();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}