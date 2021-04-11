package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Wall extends MapObject{
    Image img;
    public Wall(int x, int y, int width, int height) {
        super(x, y,width,height);
        img = ImageController.getInstance().tryGet("/img/wall_1x1.png");
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
