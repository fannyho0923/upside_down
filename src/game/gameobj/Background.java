package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        super(0,0, 5000,640);
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
