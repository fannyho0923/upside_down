package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class MovePlatform extends GameObject{
    Image img;
    int num;
    public MovePlatform(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.num = num;
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

    public Image getImage(){
            switch (num){
                case 1:
                    img = ImageController.getInstance().tryGet("/img/tile_0330.png");
                    break;
                case 2:
                    img = ImageController.getInstance().tryGet("/img/tile_0331.png");
                    break;
                case 3:
                    img = ImageController.getInstance().tryGet("/img/tile_0332.png");
                    break;
            }
        return img;
    }
}