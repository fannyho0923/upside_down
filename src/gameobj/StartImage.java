package gameobj;

import controller.ImageController;

import java.awt.*;

public class StartImage extends GameObject{
    Image img;
    public StartImage(int x, int y) {
        super(x, y,584,400);
        img = ImageController.getInstance().tryGet("/type.png");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
