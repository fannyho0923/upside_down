package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.Global;
import game.utils.Vector;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public abstract class GameScene extends Scene {

    private GameObject background;
    //fanny
    private Actor actor;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> orinBrokenRoads; // 分開加入, 鏡頭改變時要重新加入
    private ArrayList<GameObject> brokenRoads;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;
    private int frameX_count = 0;
    private int frameY_count = 0;

    private String mapBmpPath;
    private String mapTxtPath;

    private Spike up;
    private Spike down;

    public GameScene(String mapBmpPath, String mapTxtPath,
                     Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     int cameraStartX, int cameraStartY,
                     boolean actorTrigCamera){
        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = mapTxtPath;
        this.actor = actor;
        this.background = background;
        this.tracker = new Tracker((cameraWidth - Global.UNIT) / 2,
                (cameraHeight - Global.UNIT) / 2, new Vector(cameraVelocityX,cameraVelocityY));
        this.actorTrigCamera = actorTrigCamera;

        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker) //
                .setCameraWindowLocation(0, 0)
                .setCameraLockDirection(false, false, false, false)
                .setCameraStartLocation(cameraStartX, cameraStartY)
                .gen();

    }

    @Override
    public void sceneBegin() {
        gameObjects = new ArrayList<>();
        orinBrokenRoads = new ArrayList<>();
        mapInit();
        brokenRoads = (ArrayList) orinBrokenRoads.clone();

        MapInformation.getInstance().setMapInfo(this.background);
        if(actorTrigCamera){
            tracker.velocity().zero();
        }
        up = new Spike(camera.painter().left(),camera.painter().top(), camera.painter().width(), 32, 1 );
        down = new Spike(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);

    }

    @Override
    public void sceneEnd() {
        this.background = null;
        this.actor = null;
        this.gameObjects = null;
        this.camera = null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        actor.velocity().setX(-Actor.WALK_SPEED);
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().setX(Actor.WALK_SPEED);
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        actor.velocity().zeroX();
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().zero();
                        break;
                    case Global.VK_SPACE:
                        actor.reverse();
                        break;
                    case Global.VK_A: //jump
                        actor.jump();
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        camera.start(g);

        if (camera.isCollision(this.background)) {
            this.background.paint(g);
        }

        gameObjects.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        brokenRoads.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });
        up.paint(g);
        down.paint(g);

        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }
        camera.paint(g);
        camera.end(g);

    }

    @Override
    public void update() {
        actor.update();
        System.out.println(actor.painter().top());
        System.out.println(actor.velocity().y());

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject obj = gameObjects.get(i);
            if (actor.isCollision(obj)) {
                obj.collisionEffect(actor);
            }
            obj.update();
            if(!obj.isExist()){
                gameObjects.remove(i);
            }
        }

        for (int i = 0; i < brokenRoads.size(); i++) {
            GameObject obj = brokenRoads.get(i);
            if (actor.isCollision(obj)) {
                obj.collisionEffect(actor);
            }
            obj.update();
            if(!obj.isExist()){
                obj.setExist(true);
                brokenRoads.remove(i);
            }
        }
        actor.move();

        // 瞬間移動, 暫時還沒用到tracker 的速度
        if(actorTrigCamera){
            if (actor.painter().centerX() < camera.painter().left()) {       // 左
                frameX_count--;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            if (actor.painter().centerY() < camera.painter().top()) {        // 上
                frameY_count--;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            if (actor.painter().centerX() > camera.painter().right()) {     // 右
                frameX_count++;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            if (actor.painter().centerY() > camera.painter().bottom()) {   // 下
                frameY_count++;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            tracker.setX( (camera.painter().width() - Global.UNIT) / 2 +
                    (camera.painter().width()*frameX_count));
            tracker.setY( (camera.painter().height() - Global.UNIT) / 2 +
                    camera.painter().height()*frameY_count);
        }else{
            tracker.update();
            if (actor.collider().right() <= camera.collider().left()) {//work left
                actor.setXY(camera.collider().right() - 1, actor.painter().top());
                return;
            }
            if (actor.collider().left() >= camera.collider().right()) {
                actor.setXY(camera.collider().left() - actor.painter().width() + 1, actor.painter().top());
                return;
            }
        }
        up = new Spike(camera.painter().left(),camera.painter().top(), camera.painter().width(), 32, 1 );
        down = new Spike(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);
        camera.update();
    }

    public void mapInit() {
        try {
            final MapLoader mapLoader = new MapLoader(mapBmpPath, mapTxtPath);
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();
            this.orinBrokenRoads.addAll(mapLoader.createObjectArray("brokenRoad", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("road", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Road(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("wall", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Wall(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileDarkGreen", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.GREEN);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spikeUp", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileRed", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.RED);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileGreen", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.GREEN);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spikeDown", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("movePlatform", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("flag", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Flag(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("pass", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Pass(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("conveyorRight", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 10);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("conveyorLeft", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, -10);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("key", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Key(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}