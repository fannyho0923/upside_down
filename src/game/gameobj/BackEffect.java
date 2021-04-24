package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class BackEffect extends GameObject{

    private Image img;
    private int count;
    private static final int COUNT_MAX = 8;
    private static final int UNIT_X = 64;
    private static final int UNIT_Y = 256;
    private Delay delay;
    private Delay playDelay;
    private boolean play;

    public BackEffect(int x, int y){
        super(x,y, UNIT_X,UNIT_Y);
        this.count = 0;
        this.delay = new Delay(5);
        delay.loop();

        playDelay = new Delay (15);
        playDelay.loop();
        play = true;

        this.img = ImageController.getInstance().tryGet("/img/effect/backEffect2.png");
    }
    @Override
    public void paint(Graphics g) {
        if(play){
            g.drawImage(img, painter().left(),painter().top(),painter().right(),painter().bottom(),
                    count*UNIT_X,0,count*UNIT_X+UNIT_X,UNIT_Y,null);
        }
    }
    @Override
    public void update() {
        if(play){
            if(delay.count()){
                count = ++count%COUNT_MAX;
            }
            if(count == 0){
                play = false;
            }
        }else{
            if (playDelay.count()){
                this.setExist(false);
                play = true;
            }
        }
    }
}