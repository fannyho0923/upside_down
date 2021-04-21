package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Spike extends GameObject{
    private Type type;
    private boolean isTouch;
    private ArrayList<Image> imageArrayList;
    private boolean soundPlayed;

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
        this.soundPlayed = false;
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
        if(actor.getState() == Actor.State.ALIVE){
//            AudioResourceController.getInstance().shot("/sound/spike.wav");
            actor.dead();
            soundPlayed =false;
        }
        if (actor.getState()==Actor.State.DEAD){
            AudioResourceController.getInstance().play("/sound/blood_crop.wav");
            isTouch=true;
            delay.loop();
            soundPlayed = true;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
        if (isTouch){
            if(count < 4) {
                if (type==Type.down) {
                    g.drawImage(imageArrayList.get(count), painter().left(), painter().top(), null);
                }
                if (type==Type.top){
                    g.drawImage(paintReverse(imageArrayList.get(count)), painter().left(), painter().top(), null);
                }
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

    private Image paintReverse(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
    }
}