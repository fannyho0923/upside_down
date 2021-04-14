package game.gameobj;

import game.camera.Camera;
import game.controller.ImageController;

import java.awt.*;

public class CameraBack extends GameObject{
    private Image img;
    public CameraBack(int x, int y){
        super(x,y,960,640);
        img = ImageController.getInstance().tryGet("/img/dark.png");
    }

    @Override
    public void collisionEffect(Actor actor) {
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,this.painter().left(),this.painter().top(),null);
    }

    @Override
    public void update() {

    }
}
