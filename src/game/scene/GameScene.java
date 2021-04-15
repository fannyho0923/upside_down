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
//    private GameObject cameraBack;
    private Actor actor;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> orinBrokenRoads; // 分開加入, 鏡頭改變時要重新加入
    private ArrayList<GameObject> brokenRoads;
    private ArrayList<GameObject> movePlatform;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;
    private int num;

    private int frameX_count = 1;
    private int frameY_count = 2;

    private String mapBmpPath;
    private String mapTxtPath;
    private Spikes spikesUp;
    private Spikes spikesDown;

    public GameScene(String mapBmpPath, String mapTxtPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera){//刪掉actor
        int cameraStartX = cameraWidth*frameX_count;
        int cameraStartY = cameraHeight*frameY_count;

        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = mapTxtPath;
        this.background = background;
        this.actor = actor;
        this.tracker = new Tracker(cameraStartX + (cameraWidth - Global.UNIT) / 2,
                cameraStartY +(cameraHeight - Global.UNIT)/2, new Velocity(cameraVelocityX,cameraVelocityY,0,0,false));
        this.actorTrigCamera = actorTrigCamera;
        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker)
                .gen();
//        cameraBack = new CameraBack(camera.painter().left(),camera.painter().top());
    }

    @Override
    public void sceneBegin() {
        gameObjects = new ArrayList<>();
        orinBrokenRoads = new ArrayList<>();
        movePlatform = new ArrayList<>();
        mapInit();
        brokenRoads = (ArrayList) orinBrokenRoads.clone();

        MapInformation.getInstance().setMapInfo(this.background);
        if(actorTrigCamera){
            tracker.velocity().stop();
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
                        actor.velocity().setX(-Actor.WALK_SPEED);
                        actor.leftSpeedUp(true);
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().setX(Actor.WALK_SPEED);
                        actor.rightSpeedUp(true);
                        break;
                    case Global.VK_SPACE:
                        if(actor.canReverse()){
                            actor.velocity().gravityReverse();
                            actor.setCanReverse(false);
                        }
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
        background.paint(g);
//        this.cameraBack.paint(g);

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

        movePlatform.forEach(a -> {
            if (camera.isCollision(a)){
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
        boolean planCollision = false;

        for (int i = 0; i < movePlatform.size(); i++){
            GameObject obj = movePlatform.get(i);
            if (actor.isCollision(obj)) {
                obj.collisionEffect(actor);
            }
            if(actor.collider().isOverlap(obj.secondCollider())){
                planCollision = true;

            }
            if(i==movePlatform.size()-1){
                if(planCollision){
                    obj.secondCollisionEffect(actor);
                }
            }
            obj.update();
        }
        actor.update();

        camera.update();
//        cameraBack.setXY(camera.painter().left(),camera.painter().top());
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

            /*移動平台------------------------------------------------*/
            this.movePlatform.addAll(mapLoader.createObjectArray("movePlatHead", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,1);
                    return tmp;
                }
                return null;
            }));

            this.movePlatform.addAll(mapLoader.createObjectArray("movePlatBody", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,2);
                    return tmp;
                }
                return null;
            }));

            this.movePlatform.addAll(mapLoader.createObjectArray("movePlatTail", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,3);
                    return tmp;
                }
                return null;
            }));

            // 易碎地, 要分開存!!
            this.orinBrokenRoads.addAll(mapLoader.createObjectArray("brokenRoad", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("born", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Born(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("savePoint", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new SavePoint(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0183", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0183R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            2);
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

            this.gameObjects.addAll(mapLoader.createObjectArray("transHead", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 10, 0, 1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("transBody", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,10, 0,2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("transTail", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,10,0,3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("transHead_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 10, 1, 1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("transBody_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,10, 1,2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("transTail_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,10,1,3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberHead", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 1, 1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberBody", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 1, 2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberTail", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,1, 3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberTail_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 0, 3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberBody_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 0, 2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubberHead_R", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 0, 1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("mon1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("mon2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("mon3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 3);
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

            /*TILES--------------------------------------------------------------------------------*/

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0195", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,195);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0196", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,196);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0197", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,197);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0215", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,215);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0216", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,216);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0217", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,217);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0235", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,235);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0236", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,236);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0237", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,237);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0239", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,239);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0259", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,259);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0219", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,219);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0199", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,199);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0000", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,0);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0291", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,291);
                    return tmp;
                }
                return null;
            }));


        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}