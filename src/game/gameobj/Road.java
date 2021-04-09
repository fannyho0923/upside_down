package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Road extends GameObject{
    Image img;
    public Road(int x, int y, int width, int height) {
        super(x, y,width,height);
        img = ImageController.getInstance().tryGet("/img/road_2x2.png");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}