package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class CMoney extends GameObject{

    public enum Word{
        I("/img/cMoney/3.png"),
        Love("/img/cMoney/L.png"),
        CMY("/img/cMoney/2.png"),
        ONE("/img/cMoney/Money.png");
        private Image img;
        Word(String path){
            this.img = ImageController.getInstance().tryGet(path);
        }

    }

    private Word word;
    public CMoney(int x, int y,Word word){
        super(x-16, y-16 , Global.UNIT, Global.UNIT);
        this.word = word;
        this.collider().offsetX(16);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(word.img, painter().left(),painter().top(),64,64,null);
    }

    @Override
    public void update() {

    }

    @Override
    public void collisionEffect(Actor actor){
        actor.beBlock(this);
    }
}
