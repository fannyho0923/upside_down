package game.scene;

import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;

public class SpeedRun extends GameScene{
    public SpeedRun(int num) {
        super(2, "/map/speedRun.bmp",
                new Actor(0,0,num), new Background(960,9600),
                960, 640, 0, -1,false,"speedrun.txt");
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/Battle-Dawn-volumeReduce.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/Battle-Dawn-volumeReduce.wav");
    }
}
