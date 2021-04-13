package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Pass extends GameObject{
    Image img;
    public Pass(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/pass.png");
    }

    @Override
    public void collisionEffect(Actor actor) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}