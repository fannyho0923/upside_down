package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        //x=3840 y=1920
        super(0,0, 3840,1920);
//        img = ImageController.getInstance().tryGet("/img/background.jpg");
    }

    @Override
    public void paint(Graphics g) {
//        g.drawImage(img,painter().left(),painter().top(),
//                painter().width(),painter().height(),null);
    }

    @Override
    public void update() {

    }

    @Override
    public void collisionEffect(Actor actor) {

    }
}
