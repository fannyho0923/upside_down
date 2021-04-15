package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Born extends GameObject{
    Image img;
    public Born(int left, int top, int width, int height) {
        super(left, top, width, height);
        img = ImageController.getInstance().tryGet("/img/tile_0114.png");
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