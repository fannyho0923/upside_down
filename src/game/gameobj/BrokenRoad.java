package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;



public class BrokenRoad extends GameObject{
    private boolean isTouched;
    private Delay delay;
    private Type type;

    public enum Type{
        A("/img/gameObj/broken/broken1.png"),
        B("/img/gameObj/broken/broken2.png");

        private Image img;
        Type(String path){
            img = ImageController.getInstance().tryGet(path);
        }
    }

    public BrokenRoad(int left, int top, Type type) {
        super(left, top, Global.UNIT,Global.UNIT);
        this.type = type;

        delay = new Delay(20);
        isTouched = false;
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/break_platform.wav");
        actor.beBlock(this);
        delay.play();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        if(delay.count()){
            setExist(false);
        }
    }
}