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

import game.utils.Global;

public class TestGame extends Scene{
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
        actor = new Actor(50,200);//160,300
        background = new Background();

        int cameraWidth = 640;
        int cameraHeight = 640;

        MapInformation.getInstance().setMapInfo(this.background);
        tracker = new Tracker((cameraWidth-MAP_UNIT)/2, (cameraHeight-MAP_UNIT)/2, 64);
        // speed 必須是camera 長寬的公因數
        tracker_movement = TRACKER_MOVEMENT.TOUCH_CAMERA;

        camera = new Camera.Builder(cameraWidth,cameraHeight)
                .setChaseObj(tracker)
                .setCameraWindowLocation(0,0)
                .setCameraLockDirection(false,false,false,false)
                .setCameraStartLocation(0,0)
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
                switch(commandCode){
                    case Global.VK_LEFT:
                        //actor.velocity().setX(-actor.WALK_SPEED);
                        actor.leftSpeedUp(true);
                        break;
                    case Global.VK_RIGHT:
                        //actor.velocity().setX(actor.WALK_SPEED);
                        actor.rightSpeedUp(true);
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode){
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

        if(camera.isCollision(this.background)){
            this.background.paint(g);
        }

        if(camera.isCollision(this.actor)){
            this.actor.paint(g);
        }

        gameObjects.forEach(a-> {
            if(camera.isCollision(a)) {
                a.paint(g);
            }
        });

        camera.paint(g);
        camera.end(g);
    }

    @Override
    public void update() {
        actor.update();



        for (int i = 0; i < gameObjects.size(); i++){
            GameObject obj = gameObjects.get(i);
            if(actor.isCollision(obj)) {

                actor.preMove();
                actor.moveY();
                if(actor.isCollision(obj)){ // 撞到 Y
                    if(actor.velocity().y() < 0){
                        actor.setY(obj.collider().bottom() +1);
                        actor.velocity().stopY();
                    }
                    else if (actor.velocity().y() > 0){
                        actor.setY(obj.collider().top() - actor.painter().height() -1);
                        actor.velocity().stopY();
                    }
                    actor.moveX();

                }
                // y還沒被修正的掉落? 應該是撞到前一塊物件
                if(actor.collider().bottom() == obj.collider().top() |
                   actor.collider().top() == obj.collider().bottom()){
                    actor.moveX();
                }
                // 撞到牆的反彈還沒修正
            }
        }

        //fanny 左右穿牆 , 如果使用touch camera 不會矛盾, 角色到不了, 不可能觸發事件
        if (actor.collider().right()<=camera.collider().left()) {//work left
            actor.setXY(camera.collider().right() -1, actor.painter().top());
            return;
        }if (actor.collider().left()>=camera.collider().right()) {
            actor.setXY(camera.collider().left() - actor.painter().width() +1 , actor.painter().top());
            return;
        }

        tracker_movement.move(actor,camera,tracker);
//        tracker_movement.move(tracker);
        camera.update();
    }

    public void mapInit(ArrayList<GameObject> gameObjects) {

        try {
<<<<<<< HEAD
            final MapLoader mapLoader = new MapLoader("/map/testMap.bmp", "/map/testMap.txt");
=======
            final MapLoader mapLoader = new MapLoader("/map/fannyTestMap.bmp", "/map/fannyTestMap.txt");
            //final MapLoader mapLoader = new MapLoader("/map/testMap.bmp", "/map/testMap.txt");
>>>>>>> 33f12e96745639ea23aa3e3b0e2bd37708ea30da
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

    private enum TRACKER_MOVEMENT{
        TOUCH_CAMERA{
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {
                if(gameObject.painter().centerX() < camera.painter().left()){       // 左
                    tracker.moveTo(tracker.painter().left()-camera.painter().width(),tracker.painter().top());
                } //left
                if(gameObject.painter().centerY() < camera.painter().top()){         // 上
                    tracker.moveTo(tracker.painter().left(),tracker.painter().top()-camera.painter().width());
                }
                if(gameObject.painter().centerX()> camera.painter().right()){     // 右
                    tracker.moveTo(tracker.painter().left()+camera.painter().width() ,tracker.painter().top());
                }
                if(gameObject.painter().centerY() > camera.painter().bottom()){   // 下
                    tracker.moveTo(tracker.painter().left(),tracker.painter().top()+camera.painter().width());
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
        },
        HOLD{
            @Override
            public void move(GameObject gameObject, Camera camera, Tracker tracker) {

            }

            @Override
            public void move(Tracker tracker) {

            }
        };

        public abstract void move(GameObject gameObject, Camera camera, Tracker tracker);
        public abstract void move(Tracker tracker);

    }

}
