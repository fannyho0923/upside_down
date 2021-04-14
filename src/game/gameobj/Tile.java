package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Tile extends GameObject{
    Image img;
    int style;
    public Tile(int top, int left, int width, int height, int style) {
        super(top, left, width, height);
        this.style = style;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        switch (style){
            case 1:
                img = ImageController.getInstance().tryGet("/img/tile_0049.png");
                break;

            case 2:
                img = ImageController.getInstance().tryGet("/img/tile_0051.png");
                break;

            case 3:
                img = ImageController.getInstance().tryGet("/img/tile_0090.png");
                break;
        }
        return img;
    }
}
