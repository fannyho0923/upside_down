package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class StarObj extends GameObject {
    private Image image;
    private Delay delay;

    public StarObj(int x, int y, int width, int height) {
        super(x, y, width, height);
        image = ImageController.getInstance().tryGet("/img/background/stars.png");
        delay = new Delay(50);
        delay.play();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, this.painter().left(), this.painter().top(),16,16, null);
    }

    @Override
    public void update() {
        if (delay.count()) {
            this.setExist(false);
        }
    }


}
