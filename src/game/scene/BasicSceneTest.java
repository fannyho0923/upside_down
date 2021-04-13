package game.scene;

import game.gameobj.*;

public class BasicSceneTest extends GameSceneTest {
    public BasicSceneTest(){
        super(new SceneSet(400, 200,
                new Background(),
                960, 640, 0,0,
                0, 0, 0, 0,
                true,
                "/map/basicMap.bmp", "/map/basicMap.txt"));
    }
}
