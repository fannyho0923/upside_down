package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameSceneTest extends Scene {
    private Tracker tracker;
    private Camera camera;
    private int frameX_count = 0;
    private int frameY_count = 0;
    private Spikes spikesUp;
    private Spikes spikesDown;

    private SceneSet sceneSet;
    
    public GameSceneTest(SceneSet sceneSet){
        this.sceneSet = sceneSet;
    }
    

    @Override
    public void sceneBegin() {
        mapInit(sceneSet.getGameObjects());
        MapInformation.getInstance().setMapInfo(this.sceneSet.getBackground());
        tracker = new Tracker((sceneSet.getCameraWidth() - Global.MAP_UNIT) / 2,
                (sceneSet.getCameraHeight() - Global.MAP_UNIT) / 2, sceneSet.getCameraVelocity());
        if(sceneSet.isActorTrigCamera()){
            tracker.velocity().stop();
        }
        camera = new Camera.Builder(sceneSet.getCameraWidth(), sceneSet.getCameraHeight())
                .setChaseObj(tracker) //
                .setCameraWindowLocation(0, 0)
                .setCameraLockDirection(false, false, false, false)
                .setCameraStartLocation(sceneSet.getCameraStartX(), sceneSet.getCameraStartY())
                .gen();
        if(sceneSet.isActorTrigCamera()){
            tracker.velocity().stop();
        }
        spikesDown = new Spikes(camera.painter().left(),camera.painter().top(), camera.painter().width(), 32, 2 );
        spikesUp = new Spikes(camera.painter().left(),camera.painter().bottom()-32,  camera.painter().width(), 32, 1);
    }

    @Override
    public void sceneEnd() {
        this.sceneSet.setBackground(null);
        this.sceneSet.setActor(null);
        this.sceneSet.setGameObjects(null);
        this.camera = null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        //sceneSet.getActor().velocity().setX(-sceneSet.getActor().WALK_SPEED);
                        sceneSet.getActor().leftSpeedUp(true);
                        break;
                    case Global.VK_RIGHT:
                        //sceneSet.getActor().velocity().setX(sceneSet.getActor().WALK_SPEED);
                        sceneSet.getActor().rightSpeedUp(true);
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        sceneSet.getActor().leftSpeedUp(false);
                        sceneSet.getActor().velocity().stopX();
                        break;
                    case Global.VK_RIGHT:
                        sceneSet.getActor().rightSpeedUp(false);
                        sceneSet.getActor().velocity().stopX();
                        break;
                    case Global.VK_SPACE:
                        sceneSet.getActor().velocity().gravityReverse();
                        break;
                    case Global.VK_A: //jump
                        sceneSet.getActor().jump();
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

        if (camera.isCollision(this.sceneSet.getBackground())) {
            this.sceneSet.getBackground().paint(g);
        }

        if (camera.isCollision(this.sceneSet.getActor())) {
            this.sceneSet.getActor().paint(g);
        }

        sceneSet.getGameObjects().forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        spikesUp.paint(g);
        spikesDown.paint(g);

        camera.paint(g);
        camera.end(g);
    }

    @Override
    public void update() {
        sceneSet.getActor().update();

        for (int i = 0; i < sceneSet.getGameObjects().size(); i++) {
            GameObject obj = sceneSet.getGameObjects().get(i);
            if (sceneSet.getActor().isCollision(obj)) {

                sceneSet.getActor().preMove();
                sceneSet.getActor().moveY();
                if (sceneSet.getActor().isCollision(obj)) { // 撞到 Y
                    sceneSet.getActor().jumpReset();
                    if (sceneSet.getActor().velocity().y() < 0) {
                        sceneSet.getActor().setY(obj.collider().bottom() + 1);
                        sceneSet.getActor().velocity().stopY();
                    } else if (sceneSet.getActor().velocity().y() > 0) {
                        sceneSet.getActor().setY(obj.collider().top() - sceneSet.getActor().painter().height() - 1);
                        sceneSet.getActor().velocity().stopY();
                    }
                    sceneSet.getActor().moveX();

                }
                // y還沒被修正的掉落? 應該是撞到前一塊物件
                if (sceneSet.getActor().collider().bottom() == obj.collider().top() |
                        sceneSet.getActor().collider().top() == obj.collider().bottom()) {
                    sceneSet.getActor().moveX();
                }
                // 撞到牆的反彈還沒修正
            }
            obj.update();
        }

        // 瞬間移動, 暫時還沒用到tracker 的速度
        camera.update();
        if(sceneSet.isActorTrigCamera()){
            if (sceneSet.getActor().painter().centerX() < camera.painter().left()) {       // 左
                frameX_count--;
            }
            if (sceneSet.getActor().painter().centerY() < camera.painter().top()) {        // 上
                frameY_count--;
            }
            if (sceneSet.getActor().painter().centerX() > camera.painter().right()) {     // 右
                frameX_count++;
            }
            if (sceneSet.getActor().painter().centerY() > camera.painter().bottom()) {   // 下
                frameY_count++;
            }
            tracker.setX( (camera.painter().width() - Global.MAP_UNIT) / 2 +
                    (camera.painter().width()*frameX_count));
            tracker.setY( (camera.painter().height() - Global.MAP_UNIT) / 2 +
                    camera.painter().height()*frameY_count);
        }else{
            tracker.update();

            //fanny 左右穿牆
            if (sceneSet.getActor().collider().right() <= camera.collider().left()) {//work left
                sceneSet.getActor().setXY(camera.collider().right() - 1, sceneSet.getActor().painter().top());
                return;
            }
            if (sceneSet.getActor().collider().left() >= camera.collider().right()) {
                sceneSet.getActor().setXY(camera.collider().left() - sceneSet.getActor().painter().width() + 1, sceneSet.getActor().painter().top());
                return;
            }
            spikesDown.offsetY(camera.painter().top()-spikesDown.painter().top());
            spikesUp.offsetY(camera.painter().bottom()-spikesUp.painter().bottom());
        }
    }

    public void mapInit(ArrayList<GameObject> gameObjects) {

        try {
            final MapLoader mapLoader = new MapLoader(sceneSet.getMapBmpPath(), sceneSet.getMapTxtPath());
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("road", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Road(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("wall", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Wall(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("tileDarkGreen", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("spikeUp", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            1);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("tileRed", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("tileGreen", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("spikeDown", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Spike(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size,
                            2);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("monster", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("movePlatform", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new MovePlatform(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("pass", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Pass(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("rubber", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Rubber(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("conveyorRight", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 2);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("conveyorLeft", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, 2);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("brokenRoad", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.sceneSet.getGameObjects().addAll(mapLoader.createObjectArray("key", Global.MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
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
