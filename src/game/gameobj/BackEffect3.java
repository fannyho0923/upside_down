package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class BackEffect3 extends GameObject{

    private Image img;
    private int count;
    private static final int COUNT_MAX = 5;
    private static final int UNIT_X = 128;
    private static final int UNIT_Y = 128;
    private Delay delay;

    public BackEffect3(int x, int y){
        super(x,y, UNIT_X,UNIT_Y);
        this.count = 0;
        this.delay = new Delay(3);
        delay.loop();
        this.img = ImageController.getInstance().tryGet("/img/effect/backEffect3.png");
    }



    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(),painter().top(),painter().right(),painter().bottom(),
                0,count*UNIT_Y,UNIT_X,count*UNIT_Y+UNIT_Y,null);
    }

    @Override
    public void update() {
        if(delay.count()){
            count = ++count%COUNT_MAX;
        }
        if(count == 0){
            this.setExist(false);
        }
    }
}