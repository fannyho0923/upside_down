package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Spike extends GameObject{
    Image img;
    int num;
    public Spike(int top, int left, int width, int height, int num) {
        super(top,left,width,height);
//        this.num = num;
//        img = null;

        switch(num){
            case 1:
                this.collider().offsetY(10);
                this.collider().offsetHeight(10);
                img = ImageController.getInstance().tryGet("/img/tile_0183.png");
                break;
            case 2:
                this.collider().offsetY(-10);
                this.collider().offsetHeight(-10);
                img = ImageController.getInstance().tryGet("/img/tile_0183_R.png");
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.reborn();
    }

    @Override
    public void paint(Graphics g) {
//        g.drawImage(getImage(), painter().left(), painter().top(), null);
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

//    public Image getImage(){
//        switch (num){
//            case 1:
//                img = ImageController.getInstance().tryGet("/img/tile_0183.png");
//
//                break;
//
//            case 2:
//                img = ImageController.getInstance().tryGet("/img/tile_0183_R.png");
//                break;
//        }
//        return img;
//    }
}