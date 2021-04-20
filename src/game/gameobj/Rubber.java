package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Rubber extends GameObject{

    private Type type;

    public enum Type{
        h1("/img/gameObj/rubber/rubber_h1.png",Direction.horizontal),
        h2("/img/gameObj/rubber/rubber_h2.png",Direction.horizontal),
        h3("/img/gameObj/rubber/rubber_h3.png",Direction.horizontal),
        v1("/img/gameObj/rubber/rubber_v1.png",Direction.vertical),
        v2("/img/gameObj/rubber/rubber_v2.png",Direction.vertical),
        v3("/img/gameObj/rubber/rubber_v3.png",Direction.vertical);

        private Image img;
        private Direction dir;
        Type(String path,Direction dir){
            img = ImageController.getInstance().tryGet(path);
            this.dir = dir;
        }
    }

    private enum Direction{
        horizontal,
        vertical;
    }

    public Rubber(int left, int top, Type type) {
        super(left, top, Global.UNIT, Global.UNIT);
        this.type = type;
    }
    @Override
    public void collisionEffect(Actor actor) {
        if(type.dir == Direction.horizontal){
            if(actor.velocity().y() < 0){ //up
                actor.setY(this.collider().bottom()+1);
            }else if (actor.velocity().y() > 0){ //down
                actor.setY(this.collider().top() -actor.collider().height() -1);
            }
            actor.velocity().stopY();
            actor.velocity().gravityReverse();
        }else if(type.dir == Direction.vertical){
            // stopX??
            // 那離開不可以翻??
            actor.setCanReverse(true);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}