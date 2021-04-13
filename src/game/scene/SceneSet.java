package game.scene;

import game.gameobj.*;
import game.utils.Velocity;
import java.util.ArrayList;

public class SceneSet {
    private GameObject background;
    private Actor actor;
    private ArrayList<GameObject> gameObjects;

    private int cameraWidth;
    private int cameraHeight;
    private int cameraStartX;
    private int cameraStartY;
    private Velocity cameraVelocity;
    private int cameraVelX;
    private int cameraVelY;
    private int cameraVelDx;
    private int cameraVelDy;

    private boolean actorTrigCamera;

    private String mapBmpPath;
    private String mapTxtPath;

    public SceneSet(int actorX, int actorY,
                    GameObject background,
                    int cameraWidth, int cameraHeight, int cameraStartX, int cameraStartY,
                    int cameraVelX, int cameraVelY, int cameraVelDx, int cameraVelDy,
                    boolean actorTrigCamera,
                    String mapBmpPath, String mapTxtPath){
        this.actor = new Actor(actorX, actorY);//actor創建在畫布的位置
        this.background = background;
        this.gameObjects = new ArrayList<>();
        this.cameraWidth = cameraWidth;
        this.cameraHeight = cameraHeight;
        this.cameraStartX = cameraStartX;
        this.cameraStartY = cameraStartY;
        this.cameraVelX = cameraVelX; //正數=鏡頭向右移, 負數=鏡頭向左移
        this.cameraVelY = cameraVelY; //正數=鏡頭向下移, 負數=鏡頭向上移
        this.cameraVelDx = cameraVelDx;
        this.cameraVelDy = cameraVelDy;
        this.actorTrigCamera = actorTrigCamera; //false=固定鏡頭, true=鏡頭會動
        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = mapTxtPath;
    }

    public void setBackground(GameObject background) {
        this.background = background;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public GameObject getBackground() {
        return background;
    }

    public Velocity getCameraVelocity() {
        return new Velocity(cameraVelX, cameraVelY, cameraVelDx, cameraVelDy, false);
    }

    public Actor getActor() {
        return actor;
    }

    public String getMapBmpPath() {
        return mapBmpPath;
    }

    public String getMapTxtPath() {
        return mapTxtPath;
    }

    public int getCameraStartX() {
        return cameraStartX;
    }

    public int getCameraStartY() {
        return cameraStartY;
    }

    public int getCameraWidth() {
        return cameraWidth;
    }

    public int getCameraHeight() {
        return cameraHeight;
    }

    public boolean isActorTrigCamera() {
        return actorTrigCamera;
    }
}
