package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        super(0,0, 3840,9600);//2560
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void update() {

    }

    @Override
    public void collisionEffect(Actor actor) {

    }
}
