package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        super(0,0, 3840,2560);
        img = ImageController.getInstance().tryGet("/img/dungeon_background_1226.jpg");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),null);
    }

    @Override
    public void update() {

    }

    @Override
    public void collisionEffect(Actor actor) {

    }
}
