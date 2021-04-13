package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.GameSceneTest;
import game.utils.Global;
import game.utils.Velocity;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BasicSceneTest extends GameSceneTest {
    public BasicSceneTest(){
        super(new SceneSet(400, 200,
                new Background(),
                960, 640, 0,0,
                0, 30, 0, 0,
                true,
                "/map/basicMap.bmp", "/map/basicMap.txt"));
    }
}
