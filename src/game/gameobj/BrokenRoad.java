package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;



public class BrokenRoad extends GameObject{
    private Delay delay;
    private Type type;

    public enum Type{
        A("/img/gameObj/broken/broken_t7.png"),
        B("/img/gameObj/broken/broken_t7.png");

        private Image img;
        Type(String path){
            img = ImageController.getInstance().tryGet(path);
        }
    }

    public BrokenRoad(int left, int top, Type type) {
        super(left, top, Global.UNIT,Global.UNIT);
        this.type = type;
        delay = new Delay(15);
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/break_platform.wav");
        actor.beBlock(this);
        delay.play();
    }

    @Override
    public void paint(Graphics g) {
        // 很耗效能但效果不錯, 要做圖
//            g.drawImage(type.img,painter().left(),painter().top(),painter().right(),painter().bottom(),
//                    0,0,Global.UNIT,Global.UNIT-delay.getCount()*2,null);
            g.drawImage(type.img, painter().left(), painter().top(), null);

    }

    @Override
    public void update() {
        if(delay.count()){
            setExist(false);
        }
    }
}