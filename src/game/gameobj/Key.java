package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Key extends GameObject{
    Image img;
    public Key(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/key_1x1.png");
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