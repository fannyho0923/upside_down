package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;


public class BasicScene extends GameScene {
    public BasicScene(int num) {
        super("/map/basicMap.bmp",
                new Actor(0,0,num), new Background(1920,1920),
                960, 640, 0, 0,true);
    }
}

