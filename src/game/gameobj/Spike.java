package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class Spike extends GameObject{
    Image img;
    Delay delay;
    public Spike(int top, int left, int width, int height, int num) {
        super(top,left,width,height);
        delay = new Delay(5);

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
        System.out.println("!");
        if(!delay.isPlaying()){
            AudioResourceController.getInstance().shot("/sound/spike.wav");
            delay.play();
        }
        if (delay.count()) {
            actor.reborn();
        }
//        AudioResourceController.getInstance().play("/sound/dead.wav");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}