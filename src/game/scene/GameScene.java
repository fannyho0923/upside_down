package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.controller.AudioResourceController;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.Global;
import game.utils.Velocity;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public abstract class GameScene extends Scene {

    private GameObject background;
    private Actor actor;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> orinBrokenRoads; // 分開加入, 鏡頭改變時要重新加入
    private ArrayList<GameObject> brokenRoads;
    private ArrayList<GameObject> movePlatform;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;

    private int frameX_count;
    private int frameY_count;

    private String mapBmpPath;
    private String mapTxtPath;
    private Spikes spikesUp;
    private Spikes spikesDown;

    public GameScene(String mapBmpPath, String mapTxtPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera){

        gameObjects = new ArrayList<>();
        orinBrokenRoads = new ArrayList<>();
        movePlatform = new ArrayList<>();

        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = mapTxtPath;

        mapInit();
        this.actor = actor;
//        frameX_count = gameObjects.get(0).collider().left() / cameraWidth;
//        frameY_count = gameObjects.get(0).collider().top() / cameraHeight;
        frameX_count=0;
        frameY_count=0;

//        actor.setXY(gameObjects.get(0).painter().left(),gameObjects.get(0).painter().top());
        actor.setXY(370,60);
        actor.setRebornX(actor.painter().left());
        actor.setRebornY(actor.painter().top());

        this.background = background;

        int cameraStartX = cameraWidth*frameX_count;
        int cameraStartY = cameraHeight*frameY_count;
        this.tracker = new Tracker(cameraStartX + (cameraWidth - Global.UNIT) / 2,
                cameraStartY +(cameraHeight - Global.UNIT)/2, new Velocity(cameraVelocityX,cameraVelocityY,0,0,false));
        this.actorTrigCamera = actorTrigCamera;
        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker)
                .gen();
    }

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().loop("/sound/Battle-Dawn-crop-reduce.wav",50);

        brokenRoads = (ArrayList) orinBrokenRoads.clone();

        MapInformation.getInstance().setMapInfo(this.background);
        if(actorTrigCamera){
            tracker.velocity().stop();
        }
//        spikesDown = new Spikes(camera.painter().left(),camera.painter().top(),camera.painter().width(), 32, 2 );
//        spikesUp = new Spikes(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/Battle-Dawn-crop-reduce.wav");
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

//        movePlatform.forEach(a -> {
//            if (camera.isCollision(a)){
//                a.paint(g);
//            }
//        });

        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }

//        spikesUp.paint(g);
//        spikesDown.paint(g);

        camera.paint(g);
        camera.end(g);
    }

    @Override
    public void update() {
        actor.update();

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

        actor.shift();

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
//            spikesDown.painter().setXY(camera.painter().left(),camera.painter().top()-5); // 為什麼會不貼邊??
//            spikesUp.painter().setXY(camera.painter().left(),camera.painter().top() + camera.painter().height() - spikesUp.painter().height());
        }
    }


    public void mapInit() {
        try {
            final MapLoader mapLoader = new MapLoader(mapBmpPath, mapTxtPath);
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

//          /*移動平台------------------------------------------------*/
//            因為有兩個碰撞框要update, 要分開存
            // 易碎地, 要分開存!!
            this.orinBrokenRoads.addAll(mapLoader.createObjectArray("broken1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.A);
                    return tmp;
                }
                return null;
            }));

            this.orinBrokenRoads.addAll(mapLoader.createObjectArray("broken2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.B);
                    return tmp;
                }
                return null;
            }));


            // 出生點，存第一個
//            this.gameObjects.addAll(mapLoader.createObjectArray("born", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
//                final GameObject tmp;
//                if (gameObject.equals(name)) {
//                    tmp = new Born(mapInfo.getX() * size, mapInfo.getY() * size);
//                    return tmp;
//                }
//                return null;
//            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.A1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.B1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back3-2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back3-3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back3-4", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C4);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back4-1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back4-2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back4-3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back4-4", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D4);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back5", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.E1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back6", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.F1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back11", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.G1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back12", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.H1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back13", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny13);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back14", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny14);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back15", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny15);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back16", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny16);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back17", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny17);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back18", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny18);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("back19", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny19);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("savePoint", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new SavePoint(mapInfo.getX() * size, mapInfo.getY() * size);
                    return tmp;
                }
                return null;
            }));

            //tiles
            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0195", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0195);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0196", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0196);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0197", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0197);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0198", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0198);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0215", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0215);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0235", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0235);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0236", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0236);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0237", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0237);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0238", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0238);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0255", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0255);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0256", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0256);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0257", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0257);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_999", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_999);
                    return tmp;
                }
                return null;
            }));

            //spikes
            this.gameObjects.addAll(mapLoader.createObjectArray("spike_left", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, Spike.Type.left);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spike_top", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, Spike.Type.top);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spike_right", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, Spike.Type.right);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("spike_down", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, Spike.Type.down);
                    return tmp;
                }
                return null;
            }));

            // rubber

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_h1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.h1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_h2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.h2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_h3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.h3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_v1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.v1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_v2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.v2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("rubber_v3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, Rubber.Type.v3);
                    return tmp;
                }
                return null;
            }));

            //Conveyor
            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-L1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-L2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-L3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR3);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR1);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR2);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR3);
                    return tmp;
                }
                return null;
            }));

            // monster
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}