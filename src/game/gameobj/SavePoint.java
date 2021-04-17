package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class SavePoint extends GameObject{
    Image img;
    public SavePoint(int top, int left) {
        super(top, left, Global.UNIT, Global.UNIT);
        img = ImageController.getInstance().tryGet("/img/gameObj/savePoint/savePoint_toGet.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.setRebornX(actor.painter().left());
        actor.setRebornY(actor.painter().top());
        actor.setRebornState(false);
        // 依據人物最後碰到的位置重設, 待修正固定值
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
