package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;

public class Spike extends GameObject{


    private Type type;

    public enum Type{
        left("/img/gameObj/spike/spike_left.png"),
        top("/img/gameObj/spike/spike_top.png"),
        right("/img/gameObj/spike/spike_right.png"),
        down("/img/gameObj/spike/spike_down.png");

        private Image img;

        Type(String path){
            this.img = ImageController.getInstance().tryGet(path);
        }
    }

    private Delay delay;
    public Spike(int top, int left, Type type) {
        super(top,left, Global.UNIT,Global.UNIT);
        this.type = type;
        delay = new Delay(5);
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
        g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}