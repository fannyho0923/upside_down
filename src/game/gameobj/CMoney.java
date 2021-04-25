package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;

public class CMoney extends GameObject{

    public enum Color{
        pink,
        PINK,
        yellow,
        YELLOW;
    }
    private Color color;

    private static Image[] pink = {
            ImageController.getInstance().tryGet("/img/cMoney/p_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/p_2.png"),
            ImageController.getInstance().tryGet("/img/cMoney/p_3.png"),
            ImageController.getInstance().tryGet("/img/cMoney/p_4.png"),
            ImageController.getInstance().tryGet("/img/cMoney/p_5.png")
    };
    private static Image[] PINK = {
            ImageController.getInstance().tryGet("/img/cMoney/Pink_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Pink_2.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Pink_3.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Pink_4.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Pink_5.png")
    };
    private static Image[] yellow = {
            ImageController.getInstance().tryGet("/img/cMoney/y_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/y_2.png"),
            ImageController.getInstance().tryGet("/img/cMoney/y_3.png"),
            ImageController.getInstance().tryGet("/img/cMoney/y_4.png"),
            ImageController.getInstance().tryGet("/img/cMoney/y_5.png")
    };
    private static Image[] YELLOW = {
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_2.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_3.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_4.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_5.png")
    };

    private static Image[] colors = {
            ImageController.getInstance().tryGet("/img/cMoney/pink_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/PINK_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/yellow_1.png"),
            ImageController.getInstance().tryGet("/img/cMoney/Yellow_1.png"),
    };

    private static int imgMax = 5;
    private Delay delay;
    private int imgCount;

    public CMoney(int x, int y,Color color){
        super(x,y,Global.UNIT,Global.UNIT);
        this.color = color;
        this.collider().offsetX(16);
        delay = new Delay(3);
        delay.loop();
        imgCount = 0;
    }

    @Override
    public void paint(Graphics g) {
        switch (color){
            case pink:
                g.drawImage(pink[imgCount], painter().left(),painter().top(),painter().right(),painter().bottom(),
                        16,16,48,48,null );
                break;
            case PINK:
                g.drawImage(PINK[imgCount], painter().left(),painter().top(),painter().right(),painter().bottom(),
                        16,16,48,48,null );
                break;
            case yellow:
                g.drawImage(yellow[imgCount], painter().left(),painter().top(),painter().right(),painter().bottom(),
                        16,16,48,48,null );
                break;
            case YELLOW:
                g.drawImage(YELLOW[imgCount], painter().left(),painter().top(),painter().right(),painter().bottom(),
                        16,16,48,48,null );
                break;
        }
    }

    @Override
    public void update() {
        if(delay.count()){
            imgCount = ++imgCount % imgMax;
        }
    }

    @Override
    public void collisionEffect(Actor actor){
        actor.beBlock(this);
    }
}
