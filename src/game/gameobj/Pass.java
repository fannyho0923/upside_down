package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Pass extends GameObject{
    Image img;
    boolean isPlayed;
    public Pass(int left, int top) {
        super(left, top, Global.UNIT, Global.UNIT);
        img = ImageController.getInstance().tryGet("/img/tile_0007.png");
        isPlayed=false;
    }

    @Override
    public void collisionEffect(Actor actor) {
        if(!isPlayed) {
            AudioResourceController.getInstance().shot("/sound/victory1.wav");
            isPlayed=true;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}