package game.scene;

import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;

public class EndScene extends GameScene{
    public EndScene(int num) {
        super(5, "/map/finalMap.bmp",
                new Actor(0, 0, num), new Background(1920, 1920),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 0,
                true, "end.txt");
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
