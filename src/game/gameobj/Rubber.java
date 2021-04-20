package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;

import java.awt.*;

public class Rubber extends GameObject{

    private Type type;
    private boolean isTouch;
    private Delay delay;
    private int count;
    private Image imgEffect;

    public enum Type{
        h1("/img/gameObj/rubber/rubber_h1.png",Direction.horizontal),
        h2("/img/gameObj/rubber/rubber_h2.png",Direction.horizontal),
        h3("/img/gameObj/rubber/rubber_h3.png",Direction.horizontal),
        v1("/img/gameObj/rubber/rubber_v1.png",Direction.vertical),
        v2("/img/gameObj/rubber/rubber_v2.png",Direction.vertical),
        v3("/img/gameObj/rubber/rubber_v3.png",Direction.vertical);

        private Image img;
        private Direction dir;
        Type(String path,Direction dir){
            img = ImageController.getInstance().tryGet(path);
            this.dir = dir;
        }
    }
    private enum Direction{
        horizontal,
        vertical;
    }

    public Rubber(int left, int top, Type type) {
        super(left, top, Global.UNIT, Global.UNIT);
        this.type = type;
        isTouch = false;
        imgEffect = ImageController.getInstance().tryGet("/img/effect/rubberCollision.png");
        delay = new Delay(5);
    }

    @Override
    public void collisionEffect(Actor actor) {
        if(type.dir == Direction.horizontal){
            if(actor.velocity().y() < 0){ //up
                actor.setY(this.collider().bottom()+1);
            }else if (actor.velocity().y() > 0){ //down
                actor.setY(this.collider().top() -actor.collider().height() -1);
            }
            actor.velocity().stopY();
            actor.velocity().gravityReverse();
        }else if(type.dir == Direction.vertical){
            // 幫忙翻一次 / 要怎麼分辨是不是同一個?
            // actor.velocity().gravityReverse();
            actor.setCanReverse(true);
        }
        isTouch=true;
        delay.loop();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
        if (isTouch){
            if(count < 10) {
                if (delay.count()) {
                    count++;
                }
                System.out.println(count);
                g.drawImage(imgEffect, painter().left(), painter().top(), painter().right(), painter().bottom(),
                        count * Global.UNIT_X64,
                        count/5 * Global.UNIT_X64,
                        count * Global.UNIT_X64 + Global.UNIT_X64,
                        count/5 * Global.UNIT_X64 + Global.UNIT_Y64, null);
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