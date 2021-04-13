package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Tile extends GameObject{
    Image img;
    Color color;
    public Tile(int top, int left, int width, int height, Color color) {
        super(top, left, width, height);
        this.color = color;
    }

    public enum Color{
        GREEN,
        RED,
        DARK_GREEN;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.block(this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        switch (color){
            case GREEN:
                img = ImageController.getInstance().tryGet("/img/tileGreen.png");
                break;

            case RED:
                img = ImageController.getInstance().tryGet("/img/tileRed.png");
                break;

            case DARK_GREEN:
                img = ImageController.getInstance().tryGet("/img/tileDarkGreen.png");
                break;
        }
        return img;
    }
}
