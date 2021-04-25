package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;

public class Conveyor extends GameObject{
    // 另外載入, 不要放在GameObj Arr, 鏡頭轉換時要重新創建

    private static final int SHIFT = 1;
    private Type type;

    public enum Type{

        TopL("/img/gameObj/conveyor/up.png","/img/gameObj/conveyor/up2.png",Global.Direction.LEFT),
        DownL("/img/gameObj/conveyor/down.png","/img/gameObj/conveyor/down2.png",Global.Direction.LEFT),
        TopR("/img/gameObj/conveyor/up.png","/img/gameObj/conveyor/up2.png",Global.Direction.RIGHT),
        DownR("/img/gameObj/conveyor/down.png","/img/gameObj/conveyor/down2.png",Global.Direction.RIGHT);
        private Image[] imgs;
        private Global.Direction dir;
        Type(String path,String path2,Global.Direction dir){
            this.imgs = new Image[2];
            this.imgs[0] = ImageController.getInstance().tryGet(path);
            this.imgs[1] = ImageController.getInstance().tryGet(path2);
            this.dir = dir;
        }
    }

    private Delay delay;
    private boolean flag;

    public Conveyor(int left, int top, Type type) {
        super(left, top, Global.UNIT, Global.UNIT);
        this.type = type;
        delay = new Delay(3);
        delay.loop();
        flag = true;
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/conveyor-crop.wav");
        actor.beBlock(this);
        if(type.dir == Global.Direction.LEFT){
            actor.offsetX(-SHIFT);
        }else if(type.dir == Global.Direction.RIGHT){
            actor.offsetX(SHIFT);
        }
    }

    @Override
    public void paint(Graphics g) {
        if(flag){
            g.drawImage(type.imgs[0], painter().left(), painter().top(), null);
        }else {
            g.drawImage(type.imgs[1], painter().left(), painter().top(), null);
        }

    }

    @Override
    public void update() {
        if (delay.count()){
            flag = !flag;
        }
    }
}