package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;

public class BrokenRoad extends GameObject{
    private Delay delay;
    private Type type;

    private Delay glowDelay;
    private int glowCount;

    public enum Type{
        A("/img/gameObj/broken/crate_13.png", "/img/gameObj/bullet/a.png"),
        B("/img/gameObj/broken/crate_14.png", "/img/gameObj/bullet/b.png"),
        C("/img/gameObj/broken/crate_15.png", "/img/gameObj/bullet/e.png"),
        D("/img/gameObj/broken/crate_16.png", "/img/gameObj/bullet/c.png"),
        E("/img/gameObj/broken/crate_blue.png", "/img/gameObj/bullet/a.png");

        private Image img;
        private Image img2;

        Type(String path, String path2){
            img = ImageController.getInstance().tryGet(path);
            img2 = ImageController.getInstance().tryGet(path2);
        }
    }

    public BrokenRoad(int left, int top, Type type) {
        super(left, top, Global.UNIT,Global.UNIT);
        this.type = type;
        delay = new Delay(15);
        glowDelay = new Delay(10);
        glowCount = 0;
        glowDelay.loop();
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/break_platform.wav");
        actor.beBlock(this);
        delay.play();
    }

    @Override
    public void paint(Graphics g) {
//         很耗效能但效果不錯, 要做圖
//            g.drawImage(type.img,painter().left(),painter().top(),painter().right(),painter().bottom(),
//                    0,0,Global.UNIT,Global.UNIT-delay.getCount()*2,null);

            g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    public void paintAppear(Graphics g){
//        g.drawImage(type.img2,painter().left()-32, painter().top()-32,painter().left()+96-32,painter().top()+96-32,
//                            glowCount*96,glowCount*96,glowCount*96+96,glowCount*96+96,null);
        g.drawImage(type.img2,painter().left(), painter().top(),painter().left()+32,painter().top()+32,
                glowCount*96+32,glowCount*96+32,glowCount*96+64,glowCount*96+64,null);
    }

    @Override
    public void update() {
        if(delay.count()){
            setExist(false);
        }
        if(glowDelay.count()){
            glowCount = ++glowCount % 5;
        }
    }
}