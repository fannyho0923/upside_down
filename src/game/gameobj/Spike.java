package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;

public class Spike extends GameObject{
    private Type type;
    private boolean isTouch;
    private ArrayList<Image> imageArrayList;

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
    private int count;
    public Spike(int top, int left, Type type) {
        super(top,left, Global.UNIT,Global.UNIT);
        this.type = type;
        delay = new Delay(10);
        count = 0;
        isTouch = false;
        imageArrayList = new ArrayList<>();
        imageArrayList.add(ImageController.getInstance().tryGet("/img/effect/spikeBlood_1.png"));
        imageArrayList.add(ImageController.getInstance().tryGet("/img/effect/spikeBlood_2.png"));
        imageArrayList.add(ImageController.getInstance().tryGet("/img/effect/spikeBlood_3.png"));
        imageArrayList.add(ImageController.getInstance().tryGet("/img/effect/spikeBlood_4.png"));

        switch (type){
            case left:
                collider().offsetWidth(-15);
                break;
            case top:
                collider().offsetHeight(-15);
                break;
            case right:
                collider().offsetX(15);
                collider().offsetWidth(-15);
                break;
            case down:
                collider().offsetY(15);
                collider().offsetHeight(-15);
                break;
        }


    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().shot("/sound/blood.wav");
        if(actor.getState() == Actor.State.ALIVE){
            actor.dead();
//            isTouch=true;
//            delay.loop();
        }
        if (actor.getState()==Actor.State.DEAD){
//            AudioResourceController.getInstance().shot("/sound/spike.wav");
//            AudioResourceController.getInstance().shot("/sound/blood.wav");
            isTouch=true;
            delay.loop();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
        g.setColor(Color.RED);
        g.drawRect(collider().left(),collider().top(),collider().width(),collider().height());
        if (isTouch){
            if(count < 4) {
                g.drawImage(imageArrayList.get(count), painter().left(), painter().top(), null);
                if (delay.count()) {
                    count++;
                }
            }else {
                delay.pause();
                count = 0;
                isTouch = false;
            }
        }
    }

    @Override
    public void update() {
    }
}