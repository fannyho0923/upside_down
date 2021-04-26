package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;

public class SpeedRun extends GameScene{
    public SpeedRun(int num) {
        super(2, "/map/speedRun.bmp",
                new Actor(0,0,num), new Background(960,10000),
                960, 640, 0, -1,false,"speedrun.txt");
    }
}
