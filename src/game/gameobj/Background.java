package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        //x=3840 y=1920, unit_X=1280 unit_Y=640
        super(0,0, 3840,2560);
        img = ImageController.getInstance().tryGet("/img/dungeon_background.jpg");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),
                painter().width(),painter().height(),null);
    }

    @Override
    public void update() {

    }

    @Override
    public void collisionEffect(Actor actor) {

    }
}
