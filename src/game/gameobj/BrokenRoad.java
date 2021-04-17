package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;



public class BrokenRoad extends GameObject{
    private Image img;
    private boolean isTouched;
    private Delay delay;
    private Type type;

    public static enum Type{
        A,
        B;
    }

    public BrokenRoad(int left, int top, Type type) {
        super(left, top, Global.UNIT,Global.UNIT);
        img = ImageController.getInstance().tryGet("/img/tile_0071.png");

        delay = new Delay(20);

        isTouched = false;
        switch (type){
            case A:
                img = ImageController.getInstance().tryGet("/img/gameObj/broken/broken1.png");
                break;
            case B:
                img = ImageController.getInstance().tryGet("/img/gameObj/broken/broken2.png");
        }
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