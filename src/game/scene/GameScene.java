package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.Global;
import game.utils.Velocity;

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


    private Spike up;
    private Spike down;

    public abstract String setMapBmpPath();

    public abstract String setMapTxtPath();

    //fanny
    public abstract Actor addActor();

    public abstract GameObject setBackground();

    public abstract int setCameraWidth();

    public abstract int setCameraHeight();

    public abstract int setTrackerSpeed();

    public abstract int setCameraStartX();
    public abstract int setCameraStartY();
    public abstract Velocity setCameraVelocity();
    public abstract boolean setActorTrigCamera();



    @Override
    public void sceneBegin() {
        gameObjects = new ArrayList<>();
        orinBrokenRoads = new ArrayList<>();

        mapInit(); // load map information

        brokenRoads = (ArrayList) orinBrokenRoads.clone();

        actor = addActor();
        background = setBackground();


        int cameraWidth = setCameraWidth();
        int cameraHeight = setCameraHeight();
        MapInformation.getInstance().setMapInfo(this.background);
        tracker = new Tracker((cameraWidth - Global.MAP_UNIT) / 2,
                (cameraHeight - Global.MAP_UNIT) / 2, setCameraVelocity());
        actorTrigCamera = setActorTrigCamera();
        if(actorTrigCamera){
            tracker.velocity().stop();
        }

        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker) //
                .setCameraWindowLocation(0, 0)
                .setCameraLockDirection(false, false, false, false)
                .setCameraStartLocation(setCameraStartX(), setCameraStartY())
                .gen();

        up = new Spike(camera.painter().left(),camera.painter().top(), camera.painter().width(), 32, 1 );
        down = new Spike(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);

        if(actorTrigCamera){
            tracker.velocity().stop();

        }
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
                        actor.leftSpeedUp(true);
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().setX(Actor.WALK_SPEED);
                        actor.rightSpeedUp(true);
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        actor.leftSpeedUp(false);
                        actor.velocity().stopX();
                        break;
                    case Global.VK_RIGHT:
                        actor.rightSpeedUp(false);
                        actor.velocity().stopX();
                        break;
                    case Global.VK_SPACE:
                        actor.velocity().gravityReverse();
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

        // 瞬間移動, 暫時還沒用到tracker 的速度
        if(actorTrigCamera){
            if (actor.painter().centerX() < camera.painter().left()) {       // 左
                frameX_count--;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();

                orinBrokenRoads.forEach(a -> System.out.println(a.isExist()));
                System.out.println("=============================");
                brokenRoads.forEach(a -> System.out.println(a.isExist()));

            }
            if (actor.painter().centerY() < camera.painter().top()) {        // 上
                frameY_count--;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            if (actor.painter().centerX() > camera.painter().right()) {     // 右
                frameX_count++;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();

                orinBrokenRoads.forEach(a -> System.out.println(a.isExist()));
                System.out.println("=============================");
                brokenRoads.forEach(a -> System.out.println(a.isExist()));
            }
            if (actor.painter().centerY() > camera.painter().bottom()) {   // 下
                frameY_count++;
                brokenRoads = (ArrayList) orinBrokenRoads.clone();
            }
            tracker.setX( (camera.painter().width() - Global.MAP_UNIT) / 2 +
                    (camera.painter().width()*frameX_count));
            tracker.setY( (camera.painter().height() - Global.MAP_UNIT) / 2 +
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
            final MapLoader mapLoader = new MapLoader(setMapBmpPath(), setMapTxtPath());
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

            this.orinBrokenRoads.addAll(mapLoader.createObjectArray("brokenRoad", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("road", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Road(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("wall", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Wall(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileDarkGreen", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.GREEN);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spikeUp", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileRed", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.RED);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tileGreen", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.GREEN);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spikeDown", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("movePlatform", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("flag", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Flag(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("pass", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Pass(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("conveyorRight", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 10);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("conveyorLeft", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, -10);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("key", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
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