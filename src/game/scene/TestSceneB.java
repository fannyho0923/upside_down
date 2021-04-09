package game.scene;

import game.Internect.ClientClass;
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

public class TestSceneB extends Scene {

    private final ArrayList<GameObject> gameObjectArr = new ArrayList();
    private TestMap testMap;
    private SmallMap smallMap;
    private ArrayList<Camera> camArr;
//    private TestAirplane testAirplane;
    private ArrayList<TestAirplane> airplanes;
    private int airplaneCount;
    private ArrayList<TestBoom> testAmmo;
    private ArrayList<TestEnemy> testEnemies;
    private TestObject testObject;

    @Override
    public void sceneBegin() {
        this.testMap = new TestMap();
        mapInit();
        //紀錄地圖資訊
        MapInformation.getInstance().setMapInfo(this.testMap);
        airplaneCount = 0;
        airplanes = new ArrayList<>();
        airplanes.add(new TestAirplane(100,300));
        airplanes.get(airplaneCount++).setId(ClientClass.getInstance().getID());

        this.testEnemies = new ArrayList<>();
        this.testAmmo = new ArrayList<>();
        this.testObject = new TestObject(0, 0);
        this.camArr = new ArrayList<>();
        this.camArr.add(new Camera.Builder(Global.WINDOW_WIDTH - 100, Global.WINDOW_HEIGHT - 100)// 創建一個700*500的鏡頭
                .setChaseObj(this.airplanes.get(0))// 追蹤測試物件(testObject) X軸追焦係數為1 Y軸追焦係數為1
                .setCameraWindowLocation(0, 0) //鏡頭在視窗顯示位置(左上角)
                .setCameraLockDirection(false, false, false, false)//鏡頭左方向鎖住
                .setCameraStartLocation(0, 0) //設定鏡頭在地圖絕對座標的初始位置(左上角)
                .gen());
        this.smallMap = new SmallMap(new Camera.Builder(MapInformation.getInstance().width(), MapInformation.getInstance().height()).setChaseObj(this.testObject).setCameraWindowLocation(Global.WINDOW_WIDTH - 180, Global.WINDOW_HEIGHT - 110).setCameraStartLocation(0, 0).gen(), 0.1, 0.1);
    }

    @Override
    public void sceneEnd() {
        this.testAmmo = null;
        this.testEnemies = null;
//        this.testAirplane = null;
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
                    TestSceneB.this.airplanes.get(0).changeDir(commandCode);
                }
            }

            @Override
            public void keyReleased(final int commandCode, final long trigTime) {
                TestSceneB.this.airplanes.get(0).changeDir(Global.Direction.NO.ordinal());
                if (commandCode == Global.SPACE) {
                    TestSceneB.this.testAmmo.add(new TestBoom((TestSceneB.this.airplanes.get(0).painter().centerX()), TestSceneB.this.airplanes.get(0).painter().top()));
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
            for(int i = 0; i < airplanes.size(); i++) {
                if (cam.isCollision(airplanes.get(i))) {
                    airplanes.get(i).paint(g);
                }
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
        for(int i = 0; i < airplanes.size(); i++) {
            if (this.smallMap.isCollision(airplanes.get(i))) {
                // 使用接口Paint自定義畫出的方式
                final SmallMap.Paint tmp = (Graphics g1, GameObject obj) -> {
                    final Color c = this.smallMap.getColor("" + obj.getClass());
                    g.setColor(c);
                    //這邊有問題
                    g.fillOval(obj.painter().left(), obj.painter().top(), airplanes.get(0).painter().width(), airplanes.get(0).painter().height());
                    g.setColor(Color.BLACK);
                };
                tmp.paint(g, airplanes.get(i));
            }
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
        airplanes.get(0).update();
//        //當操控物件(飛機)的左方碰到鏡頭邊界時，飛機位置會被限制在鏡頭的最左方
//        if (airplanes.get(0).collider().left() <= this.camArr.get(0).collider().left()) {
//            airplanes.get(0).setX(this.camArr.get(0).collider().left());
//        }
//        //當操控物件(飛機)的上方碰到鏡頭邊界時，飛機位置會被限制在鏡頭的最上方
//        if (airplanes.get(0).collider().top() <= this.camArr.get(0).collider().top()) {
//            airplanes.get(0).setY(this.camArr.get(0).collider().top());
//        }
//        //當操控物件(飛機)的右方碰到鏡頭邊界時，飛機會以飛機移動速度往左推
//        if (airplanes.get(0).collider().right() >= this.camArr.get(0).collider().right()) {
//            airplanes.get(0).offsetX(-TestAirplane.getMoveSpeed());
//        }
//        //當操控物件(飛機)的右方碰到鏡頭邊界時，飛機位置會被限制在鏡頭的最右邊
//        if (airplanes.get(0).collider().bottom() >= this.camArr.get(0).collider().bottom()) {
//            airplanes.get(0).offsetY(-TestAirplane.getMoveSpeed());
//        }
        //---------------------------------------------網路部分--------------------------------------------------------------------------
        ArrayList<String> strs = new ArrayList<String>();
        strs.add(airplanes.get(0).painter().left() + "");
        strs.add(airplanes.get(0).painter().top() + "");
        ClientClass.getInstance().sent(ClientClass.CommandTest.CONNECT_AIRPLANE/*指令編號*/,strs/*參數串成的字串*/);//傳座標是為了在本地加入client傳來的飛機
        ClientClass.getInstance().sent(ClientClass.CommandTest.MOVE_AIRPLANE/*指令編號*/,strs/*參數串成的字串*/);
        ClientClass.getInstance().consume((int serialNum, int commandCode, ArrayList<String> strs1) -> {
            //這裡的實作方法 全都是為了接收client端的資料並更新的 所以有關本地更新的不要寫在這裡
            if (serialNum == airplanes.get(0).getId()) {//若ID與自己的ID相同則return 因為我不要在這裡更新本地的飛機
                return;
            }
            switch (commandCode) {
                case ClientClass.CommandTest.CONNECT_AIRPLANE:
                    boolean isBorn = false;
                    for (int i = 0; i < airplanes.size(); i++) {
                        if (airplanes.get(i).getId() == serialNum) {
                            isBorn = true;
                            break;
                        }
                    }
                    if (!isBorn) {                           //第一次加入client端飛機
                        airplanes.add(new TestAirplane(Integer.parseInt(strs1.get(0)), Integer.parseInt(strs1.get(1))));
                        airplanes.get(airplaneCount++).setId(serialNum);
                    }
                    break;
                case ClientClass.CommandTest.MOVE_AIRPLANE:
                    for (int i = 0; i < airplanes.size(); i++) {
                        if (airplanes.get(i).getId() == serialNum) {//因為前面if (serialNum == airPlanes.get(0).id) 已經把本地飛機過濾掉
                            airplanes.get(i).setX(Integer.parseInt(strs1.get(0)));
                            airplanes.get(i).setY(Integer.parseInt(strs1.get(1)));
                            break;
                        }
                    }
                    break;
//                case game.Internect.ClientClass.CommandTest.SHOT_BOOM:
//                    boom.add(new Boom(Integer.parseInt(strs1.get(0)), Integer.parseInt(strs1.get(1))));
//                    break;

                case ClientClass.CommandTest.CONNECT_ENEMY:
                    testEnemies.add(new TestEnemy(Integer.valueOf(strs1.get(0)),Integer.valueOf(strs1.get(1)),Integer.valueOf(strs1.get(2))));
                    break;

                case ClientClass.CommandTest.REMOVE_ENEMY:
                    for(int i=0;i<testEnemies.size();i++){
                        if(testEnemies.get(i).painter().left()==Integer.parseInt(strs1.get(0)) && testEnemies.get(i).painter().top()==Integer.parseInt(strs1.get(1))){
                            testEnemies.remove(i);
                        }
                    }
                    break;

                case ClientClass.CommandTest.DISCONNECT:
                    for(int i = 0; i < airplanes.size(); i++){
                        if(airplanes.get(i).getId() == Integer.valueOf(strs1.get(0))){
                            airplanes.remove(i);
                            airplaneCount--;
                        }
                    }
                    break;

            }
        });
        //---------------------------------------------↑網路部分--------------------------------------------------------------------------


        if (Global.IS_SERVER && Global.IS_INTERNET) {
            if (Global.random(1, 100) <= 20) {
                int x = Global.random(0, Global.WINDOW_WIDTH / 10);
                int y = 0;
                int d = Global.random(-5, 5);
                this.testEnemies.add(new TestEnemy(x, y, d));
                ArrayList<String> str = new ArrayList<String>();
                str.add(x + "");
                str.add(y + "");
                str.add(d + "");
                ClientClass.getInstance().sent(ClientClass.CommandTest.CONNECT_ENEMY/*指令編號*/, str/*參數串成的字串*/);
            }
        }else if(!Global.IS_INTERNET){
            if(Global.random(1, 100) < 20) {
                int x = Global.random(0, 600);
                int y = 0;
                int d = Global.random(-5, 5);
                this.testEnemies.add(new TestEnemy(x, y, d));
            }
        }
        //----------------------------------------------------↑上面為生成敵機

        //----------------------------------------------------改動敵機
        for (int i = 0; i < this.camArr.size(); i++) {
            this.camArr.get(i).update();
        }

//        for (int i = 0; i < this.testAmmo.size(); i++) {
//            this.testAmmo.get(i).update();
//            if (this.testAmmo.get(i).getState() == TestBoom.State.DEAD) {
//                this.testAmmo.remove(i);
//                i--;
//                continue;
//            }
//            if (this.testAmmo.get(i).isOutOfScreen()) {
//                this.testAmmo.remove(i);
//                i--;
//            }
//        }
        for (int i = 0; i < this.testEnemies.size(); i++) {
            this.testEnemies.get(i).update();
            for(int k = 0; k < airplanes.size(); k++) {
                if (this.airplanes.get(k).isCollision(this.testEnemies.get(i))) {
                    ArrayList<String> strs4 = new ArrayList<String>();
                    strs4.add(testEnemies.get(i).painter().left() + "");
                    strs4.add(testEnemies.get(i).painter().top() + "");
                    ClientClass.getInstance().sent(ClientClass.CommandTest.REMOVE_ENEMY, strs4);
                    this.testEnemies.remove(i);
                    i--;
                    continue;
                }
            }
//            if (this.testEnemies.get(i).isOutOfScreen()) {
//                this.testEnemies.remove(i);
//                i--;
//                continue;
//            }
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