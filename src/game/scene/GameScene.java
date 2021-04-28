package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.controller.AudioResourceController;
import game.controller.SceneController;
import game.gameobj.*;
import game.maploader.MapInfo;
import game.maploader.MapLoader;
import game.menu.scene.RankPopupWindowScene;
import game.menu.scene.RankScene;
import game.menu.scene.StopPopupWindowScene;
import game.utils.CommandSolver;
import game.utils.Delay;
import game.utils.Global;
import game.utils.Velocity;
import game.utils.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public abstract class GameScene extends Scene {
    private int level;
    private GameObject background;
    private Actor actor;
    private ArrayList<GameObject> effect;
    private ArrayList<GameObject> backgrounds;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> brokenRoads;
    private ArrayList<GameObject> savePoint;
    private ArrayList<GameObject> passPoint;
    private ArrayList<GameObject> guide;

    private Camera camera;
    private Tracker tracker;
    private boolean actorTrigCamera;
    private int saveNum;

    private static int frameX_count;
    private static int frameY_count;

    private String mapBmpPath;
    private String mapTxtPath;
    private Spikes spikesUp;
    private Spikes spikesDown;
    private StopPopupWindowScene stopPop;
    private Delay delay;
    private BackEffect backEffect;
    private RankPopupWindowScene rankPop;

    private long startTime;
    private long gameTime;
    private RankResult result;
    private Boolean rankShowed;
    private String filePath;
    private boolean isPlayed;
    private String playerName;
    private int currentRankPage;

    private static boolean isStory;

    public GameScene(int level, String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filePath) {
        stopPop = new StopPopupWindowScene(Global.WINDOW_WIDTH / 2 - 325, Global.WINDOW_HEIGHT / 2 - 225,
                650, 450);
        stopPop.setRestartClicked((int x, int y) -> {
            init(level, mapBmpPath, actor, background, cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                    actorTrigCamera, filePath);
            this.stopPop.hide();
        });
        this.filePath = filePath;
        rankPop = new RankPopupWindowScene(Global.WINDOW_WIDTH / 2 - 325, Global.WINDOW_HEIGHT / 2 - 225,
                650, 450);
        rankPop.setCancelable();
        stopPop.setCancelable();

        init(level, mapBmpPath, actor, background, cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera, filePath);
    }

    public void init(int level, String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filePath) {
        this.level = level;

        backgrounds = new ArrayList<>();
        gameObjects = new ArrayList<>();
        brokenRoads = new ArrayList<>();
        savePoint = new ArrayList<>();

        if(level == 1 ){
            guide = new ArrayList<>();
        }

        effect = new ArrayList<>();

        passPoint = new ArrayList<>();
        this.mapBmpPath = mapBmpPath;
        this.mapTxtPath = "/map/genMap.txt";
        mapInit();
        if (level == 1){
            guide.add(new Guide(guide.get(6).collider().left(),guide.get(6).collider().top(),Guide.Type.rubber));
            guide.remove(6);
            guide.get(7).setXY(160,1024);
            guide.get(7).collider().offsetHeight(96);
        }

        delay = new Delay(10);
        delay.loop();
        this.actor = actor;
        frameX_count = savePoint.get(0).collider().left() / cameraWidth;
        frameY_count = savePoint.get(0).collider().top() / cameraHeight;
        actor.setXY(savePoint.get(0).painter().centerX(), savePoint.get(0).painter().centerY());
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

    public static void setIsStory(boolean isStory){
        GameScene.isStory = isStory;
    }
    public static boolean isStory(){
        return isStory;
    }

    public static int getFrameX_count(){
        return frameX_count;
    }
    public static int getFrameY_count(){
        return frameY_count;
    }



    public Actor getActor() {
        return actor;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void sceneBegin() {
        rankShowed = false;
        brokenRoads.forEach(a -> a.setExist(true));
        MapInformation.getInstance().setMapInfo(this.background);
        if (actorTrigCamera) {
            tracker.velocity().stop();
        } else {
            tracker.offsetY(320);
            spikesUp = new Spikes(camera.painter().left(), camera.painter().top(), camera.painter().width(), Spikes.Type.topSpikes);
            spikesDown = new Spikes(camera.painter().left(), camera.painter().bottom() - 32, camera.painter().width(), Spikes.Type.downSpikes);
        }

        backEffect = new BackEffect(10, 200);
        //遊戲開始計時
        startTime = System.nanoTime();
        //System.out.println(startTime);
    }
    @Override
    public void sceneEnd() {
        this.background = null;
        this.actor = null;
        this.delay = null;
        this.effect = null;
        this.background = null;
        this.backgrounds = null;
        this.backEffect = null;
        this.gameObjects = null;
        this.brokenRoads = null;
        this.savePoint = null;
        this.spikesUp = null;
        this.spikesDown = null;
        this.passPoint = null;
        this.guide = null;
        this.camera = null;
        this.tracker = null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return (rankPop.isShow()) ? rankPop.keyListener() :
                new CommandSolver.KeyListener() {
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
                                if (stopPop.isShow()) {
                                    stopPop.hide();
                                    stopPop.sceneEnd();
                                } else {
                                    stopPop.sceneBegin();
                                    stopPop.show();
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
                            playerName = rankPop.getEditText().getEditText();
                            rankPop.setPlayerName(playerName);
                        }
                    }
                };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (stopPop.isShow()) {
                stopPop.mouseListener().mouseTrig(e, state, trigTime);
            }
            if (rankPop.isShow()) {
                rankPop.mouseListener().mouseTrig(e, state, trigTime);
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        camera.start(g);
        backgrounds.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });
        backEffect.paint(g);

        gameObjects.forEach(a -> {
            if (camera.isCollision(a)) {
                a.paint(g);
            }
        });

        effect.forEach(a -> a.paint(g));
        for (int i = 1; i < savePoint.size(); i++) {
            if (camera.isCollision(savePoint.get(i))) {
                savePoint.get(i).savePointPaint(g, i == saveNum);
            }
        }

        brokenRoads.forEach(a -> {
            if (camera.isCollision(a) && a.isExist()) {
                a.paint(g);
            }
        });

        if (passPoint.size() > 0) {
            passPoint.get(0).paint(g);
        }

        if (camera.isCollision(this.actor)) {
            this.actor.paint(g);
        }

        if (!actorTrigCamera) {
            spikesUp.paint(g);
            spikesDown.paint(g);
        }

        if(level == 1){
            guide.forEach(a-> {
                if (a.isCollision(actor)) {
                    a.paint(g);
                }
            });
        }

        camera.paint(g);
        camera.end(g);

        midPaint(g);

        if (stopPop.isShow()) {
            stopPop.paint(g);
        }
        if (rankPop.isShow()) {
            rankPop.paint(g);
        }
    }

    public void midPaint(Graphics g) {
    }

    @Override
    public void update() {
        if ((!stopPop.isShow()) && (!rankPop.isShow()) && (!rankShowed) && (!isPlayed)) {
            actor.update();

            if (actor.velocity().x() != 0 && delay.count()) {
                if (!actor.velocity().isReverse()) {
                    if (actor.velocity().x() > 0) {   // 正面向右
                        effect.add(new WalkAnimation(actor.painter().centerX() - actor.painter().width(), actor.painter().centerY()));
                    } else {                          // 正面向左
                        effect.add(new WalkAnimation(actor.painter().centerX(), actor.painter().centerY()));
                    }
                } else {
                    if (actor.velocity().x() > 0) {   // 反面向右
                        effect.add(new WalkAnimation(actor.painter().centerX() - actor.painter().width(), actor.painter().centerY() - actor.painter().height()));
                    } else {                          // 反面向左
                        effect.add(new WalkAnimation(actor.painter().centerX(), actor.painter().centerY() - actor.painter().height()));
                    }
                }
            }
            backEffect.update();
            if (!backEffect.isExist()) {
                backEffect.setXY(camera.painter().left() + Global.random(0, Global.WINDOW_WIDTH), camera.painter().top() + Global.random(0, Global.WINDOW_HEIGHT));
                backEffect.setExist(true);
            }

            savePoint.forEach(a -> a.update());
            if (actor.getState() == Actor.State.DEAD) {
                brokenRoads.forEach(a -> a.setExist(true));
            }

            for (int i = 0; i < brokenRoads.size(); i++) {
                GameObject obj = brokenRoads.get(i);
                if (actor.isCollision(obj) && obj.isExist()) {
                    obj.collisionEffect(actor);
                }
                obj.update();
            }

            for (int i = 0; i < savePoint.size(); i++) {
                GameObject obj = savePoint.get(i);
                if (actor.isCollision(obj) && actor.getState() == Actor.State.ALIVE) {
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
            for (int i = 0; i < effect.size(); i++) {
                effect.get(i).update();
                if (!effect.get(i).isExist()) {
                    effect.remove(i);
                    i--;
                }
            }

            if(!filePath.equals("countdown.txt")&&!filePath.equals("end.txt")){
                passPoint.get(0).update();
            }

            if (passPoint.size() > 0) {
                if (actor.isCollision(passPoint.get(0)) && actor.getState() == Actor.State.ALIVE) {
                    if(!isStory){
                        passPoint.get(0).collisionEffect(actor); //for音效
                        rankShowed = true;
                    }else {
                        switch (level){
                            case 1:
                                SceneController.getInstance().change(new SpeedRun(1));
                                break;
                            case 2:
                                SceneController.getInstance().change(new Parkour(2));
                                break;
                            case 3:
                                SceneController.getInstance().change(new CountDown(3));
                                break;
                        }
                    }
                }
            }
            camera.update();
            if (actorTrigCamera) {
                if (actor.painter().centerX() < camera.painter().left()) {       // 左
                    frameX_count--;
                    brokenRoads.forEach(a -> a.setExist(true));
                }
                if (actor.painter().centerY() < camera.painter().top()) {        // 上
                    frameY_count--;
                    brokenRoads.forEach(a -> a.setExist(true));
                }
                if (actor.painter().centerX() > camera.painter().right()) {     // 右
                    frameX_count++;
                    brokenRoads.forEach(a -> a.setExist(true));
                }
                if (actor.painter().centerY() > camera.painter().bottom()) {   // 下
                    frameY_count++;
                    brokenRoads.forEach(a -> a.setExist(true));
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
                    tracker.setY(actor.painter().bottom()); //
                    if (passPoint.size() > 0) {
                        if (actor.isCollision(passPoint.get(0))) {
                            passPoint.get(0).collisionEffect(actor); //for音效
                            rankShowed = true;
                        }
                    }
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
        }
        if (rankShowed) {
            Global.isGameOver = true;
            gameOver();
        }
        if (stopPop.isShow()) {
            stopPop.update();
        }
    }

    public Camera getCamera() {
        return this.camera;
    }


    public void gameOver() {
        if (Global.isGameOver) {
            rankPop.sceneBegin();
            rankPop.update();
            gameTime = System.nanoTime() - startTime;
            gameTime = TimeUnit.NANOSECONDS.toMillis(gameTime);
            int gtInt = (int) gameTime;
            result = new RankResult(playerName, gtInt);
            if (filePath.equals("basic.txt")) {
                this.currentRankPage = 0;
            }
            if (filePath.equals("speedrun.txt")) {
                this.currentRankPage = 1;
            }
            if (filePath.equals("parkour.txt")) {
                this.currentRankPage = 2;
            }
            if (filePath.equals("countdown.txt")) {
                this.currentRankPage = 3;
            }
            //確認可否刪除currentRankPage
            rankPop.setPlayer(result, filePath, currentRankPage);
            rankPop.show();
            rankShowed = false;
            Global.isGameOver = false;
            isPlayed = true;
        }
    }

    public void mapInit() {
        try {
            final MapLoader mapLoader = new MapLoader(mapBmpPath, mapTxtPath);
            final ArrayList<MapInfo> mapInfoArr = mapLoader.combineInfo();

            // 易碎地, 要分開存!!
            this.brokenRoads.addAll(mapLoader.createObjectArray("broken1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.A);
                    return tmp;
                }
                return null;
            }));

            this.brokenRoads.addAll(mapLoader.createObjectArray("broken2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.B);
                    return tmp;
                }
                return null;
            }));

            this.brokenRoads.addAll(mapLoader.createObjectArray("crate27", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.A);
                    return tmp;
                }
                return null;
            }));

            this.brokenRoads.addAll(mapLoader.createObjectArray("crate28", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.C);
                    return tmp;
                }
                return null;
            }));

            this.brokenRoads.addAll(mapLoader.createObjectArray("crate29", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.D);
                    return tmp;
                }
                return null;
            }));

            this.brokenRoads.addAll(mapLoader.createObjectArray("crate30", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new BrokenRoad(mapInfo.getX() * size, mapInfo.getY() * size, BrokenRoad.Type.E);
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
            //background
            this.backgrounds.addAll(mapLoader.createObjectArray("city", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.End);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("complete", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Complete);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.A1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.B1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back3-2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C2);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back3-3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C3);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back3-4", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.C4);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back4-1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back4-2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D2);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back4-3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D3);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back4-4", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.D4);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back5", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.E1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back6", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.F1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back11", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.G1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back12", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.H1);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back13", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny13);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back14", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny14);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back15", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny15);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back16", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny16);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back17", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny17);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back18", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny18);
                    return tmp;
                }
                return null;
            }));

            this.backgrounds.addAll(mapLoader.createObjectArray("back19", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new CameraBackground(mapInfo.getX() * size, mapInfo.getY() * size, CameraBackground.Type.Fanny19);
                    return tmp;
                }
                return null;
            }));

            if(level == 1){
                this.guide.addAll(mapLoader.createObjectArray("dir", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.dirKey);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("Space", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.space);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("save", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.save);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("ESC", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.ESC);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("mons", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.monster);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("conv", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.conveyor);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("brokenG", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.brokenRoad);
                        return tmp;
                    }
                    return null;
                }));

                this.guide.addAll(mapLoader.createObjectArray("passG", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new Guide(mapInfo.getX() * size, mapInfo.getY() * size, Guide.Type.pass);
                        return tmp;
                    }
                    return null;
                }));
            }

            if (level == 2) {
                this.backgrounds.add(
                        new CameraBackground(0, 7680, CameraBackground.Type.Fanny14));
                this.backgrounds.add(
                        new CameraBackground(0, 5120, CameraBackground.Type.Fanny16));
                this.backgrounds.add(
                        new CameraBackground(0, 5760, CameraBackground.Type.Fanny19));

                this.gameObjects.addAll(mapLoader.createObjectArray("cmoney_I", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new CMoney(mapInfo.getX() * size, mapInfo.getY() * size, CMoney.Color.pink);
                        return tmp;
                    }
                    return null;
                }));

                this.gameObjects.addAll(mapLoader.createObjectArray("Love", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new CMoney(mapInfo.getX() * size, mapInfo.getY() * size, CMoney.Color.PINK);
                        return tmp;
                    }
                    return null;
                }));

                this.gameObjects.addAll(mapLoader.createObjectArray("Cmoney_C", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new CMoney(mapInfo.getX() * size, mapInfo.getY() * size, CMoney.Color.YELLOW);
                        return tmp;
                    }
                    return null;
                }));

                this.gameObjects.addAll(mapLoader.createObjectArray("Money", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                    final GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new CMoney(mapInfo.getX() * size, mapInfo.getY() * size, CMoney.Color.yellow);
                        return tmp;
                    }
                    return null;
                }));
            }

            if (level == 3) {
                this.backgrounds.add(
                        new CameraBackground(2880, 0, CameraBackground.Type.D3));
            }
            if (level == 5) {
                this.backgrounds.add(
                        new CameraBackground(2880, 0, CameraBackground.Type.End));
            }

            Tile.Type type;
            if (level == 1) {
                type = Tile.Type.GRAY;
            } else if (level == 2) {
                type = Tile.Type.BROWN;
            } else if (level == 3) {
                type = Tile.Type.COLOR;
            } else {
                type = Tile.Type.BROWN;
            }

            //tiles
            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0195", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
//                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0195);
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0196", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
//                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0196);
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0197", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
//                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0197);
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0198", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
//                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0198);
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0215", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
//                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, Tile.Type.tile_0215);
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0235", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0236", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0237", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0238", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0255", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0256", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_0257", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("tile_999", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Tile(mapInfo.getX() * size, mapInfo.getY() * size, type);
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

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_280", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_280);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_300", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_300);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_320", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_320);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_340", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_340);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("monster_380", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Monster(mapInfo.getX() * size, mapInfo.getY() * size,
                            mapInfo.getSizeX() * size, mapInfo.getSizeY() * size, Monster.Type.monster_380);
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
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-L2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-L3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-L3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownL);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-top-R3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.TopR);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R1", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R2", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR);
                    return tmp;
                }
                return null;
            }));

            this.gameObjects.addAll(mapLoader.createObjectArray("c-down-R3", Global.UNIT, mapInfoArr, (gameObject, name, mapInfo, size) -> {
                final GameObject tmp;
                if (gameObject.equals(name)) {
                    tmp = new Conveyor(mapInfo.getX() * size, mapInfo.getY() * size, Conveyor.Type.DownR);
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