package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.GameObject;
import game.gameobj.Velocity;

public class test extends GameScene{
    @Override
    public String setMapBmpPath() {
        return "/map/fannyTestMap.bmp";
    }

    @Override
    public String setMapTxtPath() {
        return "/map/fannyTestMap.txt";
    }

    @Override
    public Actor addActor() {
        return new Actor(40,40);
    }

    @Override
    public GameObject setBackground() {
        return new Background();
    }

    @Override
    public int setCameraWidth() {
        return 960;
    }

    @Override
    public int setCameraHeight() {
        return 640;
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
    public Velocity setCameraVelocity() {
        return new Velocity(0,0,0,0,false);
    }

    @Override
    public boolean setActorTrigCamera() {
        return false;
    }
}
