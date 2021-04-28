package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    public Background(int width, int height){
        super(0,0,width,height);
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
