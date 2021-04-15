package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;


public class BasicScene extends GameScene {
    public BasicScene(int num) {
        super("/map/basicMap0414.bmp", "/map/basicMap0414.txt",
                new Actor(0,0,num), new Background(),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 10,true);
    }
}

