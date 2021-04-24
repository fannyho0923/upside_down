package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;

public class Parkour extends GameScene {
    public Parkour(int num) {
        super(3,"/map/parkour.bmp",
                new Actor(0, 0, num), new Background(4800, 640),
                960, 640, 0, 0, true, "parkour.txt");
    }
}
