package game.scene;

import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;

public class Parkour extends GameScene {
    public Parkour(int num) {
        super(3, "/map/parkour.bmp",
                new Actor(0, 0, num), new Background(4800, 640),
                true, "parkour.txt");
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/parkour.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/parkour.wav");
    }
}
