package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Tree_01 extends GameObject {
    Image img;

    public Tree_01(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.img = ImageController.getInstance().tryGet("/Tree_1.png");
    }

    @Override
    public void paint(final Graphics g) {
        g.drawImage(this.img, this.painter().left(), this.painter().top(), null);
    }

    @Override
    public void update() {

    }
}
