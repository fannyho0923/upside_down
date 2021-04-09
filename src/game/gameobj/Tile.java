package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Tile extends GameObject{
    Image img;
    public Tile(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/tileGreen.png");
    }

    @Override
    public void CollisionEffect(GameObject gameObject) {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
