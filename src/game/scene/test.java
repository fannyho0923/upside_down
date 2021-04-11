package game.scene;

import game.gameobj.Actor;
import game.gameobj.ActorPro;
import game.gameobj.Background;
import game.gameobj.GameObject;

public class test extends GameScene{
    @Override
    public Actor addActor() {
        return new Actor(50, 200);
    }
//fanny
    @Override
    public ActorPro addActorPro() {
        return new ActorPro(50, 200,0);
    }

    @Override
    public GameObject setBackground() {
        return new Background();
    }

    @Override
    public int setCameraWidth() {
        return 640;
    }

    @Override
    public int setCameraHeight() {
        return 640;
    }

    @Override
    public GameScene.TRACKER_MOVEMENT setTrackerMovement() {
        return TRACKER_MOVEMENT.TOUCH_CAMERA;
    }

    @Override
    public int setTrackerSpeed() {
        return 4;
    }

    @Override
    public int setCameraStartX() {
        return 0;
    }

    @Override
    public int setCameraStartY() {
        return 0;
    }

    @Override
    public String setMapBmpPath() {
        return "/map/testMap.bmp";
    }

    @Override
    public String setMapTxtPath() {
        return "/map/testMap.txt";
    }
}
