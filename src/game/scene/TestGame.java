package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

<<<<<<< HEAD:src/game/scene/TestGame.java
public class TestGame extends Scene {
=======
import utils.Global;

public class TestGame extends Scene{
>>>>>>> 67f0f1c3d2778b1663c66bcaa19ef5ee271f7ecf:src/scene/TestGame.java
    private Background background;
    private Actor actor;
    private ArrayList<GameObject> gameObjects;

    private Camera camera;
    private Tracker tracker;
    private TRACKER_MOVEMENT tracker_movement;


    private final int MAP_UNIT = 32;

    @Override
    public void sceneBegin() {
        gameObjects = new ArrayList<>();
        mapInit(gameObjects); // load map information
        actor = new Actor(160, 320);//160,320

        background = new Background();

        int cameraWidth = 960;//640
        int cameraHeight = 640;

        MapInformation.getInstance().setMapInfo(this.background);
        tracker = new Tracker((cameraWidth - MAP_UNIT) / 2, (cameraHeight - MAP_UNIT) / 2, 8);
        // speed 必須是camera 長寬的公因數
        tracker_movement = TRACKER_MOVEMENT.TOUCH_CAMERA;

        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker)
                .setCameraWindowLocation(0, 0)
                .setCameraLockDirection(false, false, false, false)
                .setCameraStartLocation(0, 0)
                .gen();
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
<<<<<<< HEAD:src/game/scene/TestGame.java
                switch (commandCode) {
                    case 2:
=======
                switch(commandCode){
                    case Global.VK_LEFT:
>>>>>>> 67f0f1c3d2778b1663c66bcaa19ef5ee271f7ecf:src/scene/TestGame.java
                        actor.walkLeft();
                        break;
                    case Global.VK_RIGHT:
                        actor.walkRight();
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
<<<<<<< HEAD:src/game/scene/TestGame.java
                switch (commandCode) {
                    case 2: // left
=======
                switch (commandCode){
                    case Global.VK_LEFT:
>>>>>>> 67f0f1c3d2778b1663c66bcaa19ef5ee271f7ecf:src/scene/TestGame.java
                        actor.walkStop();
                        break;
                    case Global.VK_RIGHT:
                        actor.walkStop();
                        break;
                    case Global.VK_SPACE:
                        actor.jump();
                        break;
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

        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }

        gameObjects.forEach(a ->
        {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        camera.paint(g);
        camera.end(g);

    }

    @Override
    public void update() {
        actor.update();
//        System.out.println(actor.collider().bottom());
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject obj = gameObjects.get(i);

            //fanny
            if (actor.collider().right()<=camera.collider().left()) {//work left
                actor.setXY(camera.collider().right()-1, actor.collider().bottom());
                return;
            }if (actor.collider().left()>=camera.collider().right()) {
                actor.setXY(camera.collider().left()-31, actor.collider().bottom());
                return;
            }
//            if(actor.collider().top()>=camera.collider().bottom()){
//                actor.setXY(160,320);
//                return;
//            }
            if (actor.isCollision(obj)) {
                actor.preMove();
                actor.fall();
                if (actor.isCollision(obj)) {
                    if (actor.dy() < 0) {
                        actor.setY(obj.collider().bottom());
                        actor.zeroDy();
                    } else if (actor.dy() > 0) {
                        actor.resetJumpState();
                        actor.setY(obj.collider().top() - actor.painter().height() - 1);
                        actor.zeroDy();
                    }

                    actor.walk();
                }
            }
        }


        tracker_movement.move(actor, camera, tracker);
//        tracker_movement.move(tracker);

        camera.update();
    }

    public void mapInit(ArrayList<GameObject> gameObjects) {

        try {
            final MapLoader mapLoader = new MapLoader("/map/fannyTestMap.bmp", "/map/fannyTestMap.txt");
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

            this.gameObjects.addAll(mapLoader.createObjectArray("road", MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Road(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("wall", MAP_UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Wall(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private enum TRACKER_MOVEMENT {
        TOUCH_CAMERA {
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                if (gameObject.painter().centerX() < camera.painter().left()) {       // 左
                    tracker.moveTo(tracker.painter().left() - camera.painter().width(), tracker.painter().top());
                } //left
                if (gameObject.painter().centerY() < camera.painter().top()) {         // 上
                    tracker.moveTo(tracker.painter().left(), tracker.painter().top() - camera.painter().width());
                }
                if (gameObject.painter().centerX() > camera.painter().right()) {     // 右
                    tracker.moveTo(tracker.painter().left() + camera.painter().width(), tracker.painter().top());
                }
                if (gameObject.painter().centerY() > camera.painter().bottom()) {   // 下
                    tracker.moveTo(tracker.painter().left(), tracker.painter().top() + camera.painter().width());
                }

                tracker.tryMove();
            }

            @Override
            public void move(Tracker tracker) {
            }
        },
        LEFT_ROLLING {
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                tracker.leftShift();
            }

            @Override
            public void move(Tracker tracker) {
                tracker.leftShift();
            }

        },
        UP_ROLLING {
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                tracker.up();
            }

            @Override
            public void move(Tracker tracker) {
                tracker.up();
            }
        },
        RIGHT_ROLLING {
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                tracker.rightShift();
            }

            @Override
            public void move(Tracker tracker) {
                tracker.rightShift();
            }
        },
        DOWN_ROLLING {
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                tracker.down();
            }

            @Override
            public void move(Tracker tracker) {
                tracker.down();
            }
        };

        public abstract void move(GameObject gameObject, Camera camera, Tracker tracker);

        public abstract void move(Tracker tracker);

    }

}
