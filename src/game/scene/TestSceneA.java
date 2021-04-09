package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.camera.SmallMap;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.utils.CommandSolver;
import game.utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TestSceneA extends Scene {

    private final ArrayList<GameObject> gameObjectArr = new ArrayList();
    private TestMap testMap;
    private SmallMap smallMap; // not use
    private ArrayList<Camera> camArr;
    private TestAirplane testAirplane;
    private ArrayList<TestBoom> testAmmo;
    private ArrayList<TestEnemy> testEnemies;
    private TestObject testObject;

    @Override
    public void sceneBegin() {
        this.testMap = new TestMap();
        mapInit();
        //紀錄地圖資訊
        MapInformation.getInstance().setMapInfo(this.testMap);
        this.testAirplane = new TestAirplane(200, 200); //飛機初始位置為(200,200)
        this.testEnemies = new ArrayList<>();
        this.testAmmo = new ArrayList<>();
        this.testObject = new TestObject(0, 0);
        this.camArr = new ArrayList<>();
        this.camArr.add(new Camera.Builder(Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT - 100)// 創建一個400*500的鏡頭
                .setChaseObj(this.testObject)// 追蹤測試物件(testObject) X軸追焦係數為1 Y軸追焦係數為1
                .setCameraWindowLocation(0, 0) //鏡頭在視窗顯示位置(左上角)
                .setCameraLockDirection(true, false, false, false)//鏡頭左方向鎖住
                .setCameraStartLocation(0, 0) //設定鏡頭在地圖絕對座標的初始位置(左上角)
                .gen());
        this.camArr.add(new Camera.Builder(Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT - 100)// 創建一個400*500的鏡頭
                .setChaseObj(this.testAirplane)// 追蹤飛機(testAirplane) X軸追焦係數為1 Y軸追焦係數為1
                .setCameraWindowLocation(Global.WINDOW_WIDTH / 2, 0) //鏡頭在視窗顯示位置(左上角)
                .setCameraStartLocation(0, 0) //設定鏡頭在地圖絕對座標的初始位置(左上角)
                .gen());
        this.smallMap = new SmallMap(new Camera.Builder(MapInformation.getInstance().width(), MapInformation.getInstance().height()).setChaseObj(this.testObject).setCameraWindowLocation(Global.WINDOW_WIDTH - 180, Global.WINDOW_HEIGHT - 110).setCameraStartLocation(0, 0).gen(), 0.1, 0.1);
    }

    @Override
    public void sceneEnd() {
        this.testAmmo = null;
        this.testEnemies = null;
        this.testAirplane = null;
        this.camArr = null;
        this.smallMap = null;
        this.testMap = null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(final int commandCode, final long trigTime) {
                if (commandCode < Global.Direction.values().length) {
                    TestSceneA.this.testAirplane.changeDir(commandCode);
                }
            }

            @Override
            public void keyReleased(final int commandCode, final long trigTime) {
                TestSceneA.this.testAirplane.changeDir(Global.Direction.NO.ordinal());
                if (commandCode == Global.SPACE) {
                    TestSceneA.this.testAmmo.add(new TestBoom((TestSceneA.this.testAirplane.painter().centerX()), TestSceneA.this.testAirplane.painter().top()));
                }
            }

            @Override
            public void keyTyped(final char c, final long trigTime) {

            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(final Graphics g) {
        for (int k = 0; k < this.camArr.size(); k++) {
            final Camera cam = this.camArr.get(k);
            cam.start(g);
            if (cam.isCollision(this.testMap)) {
                this.testMap.paint(g);
                this.gameObjectArr.forEach(a -> a.paint(g));
            }
            for (int i = 0; i < this.testAmmo.size(); i++) {
                if (cam.isCollision(this.testAmmo.get(i))) {
                    this.testAmmo.get(i).paint(g);
                }
            }
            for (int i = 0; i < this.testEnemies.size(); i++) {
                if (cam.isCollision(this.testEnemies.get(i))) {
                    this.testEnemies.get(i).paint(g);
                }
            }
            if (cam.isCollision(this.testAirplane)) {
                this.testAirplane.paint(g);
            }
            cam.paint(g);
            cam.end(g);
            g.setColor(Color.red);
            final int x = cam.cameraWindowX() + 20;
            final int y = cam.cameraWindowY() + 20;
            g.drawString("這是第" + (k + 1) + " 個camera!!!", x, y);
        }
        this.smallMap.start(g);
        if (this.smallMap.isCollision(this.testMap)) {
            this.testMap.paint(g);
        }
        if (this.smallMap.isCollision(this.testAirplane)) {
            // 使用接口Paint自定義畫出的方式
            final SmallMap.Paint tmp = (Graphics g1, GameObject obj) -> {
                final Color c = this.smallMap.getColor("" + obj.getClass());
                g.setColor(c);
                g.fillOval(obj.painter().left(), obj.painter().top(), this.testAirplane.painter().width(), this.testAirplane.painter().height());
                g.setColor(Color.BLACK);
            };
            tmp.paint(g, this.testAirplane);
        }
        for (int i = 0; i < this.testEnemies.size(); i++) {
            if (this.smallMap.isCollision(this.testEnemies.get(i))) {
                this.smallMap.paint(g, this.testEnemies.get(i), this.testEnemies.get(i).img);
            }
        }
        for (int i = 0; i < this.camArr.size(); i++) {
            if (this.smallMap.isCollision(this.camArr.get(i))) {
                this.smallMap.paint(g, this.camArr.get(i), Color.yellow);//畫追蹤的鏡頭框
            }
        }
        this.smallMap.end(g);
        g.setColor(Color.red);
        final int x = this.smallMap.cameraWindowX() + 20;
        final int y = this.smallMap.cameraWindowY() + 20;
        g.drawString("這是小地圖!!!", x, y);
    }

    @Override
    public void update() {
        //確保更新後再做碰撞判斷
        //隨機產生飛機
        this.testObject.update();
        if (Global.random(1, 100) <= 5) {
            this.testEnemies.add(new TestEnemy(Global.random(0, Global.WINDOW_WIDTH / 10), 0, Global.random(-5, 5)));
        }
        for (int i = 0; i < this.camArr.size(); i++) {
            this.camArr.get(i).update();
        }
        this.testAirplane.update();
        for (int i = 0; i < this.testAmmo.size(); i++) {
            this.testAmmo.get(i).update();
            if (this.testAmmo.get(i).getState() == TestBoom.State.DEAD) {
                this.testAmmo.remove(i);
                i--;
                continue;
            }
            if (this.testAmmo.get(i).isOutOfScreen()) {
                this.testAmmo.remove(i);
                i--;
            }
        }
        for (int i = 0; i < this.testEnemies.size(); i++) {
            this.testEnemies.get(i).update();
            if (this.testAirplane.isCollision(this.testEnemies.get(i))) {
                this.testEnemies.remove(i);
                i--;
                continue;
            }
            if (this.testEnemies.get(i).isOutOfScreen()) {
                this.testEnemies.remove(i);
                i--;
                continue;
            }
            for (int j = 0; j < this.testAmmo.size(); j++) {
                if (this.testAmmo.get(j).getState() != TestBoom.State.ALIVE) {
                    continue;
                }
                if (this.testEnemies.get(i).isCollision(this.testAmmo.get(j))) {
                    this.testAmmo.get(j).setState(TestBoom.State.BOOM);
                    this.testEnemies.remove(i);
                    break;
                }
            }
        }
        this.smallMap.update();
    }

    public void mapInit() {
        final int objectSize = 32;
        try {
            final MapLoader mapLoader = new MapLoader("/genMAP.bmp", "/genMap.txt");
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

            this.gameObjectArr.addAll(mapLoader.createObjectArray("Tile_01", objectSize, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile_01(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjectArr.addAll(mapLoader.createObjectArray("Tile_02", objectSize, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile_02(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjectArr.addAll(mapLoader.createObjectArray("Tile_12", objectSize, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile_12(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));

            this.gameObjectArr.addAll(mapLoader.createObjectArray("Tile_03", objectSize, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile_03(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                    return tmp;
                }
                return null;
            }));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}