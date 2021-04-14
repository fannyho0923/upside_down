package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Monster extends GameObject{
    Image img;
    int num;
    public Monster(int top, int left, int width, int height, int num) {
        super(top, left, width, height);
        this.num = num;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.reborn();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        switch (num){
            case 1:
                img = ImageController.getInstance().tryGet("/img/tile_0245.png");
                break;
            case 2:
                img = ImageController.getInstance().tryGet("/img/tile_0383.png");
            break;
            case 3:
                img = ImageController.getInstance().tryGet("/img/tile_0260.png");
            break;
        }
        return img;
    }

}