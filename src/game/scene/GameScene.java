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
    private GameObject cameraBack;
    private GameObject ghost;
    //fanny
    private Actor actor;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> orinBrokenRoads; // 分開加入, 鏡頭改變時要重新加入
    private ArrayList<GameObject> brokenRoads;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;
    private int frameX_count = 1;
    private int frameY_count = 2;

    private String mapBmpPath;
    private String mapTxtPath;

    private Spikes spikesUp;
    private Spikes spikesDown;

    public GameScene(String mapBmpPath, String mapTxtPath,
                     Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera){
        int cameraStartX = cameraWidth*frameX_count;
        int cameraStartY = cameraHeight*frameY_count;

        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = mapTxtPath;
        this.background = background;

        this.actor = new Actor(cameraStartX + 200, cameraStartY+200, 2);
        ghost = new Ghost(1280, 1400);

        this.tracker = new Tracker(cameraStartX + (cameraWidth - Global.UNIT) / 2,
                cameraStartY +(cameraHeight - Global.UNIT)/2, new Vector((double)cameraVelocityX,(double)cameraVelocityY));
        this.actorTrigCamera = actorTrigCamera;
        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker)
                .gen();
        cameraBack = new CameraBack(camera.painter().left(),camera.painter().top());
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
        spikesDown = new Spikes(camera.painter().left(),camera.painter().top(),camera.painter().width(), 32, 2 );
        spikesUp = new Spikes(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);
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
                        actor.setDirection(Global.Direction.LEFT);
                        actor.velocity().setX(-Actor.WALK_SPEED);
                        break;
                    case Global.VK_RIGHT:
                        actor.setDirection(Global.Direction.RIGHT);
                        actor.velocity().setX(Actor.WALK_SPEED);
                        break;
                    case Global.VK_SPACE:
                        if(actor.canReverse()){
                            actor.reverse();
                            //actor.velocity().gravityReverse();
                            actor.setCanReverse(false);
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        actor.setDirection(Global.Direction.NO);
                        actor.velocity().zeroX();

                        //actor.velocity().stopX();
                        break;
                    case Global.VK_RIGHT:
                        actor.setDirection(Global.Direction.NO);
                        actor.velocity().zeroX();
                        //actor.velocity().stopX();
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

        this.cameraBack.paint(g);

        ghost.paint(g);

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

        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }

        spikesUp.paint(g);
        spikesDown.paint(g);

        camera.paint(g);
        camera.end(g);
    }

    @Override
    public void update() {
        ghost.update();
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

        camera.update();
        cameraBack.setXY(camera.painter().left(),camera.painter().top());
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
                System.out.println(camera.painter().left());
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
            spikesDown.painter().setXY(camera.painter().left(),camera.painter().top()-5); // 為什麼會不貼邊??
            spikesUp.painter().setXY(camera.painter().left(),camera.painter().top() + camera.painter().height() - spikesUp.painter().height());
        }
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
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Tile.Color.DARK_GREEN);
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