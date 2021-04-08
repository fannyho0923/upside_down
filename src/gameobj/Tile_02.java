package gameobj;

import controller.ImageController;

import java.awt.*;

public class Tile_02 extends GameObject {
    Image img;

    public Tile_02(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.img = ImageController.getInstance().tryGet("/Tile_02.png");
    }

    @Override
    public void paint(final Graphics g) {
        g.drawImage(this.img, this.painter().left(), this.painter().top(), null);
    }

    @Override
    public void update() {

    }
}
