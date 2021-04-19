package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;


public class BasicScene extends GameScene {
    public BasicScene(int num) {
        super("/map/speedRunMap.bmp", "/map/Packour/genMap.txt",
                new Actor(0,0,num), new Background(),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 1,false);
    }
}

