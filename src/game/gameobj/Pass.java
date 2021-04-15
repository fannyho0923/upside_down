package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;

import java.awt.*;

public class Pass extends GameObject{
    Image img;
    public Pass(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/tile_0007.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/break_platform.wav");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}