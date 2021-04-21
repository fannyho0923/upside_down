package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class SavePoint extends GameObject{
    private Image save;
    private Image toSave;

    public SavePoint(int top, int left) {
        super(top, left, Global.UNIT, Global.UNIT);
        save = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint_get.png");
        toSave = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint_toGet.png");
    }

    @Override
    public void savePointPaint(Graphics g, boolean get){
        if(get){
            g.drawImage(save, painter().left(),painter().top(),null);
        }else {
            g.drawImage(toSave,painter().left(),painter().top(),null);
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.setReborn(this.collider().left(),this.collider().bottom()-actor.collider().height(),false);

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(save, painter().left(),painter().top(),null);
    }

    @Override
    public void update() {

    }
}
