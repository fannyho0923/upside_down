package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;

import java.awt.*;

public class SavePoint extends GameObject{
    private Image save;
    private Image toSave;

    private static final int UNIT_X = 128;
    private static final int UNIT_Y = 128;
    private static final int COUNT_MAX = 6;
    private Delay delay;
    private int count;



    public SavePoint(int top, int left) {
        super(top, left, UNIT_X,UNIT_Y);
//        save = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint_get.png");
//        toSave = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint_toGet.png");
        save = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint1.png");
        toSave = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint2.png");

        painter().offset(-48,-64);
        collider().offsetY(-32);
        collider().offsetWidth(-96);
        collider().offsetHeight(-64);
//        collider().offset();
//

        delay = new Delay(5);
        delay.loop();
        count = 0;

    }

    @Override
    public void savePointPaint(Graphics g, boolean get){
        if(get){
            g.drawImage(save, painter().left(),painter().top(), painter().right(),painter().bottom(),
                    count*UNIT_X,0,count*UNIT_X+UNIT_X,UNIT_Y,null);
        }else {
            g.drawImage(toSave,painter().left(),painter().top(), painter().right(),painter().bottom(),
                    count*UNIT_X,0,count*UNIT_X+UNIT_X,UNIT_Y,null);
            g.setColor(Color.green);
            g.drawRect(collider().left(),collider().top(),collider().width(),collider().height());
        }

    }

    @Override
    public void collisionEffect(Actor actor) {
//        AudioResourceController.getInstance().shot("/sound/savePoint.wav");
        actor.setReborn(this.collider().left(),this.collider().bottom()-actor.collider().height(),false);
    }

    @Override
    public void paint(Graphics g) {
//        g.drawImage(save, painter().left(),painter().top(),null);
    }

    @Override
    public void update() {
        if(delay.count()){
            count = ++count%COUNT_MAX;
        }
    }
}
