package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class BrokenRoad extends GameObject{
    private Image img;
    private boolean isTouched;
    private Delay delay;
    public BrokenRoad(int left, int top, int width, int height) {
        super(left, top, width, height);
        img = ImageController.getInstance().tryGet("/img/tile_0071.png");
        isTouched = false;
        delay = new Delay(20);
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/break_platform.wav");
        actor.beBlock(this);
        delay.play();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        if(delay.count()){
            setExist(false);
        }
    }
}