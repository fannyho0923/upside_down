package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;

import java.awt.*;

public class SavePoint extends GameObject{
    private Image save;
    private Image toSave;
    private boolean isPlayed;

    private static final int UNIT_X = 128;
    private static final int UNIT_Y = 128;
    private static final int COUNT_MAX = 6;
    private Delay delay;
    private int count;

    public SavePoint(int top, int left) {
        super(top, left, 32,64);
        save = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint1.png");
        toSave = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint2.png");
        offsetY(-32);
        delay = new Delay(5);
        delay.loop();
        count = 0;
    }

    @Override
    public void savePointPaint(Graphics g, boolean get){
        if(get){
            g.drawImage(save, painter().left(),painter().top(), painter().right(),painter().bottom(),
                    count*UNIT_X+48,32,count*UNIT_X+UNIT_X-48,UNIT_Y-32,null);
        }else {
            g.drawImage(toSave,painter().left(),painter().top(), painter().right(),painter().bottom(),
                    count*UNIT_X+48,32,count*UNIT_X+UNIT_X-48,UNIT_Y-32,null);
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        if (!isPlayed){
            AudioResourceController.getInstance().shot("/sound/savePoint.wav");
            isPlayed=true;
        }
        actor.setReborn(this.collider().left(),this.collider().bottom()-actor.collider().height(),false);
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void update() {
        if(delay.count()){
            count = ++count%COUNT_MAX;
        }
    }
}
