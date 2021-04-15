package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Rubber extends GameObject{
    Image img;
    int dir;
    int num;
    public Rubber(int top, int left, int width, int height, int dir, int num) {
        super(top, left, width, height);
        this.dir = dir;
        this.num = num;
    }

    @Override
    public void collisionEffect(Actor actor) {
        if(actor.velocity().y() < 0){ //up
            actor.setY(this.collider().bottom()+1);
        }else if (actor.velocity().y() > 0){
            actor.offsetY(this.collider().top() -actor.collider().height() -1);
        }

        actor.velocity().stopY();
        actor.velocity().gravityReverse();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        if (dir == 1) {
                switch (num){
                    case 1:
                        img = ImageController.getInstance().tryGet("/img/tile_0250.png");
                        break;
                    case 2:
                        img = ImageController.getInstance().tryGet("/img/tile_0251.png");
                        break;
                    case 3:
                        img = ImageController.getInstance().tryGet("/img/tile_0252.png");
                        break;
                }
        }else{
            switch (num) {
                case 1:
                    img = ImageController.getInstance().tryGet("/img/tile_0250_R.png");
                    break;
                case 2:
                    img = ImageController.getInstance().tryGet("/img/tile_0251_R.png");
                    break;
                case 3:
                    img = ImageController.getInstance().tryGet("/img/tile_0252_R.png");
                    break;
            }
        }
        return img;
    }
}