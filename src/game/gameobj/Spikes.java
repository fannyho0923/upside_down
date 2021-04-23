package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Spikes extends GameObject{

    private Type type;
    public enum Type{
        topSpikes("/img/gameObj/spike/spike_top.png"),
        downSpikes("/img/gameObj/spike/spike_down.png");
        private Image img;
        Type(String path){
            this.img = ImageController.getInstance().tryGet(path);
        }
    }
    public Spikes(int x, int y, int width, Type type){
        super(x,y,width, Global.UNIT);
        this.type = type;
    }

    @Override
    public void collisionEffect(Actor actor){
        if(actor.getState() == Actor.State.ALIVE){
            AudioResourceController.getInstance().play("/sound/spike.wav");
            actor.dead();
        }
    }

    @Override
    public void paint(Graphics g) {
        for (int i=0;i < painter().width()/Global.UNIT;i++){
            g.drawImage(type.img, i*Global.UNIT,painter().top(),null);
        }
    }

    @Override
    public void update() {

    }
}
