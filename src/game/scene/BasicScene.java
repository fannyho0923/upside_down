package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;


public class BasicScene extends GameScene {
    public BasicScene(int num) {
        super("/map/basicMap0414.bmp", "/map/basicMap0414.txt",
                new Actor(800, 2100, num), new Background(),
                960, 640, 0, 10,true);
    }
}

