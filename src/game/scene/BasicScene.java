package game.scene;

import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;

public class BasicScene extends GameScene {

    public BasicScene(int num) {
        super(1, "/map/genMap.bmp",
                new Actor(0, 0, num), new Background(1920, 1920),
                true, "basic.txt");
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/Battle-Conflict-volumeReduce.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/Battle-Conflict-volumeReduce.wav");
    }
}

