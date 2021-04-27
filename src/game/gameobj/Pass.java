package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;

import java.awt.*;

public class Pass extends GameObject{
    private Image img;
    private static Image[] passImgs = {
            ImageController.getInstance().tryGet("/img/gameObj/pass/pass_3.png"),
            ImageController.getInstance().tryGet("/img/gameObj/pass/pass_2.png"),
            ImageController.getInstance().tryGet("/img/gameObj/pass/pass_1.png"),
    };
    boolean isPlayed;

    private Delay delay;
    private int count;
    private static final int countMax = 3;

    public Pass(int left, int top) {
        super(left, top, 64, 70);
        this.collider().offset(-16,-38);
        isPlayed=false;
        img = ImageController.getInstance().tryGet("/img/gameObj/pass/light_white.png");
        this.delay = new Delay(15);
        this.delay.loop();
        this.count = 0;
    }

    @Override
    public void collisionEffect(Actor actor) {
        if(!isPlayed) {
            AudioResourceController.getInstance().shot("/sound/victory1.wav");
            isPlayed=true;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left()-32, painter().top()-50, null);
        g.drawImage(passImgs[count],painter().left()-19,painter().top()-38,null );
    }

    @Override
    public void update() {
        if(delay.count()){
            count = ++count % countMax;
        }
    }
}