package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.GameObject;
import game.utils.Velocity;



public class BasicScene extends GameScene {
    public BasicScene() {
        super("/map/basicMap0411.bmp", "/map/basicMap0411.txt",
                new Actor(400, 200, 2), new Background(),
                960, 640, 0, 10,false);
    }
}

