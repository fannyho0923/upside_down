package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Tile extends GameObject{
    public enum Type{
        BROWN, GRAY, COLOR;
    }
    private Type type;
    private int num;
    private static Image[] brownImg = {
            ImageController.getInstance().tryGet("/img/gameObj/tile/br1.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br2.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br3.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br4.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br5.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br6.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br7.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br9.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br88.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/br0.png"),};
    private static Image[] grayImg = {
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_1.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_2.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_3.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_4.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_5.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_6.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_7.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_8.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/gray_9.png"),
    };
    private static Image[] colorImg = {
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_1.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_4.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_5.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_6.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_7.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_9.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_10.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_11.png"),
            ImageController.getInstance().tryGet("/img/gameObj/tile/crate_12.png"),
    };

    public Tile(int top, int left, Type type) {
        super(top, left, Global.UNIT, Global.UNIT);
        this.type = type;
        switch (type){
            case BROWN:
                num = Global.random(0,brownImg.length-1);
                break;
            case GRAY:
                num = Global.random(0,grayImg.length-1);
                break;
            case COLOR:
                num = Global.random(0,colorImg.length-1);
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void paint(Graphics g) {
        switch (type){
            case BROWN:
                g.drawImage(brownImg[num], painter().left(), painter().top(), null );
                break;
            case GRAY:
                g.drawImage(grayImg[num], painter().left(), painter().top(), null );
                break;
            case COLOR:
                g.drawImage(colorImg[num], painter().left(), painter().top(), null );
                break;
        }
    }

    @Override
    public void update() {

    }
}
