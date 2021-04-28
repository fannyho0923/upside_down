package game.scene;

import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;

public class EndScene extends GameScene{
    public EndScene(int num) {
        super(5, "/map/finalMap.bmp",
                new Actor(0, 0, num), new Background(960, 640),
                false, "end.txt");
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/end.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/end.wav");
    }
}
