package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Tile extends GameObject{
    public static enum Type{
        tile_0195("/img/gameObj/tile/tile_0195.png"),
        tile_0196("/img/gameObj/tile/tile_0196.png"),
        tile_0197("/img/gameObj/tile/tile_0197.png"),
        tile_0198("/img/gameObj/tile/tile_0198.png"),
        tile_0215("/img/gameObj/tile/tile_0215.png"),
        tile_0235("/img/gameObj/tile/tile_0235.png"),
        tile_0236("/img/gameObj/tile/tile_0236.png"),
        tile_0237("/img/gameObj/tile/tile_0237.png"),
        tile_0238("/img/gameObj/tile/tile_0238.png"),
        tile_0255("/img/gameObj/tile/tile_0255.png"),
        tile_0256("/img/gameObj/tile/tile_0256.png"),
        tile_0257("/img/gameObj/tile/tile_0257.png"),
        tile_999("/img/gameObj/tile/tile_999.png");


        private Image img;
        Type(String path){
            img = ImageController.getInstance().tryGet(path);
        }
    }


    private Type type;
    private int num;
    private static Image[] imgs = {
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t1.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t2.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t3.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t4.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t5.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/t7.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/test2.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/test5.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/test6.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/test11.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/test7.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/test8.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/test9.png"),
//            ImageController.getInstance().tryGet("/img/gameObj/tile/test10.png"),
    };

    public Tile(int top, int left, Type type) {
        super(top, left, Global.UNIT, Global.UNIT);
        this.type = type;
    }

    public Tile(int top, int left){
        super(top, left, Global.UNIT,Global.UNIT);
        num = Global.random(0,3);
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void paint(Graphics g) {
        if(type == null){
            g.drawImage(imgs[num],painter().left(), painter().top(), null);
        }else {
            g.drawImage(type.img, painter().left(), painter().top(), null);
        }

    }

    @Override
    public void update() {

    }
}
