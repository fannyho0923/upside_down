package game.gameobj;

import game.controller.ImageController;
import game.utils.Delay;

import java.awt.*;

public class BrokenRoad extends MapObject{
    private Image img;
    private boolean isTouched;
    private boolean isExist;
    private Delay delay;
    public BrokenRoad(int top, int left, int width, int height) {
        super(top, left, width, height);
        img = ImageController.getInstance().tryGet("/img/brokenRoad_1x1.png");
        isTouched = false;
        isExist = true;
        delay = new Delay(10);
    }

    @Override
    public void collisionEffect(Actor actor) {
            delay.play();
    }

    public boolean isExist(){
        return isExist;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        if(isTouched){
            delay.play();
        }
        if(delay.count()){
            isExist = false;
        }
    }
}