package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class Spike extends GameObject{
    Image img;
    int num;
    Delay delay;
    public Spike(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.num = num;
        img = null;
        delay = new Delay(55);
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/spike.wav");
        delay.play();
        if (delay.count()) {
            AudioResourceController.getInstance().play("/sound/dead.wav");
        }
        delay.play();
        if (delay.count()) {
            actor.reborn();
        }
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
                img = ImageController.getInstance().tryGet("/img/tile_0183.png");
                break;

            case 2:
                img = ImageController.getInstance().tryGet("/img/tile_0183_R.png");
                break;
        }
        return img;
    }
}