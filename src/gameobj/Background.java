package gameobj;

import controller.ImageController;

import java.awt.*;

public class Background extends GameObject{
    private Image img;

    public Background(){
        super(0,0, 1280,1280);
        img = ImageController.getInstance().tryGet("/img/background.jpg");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),
                painter().width(),painter().height(),null);

    }

    @Override
    public void update() {

    }
}
