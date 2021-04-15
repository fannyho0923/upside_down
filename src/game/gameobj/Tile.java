package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Tile extends GameObject{
    Image img;
    public Tile(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        switch (num){
            case 0:
                img = ImageController.getInstance().tryGet("/img/tile_0000.png");
                break;
            case 195:
                img = ImageController.getInstance().tryGet("/img/tile_0195.png");
                break;
            case 196:
                img = ImageController.getInstance().tryGet("/img/tile_0196.png");
                break;
            case 197:
                img = ImageController.getInstance().tryGet("/img/tile_0197.png");
                break;
            case 199:
                img = ImageController.getInstance().tryGet("/img/tile_0199.png");
                break;
            case 215:
                img = ImageController.getInstance().tryGet("/img/tile_0215.png");
                break;
            case 216:
                img = ImageController.getInstance().tryGet("/img/tile_0216.png");
                break;
            case 217:
                img = ImageController.getInstance().tryGet("/img/tile_0217.png");
                break;
            case 219:
                img = ImageController.getInstance().tryGet("/img/tile_0219.png");
                break;
            case 235:
                img = ImageController.getInstance().tryGet("/img/tile_0235.png");
                break;
            case 236:
                img = ImageController.getInstance().tryGet("/img/tile_0236.png");
                break;
            case 237:
                img = ImageController.getInstance().tryGet("/img/tile_0237.png");
                break;
            case 239:
                img = ImageController.getInstance().tryGet("/img/tile_0239.png");
                break;
            case 259:
                img = ImageController.getInstance().tryGet("/img/tile_0259.png");
                break;
            case 291:
                img = ImageController.getInstance().tryGet("/img/tile_0291.png");
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
