package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class WalkAnimation extends GameObject{
    private Image img;
    private static final int UNIT_X = 32;
    private static final int UNIT_Y = 32;
    private static final int countMax = 4;

    private Delay delay;
    private int count;

    public WalkAnimation(int x, int y){
        super(x,y,UNIT_X,UNIT_Y);
        img = ImageController.getInstance().tryGet("/img/effect/walk.png");
        delay = new Delay(3);
        delay.loop();
        count = 0;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),painter().right(),painter().bottom(),
                0,count*UNIT_Y,UNIT_X,(count*UNIT_Y)+UNIT_Y,null);
    }

    @Override
    public void update() {
        if(delay.count()){
//            count = ++count%countMax;
            count++;
        }
        if(count == countMax) {
            setExist(false);
        }
    }
}
