package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.controller.AudioResourceController;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.menu.scene.RankPopupWindowScene;
import game.menu.scene.RankScene;
import game.menu.scene.StopPopupWindowScene;
import game.utils.CommandSolver;
import game.utils.Global;
import game.utils.Velocity;
import game.menu.scene.StopPopupWindowScene;
import game.utils.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.TimeUnit;


public abstract class GameScene extends Scene {


    private GameObject background;
    private Actor actor;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> orinBrokenRoads; // 分開加入, 鏡頭改變時要重新加入
    private ArrayList<GameObject> brokenRoads;
    private ArrayList<GameObject> savePoint;
    private ArrayList<GameObject> movePlatform;
    private ArrayList<GameObject> passPoint;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;
    private int saveNum;

    private int frameX_count;
    private int frameY_count;

    private String mapBmpPath;
    private String mapTxtPath;
    private Spikes spikesUp;
    private Spikes spikesDown;
    private StopPopupWindowScene testPop;
    private RankPopupWindowScene rankPop;

    private long startTime;
    private long gameTime;
    private Ranking ranking;
    private ArrayList<RankResult> rankResults;
    private String playerName;

    public GameScene(String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filePath) {
        testPop = new StopPopupWindowScene(Global.WINDOW_WIDTH / 2 - 325, Global.WINDOW_HEIGHT / 2 - 225,
                650, 450);
        testPop.setRestartClicked((int x, int y) -> {
            init(mapBmpPath, actor, background,
                    cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                    actorTrigCamera, filePath);
            this.testPop.hide();
        });
        rankPop = new RankPopupWindowScene(Global.WINDOW_WIDTH / 2 - 325, Global.WINDOW_HEIGHT / 2 - 225,
                650, 450);
        rankPop.setCancelable();
        testPop.setCancelable();
this.playerName="";
        init(mapBmpPath, actor, background,
                cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera, filePath);
    }



    public void init(String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filePath) {
        gameObjects = new ArrayList<>();
        orinBrokenRoads = new ArrayList<>();
        savePoint = new ArrayList<>();
        movePlatform = new ArrayList<>();
        passPoint = new ArrayList<>();
        rankResults = new ArrayList<>();

        try {
            ranking = new Ranking(filePath);
        }catch (IOException e){
            System.out.println(e);
        }

        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = "/map/genMap.txt";
        mapInit();

        this.actor = actor;
        frameX_count = savePoint.get(0).collider().left() / cameraWidth;
        frameY_count = savePoint.get(0).collider().top() / cameraHeight;
        actor.setXY(savePoint.get(0).painter().left(), savePoint.get(0).painter().top());
        actor.setReborn(actor.painter().left(), actor.painter().top(), false);
        saveNum = 0;

        this.background = background;
        int cameraStartX = cameraWidth * frameX_count;
        int cameraStartY = cameraHeight * frameY_count;

        this.tracker = new Tracker(cameraStartX + (cameraWidth - Global.UNIT) / 2,
                cameraStartY + (cameraHeight - Global.UNIT) / 2, new Velocity(cameraVelocityX, cameraVelocityY, false));
        this.actorTrigCamera = actorTrigCamera;
        camera = new Camera.Builder(cameraWidth, cameraHeight)
                .setChaseObj(tracker)
                .gen();
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().loop("/sound/Battle-Dawn-crop-reduce.wav", 50);
        brokenRoads = (ArrayList) orinBrokenRoads.clone();
        MapInformation.getInstance().setMapInfo(this.background);
        if (actorTrigCamera) {
            tracker.velocity().stop();
        } else {
            tracker.offsetY(320);
            spikesUp = new Spikes(camera.painter().left(), camera.painter().top(), camera.painter().width(), Spikes.Type.topSpikes);
            spikesDown = new Spikes(camera.painter().left(), camera.painter().bottom() - 32, camera.painter().width(), Spikes.Type.downSpikes);
        }

        //遊戲開始計時
        startTime = System.nanoTime();
//        System.out.println(startTime);

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
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().setX(Actor.WALK_SPEED);
                        break;
                    case Global.VK_SPACE:
                        if (actor.canReverse()) {
                            actor.velocity().gravityReverse();
                            actor.setCanReverse(false);
                        }
                        break;
                    case Global.VK_ESCAPE:
                        if (testPop.isShow()) {
                            testPop.hide();
                            testPop.sceneEnd();
                        } else {
                            testPop.sceneBegin();
                            testPop.show();
                        }
                        break;
                    case Global.VK_R:
                        if (rankPop.isShow()) {
                            rankPop.hide();
                            rankPop.sceneEnd();
                        } else {
                            rankPop.sceneBegin();
                            rankPop.show();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode) {
                    case Global.VK_LEFT:
                        actor.velocity().stopX();
                        break;
                    case Global.VK_RIGHT:
                        actor.velocity().stopX();
                        break;
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                if (rankPop.isShow()) {
                    rankPop.getEditText().keyTyped(c);
                    playerName=rankPop.getEditText().getEditText();
                    System.out.println(playerName);
                }

            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (testPop.isShow()) {
                testPop.mouseListener().mouseTrig(e, state, trigTime);
            }
            if (rankPop.isShow()) {
                rankPop.mouseListener().mouseTrig(e, state, trigTime);
            }

        };
    }

    @Override
    public void paint(Graphics g) {
        camera.start(g);
        gameObjects.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        for (int i = 1; i < savePoint.size(); i++) {
            if (camera.isCollision(savePoint.get(i))) {
                savePoint.get(i).savePointPaint(g, i == saveNum);
            }
        }
        brokenRoads.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        if (passPoint.size()>0) {
            passPoint.get(0).paint(g);
        }
        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }

        if (!actorTrigCamera) {
            spikesUp.paint(g);
            spikesDown.paint(g);
        }

        if (actor.getState() == Actor.State.DEAD) {

        }
        camera.paint(g);
        camera.end(g);
        midPaint(g);
        if (testPop.isShow()) {
            testPop.paint(g);
        }
        if (rankPop.isShow()) {
            rankPop.paint(g);
        }
    }

    public void midPaint(Graphics g) {
    }

    @Override
    public void update() {
        if ((!testPop.isShow())&&(!rankPop.isShow())) {
            actor.update();
            for (int i = 0; i < brokenRoads.size(); i++) {
                GameObject obj = brokenRoads.get(i);
                if (actor.isCollision(obj)) {
                    obj.collisionEffect(actor);
                }
                obj.update();
                if (!obj.isExist()) {
                    obj.setExist(true);
                    brokenRoads.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < savePoint.size(); i++) {
                GameObject obj = savePoint.get(i);
                if (actor.isCollision(obj)) {
                    obj.collisionEffect(actor);
                    saveNum = i;
                }
            }
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject obj = gameObjects.get(i);
                if (actor.isCollision(obj)) {
                    obj.collisionEffect(actor);
                }
                obj.update();
            }

            if (passPoint.size()>0) {
                if (actor.isCollision(passPoint.get(0))) {
                    passPoint.get(0).collisionEffect(actor); //for音效
                    pass();
                }
            }


            camera.update();
            if (actorTrigCamera) {
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
                tracker.setX((camera.painter().width() - Global.UNIT) / 2 +
                        (camera.painter().width() * frameX_count));
                tracker.setY((camera.painter().height() - Global.UNIT) / 2 +
                        camera.painter().height() * frameY_count);
            } else {
                tracker.update();
                if (actor.collider().right() <= camera.collider().left()) {//walk left
                    actor.setXY(camera.collider().right() - 1, actor.painter().top());
                    return;
                }
                if (actor.collider().left() >= camera.collider().right()) {
                    actor.setXY(camera.collider().left() - actor.painter().width() + 1, actor.painter().top());
                    return;
                }
                if (actor.getState() == Actor.State.REBORN) {
                    tracker.setY(actor.painter().bottom() - 200);
                }
                if (spikesUp.isCollision(actor)) {
                    spikesUp.collisionEffect(actor);
                }
                if (spikesDown.isCollision(actor)) {
                    spikesDown.collisionEffect(actor);
                }
                spikesUp.setXY(camera.painter().left(), camera.painter().top() - 5); // 為什麼會不貼邊??
                spikesDown.setXY(camera.painter().left(), camera.painter().bottom() - 32);
            }
        } if (rankPop.isShow()) {
            rankPop.update();
        } if (testPop.isShow()) {
            testPop.update();
        }
    }



    public Camera getCamera() {
        return this.camera;
    }

    public void pass(){
        gameTime = System.nanoTime()-startTime;
//        System.out.println(gameTime);
        gameTime = TimeUnit.NANOSECONDS.toMillis(gameTime);
        int gtInt = (int)gameTime;


        //到時畫面印出排行榜需要轉換的格式
//        gt = new SimpleDateFormat("mm:ss:SSS", Locale.TAIWAN).format(new Date(gameTime));

        RankResult newResult = new RankResult("Player", gtInt);

        //讀目前的排行
        ArrayList<String> arr = ranking.readL();

        if (arr.size()>0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
                System.out.println("IN");
            }
        }

        System.out.println("new: "+gtInt);

        System.out.println("Unsorted");
        for (int i = 0; i < rankResults.size(); i++) {
            System.out.println(rankResults.get(i).getName());
            System.out.println(rankResults.get(i).getTime());
            System.out.println("-----------");
        }

        //如果目前榜上資料超過9筆，要進行比對，有進榜單資格的話，就add，排序後取前10輸出
        //不超過9筆就直接加入榜單後，排序輸出

        if (rankResults.size()>2) {
                if (rankResults.get(rankResults.size()-1).compareTo(newResult)){
                    //有資格進榜單，讓使用者輸入名字
//                    newResult.setName("");
                    rankPop.sceneBegin();
                    rankPop.show();
                    newResult.setName(playerName);
                    rankResults.add(newResult);
                }

        }else {
            //榜單未滿，自動加入
            rankPop.sceneBegin();
            rankPop.show();
            newResult.setName(playerName);
            rankResults.add(newResult);
        }

        //排序
        Collections.sort(rankResults, new RankSort());

        System.out.println("\nSort");
        for (int i = 0; i < rankResults.size(); i++) {
            System.out.println(rankResults.get(i).getName());
            System.out.println(rankResults.get(i).getTime());
            System.out.println("-----------");
        }

        //取前10名轉回一串字串輸出
        String output = "";
        if (rankResults.size()>3){
            rankResults.remove(3);
        }
        for (int i = 0; i < rankResults.size(); i++) {
            output += rankResults.get(i).getName() + "," + rankResults.get(i).getTime() + ",";
        }
//清資料用
//        rankResults.clear();
        ranking.writeOut(output);
    }

    public void mapInit() {
        try {
            final MapLoader mapLoader = new MapLoader(mapBmpPath, mapTxtPath);
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

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
            this.savePoint.addAll(mapLoader.createObjectArray("born", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new SavePoint(mapInfo.getX() * size, mapInfo.getY() * size);
                    return tmp;
                }
                return null;
            }));

            this.savePoint.addAll(mapLoader.createObjectArray("savePoint", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new SavePoint(mapInfo.getX() * size, mapInfo.getY() * size);
                    return tmp;
                }
                return null;
            }));


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

            // monster

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_240", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_240);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_260", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_260);
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

            //通關點
            this.passPoint.addAll(mapLoader.createObjectArray("passPoint", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Pass(mapInfo.getX() * size, mapInfo.getY() * size);
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