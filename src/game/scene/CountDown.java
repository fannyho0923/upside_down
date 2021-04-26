package game.scene;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.gameobj.*;
import game.utils.Global;
import game.utils.NumberFig;
import game.utils.Delay;

import java.awt.*;
import java.util.ArrayList;

public class CountDown extends GameScene {
    private ArrayList<Bullet> bullets;
    private NumberFig[] numFigs;
    private Delay secDelay;
    private Delay shootDelay;
    private int timeCount;
    private int shootPosition;
    private static int timeMax = 30;
    private int rebornTime;
    private int rebornShoot;
    private Image[] passBlood;
    private Image pass;
    private Delay passDelay;
    private int passCount;

    private Delay diedDelay;


    public CountDown(int num) {
        super(4, "/map/countDown.bmp", new Actor(0, 0, num), new Background(960, 640),
                960, 640, 0, 0, true, "countdown.txt");
        // 不要寫東西!!
    }

    @Override
    public void init(int level, String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filepath) {
        super.init(level, mapBmpPath, actor, background,
                cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera, filepath);
        secDelay = new Delay(Global.UPDATE_TIMES_PER_SEC);
        secDelay.loop();
        timeCount = timeMax;
        numFigs = new NumberFig[2];
        numFigs[0] = new NumberFig(370, 0, timeCount / 10);
        numFigs[1] = new NumberFig(480, 0, timeCount % 10);

        bullets = new ArrayList<>();
        shootDelay = new Delay(60);
        shootDelay.loop();
        shootPosition = 0;

        diedDelay = new Delay(50);

        rebornTime = timeMax;
        rebornShoot = shootPosition;


        pass = ImageController.getInstance().tryGet("/img/gameObj/pass/pass.png");
        passBlood = new Image[4];
        passBlood[0] = ImageController.getInstance().tryGet("/img/gameObj/pass/blood1.png");
        passBlood[1] = ImageController.getInstance().tryGet("/img/gameObj/pass/blood2.png");
        passBlood[2] = ImageController.getInstance().tryGet("/img/gameObj/pass/blood3.png");
        passBlood[3] = ImageController.getInstance().tryGet("/img/gameObj/pass/blood4.png");
        passDelay = new Delay(5);
        passCount = 0;
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/Battle-Conflict-volumeReduce.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/Battle-Conflict-volumeReduce.wav");
    }

    @Override
    public void midPaint(Graphics g) {
        bullets.forEach(bullet -> bullet.paint(g));
        numFigs[0].paint(g);
        numFigs[1].paint(g);

//        if(timeCount == -1){
//            // level complete
//
//            if(passDelay.count()){
//                passCount++;
//                if(passCount == 3){
//                    passDelay.pause();
//                }
//            }
//            g.drawImage(passBlood[passCount],160,0,null);
//            g.drawImage(pass,160,80,null );
//            // 加音效
//        }
        //test.paint(g);
    }

    @Override
    public void update() {
        if (timeCount >= 0) {
            super.update();

            if (shootDelay.count()) {
                if (timeCount > timeMax - 8) {
                    if (shootPosition % 2 == 0) {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.A, Bullet.Dir.left));
                    } else {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.B, Bullet.Dir.left));
                    }
                    shootPosition++;
                } else if (timeCount > timeMax - 16) {
                    shootPosition--;
                    if (shootPosition % 2 == 0) {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.C, Bullet.Dir.right));
                    } else {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.D, Bullet.Dir.right));
                    }
                } else if (timeCount > timeMax - 24) {
                    if (shootPosition % 2 == 0) {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.A, Bullet.Dir.left));
                    } else {
                        bullets.add(new Bullet(7 - shootPosition, Bullet.Type.E, Bullet.Dir.left));
                    }
                    shootPosition++;
                } else if (timeCount > 0) {
                    shootPosition--;
                    if (shootPosition % 2 == 0) {
                        bullets.add(new Bullet(shootPosition, Bullet.Type.D, Bullet.Dir.right));
                    } else {
                        bullets.add(new Bullet(7 - shootPosition, Bullet.Type.F, Bullet.Dir.right));
                    }
                }
            }

            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                bullet.update();
                if (bullet.isCollision(getActor())) {
                    bullet.collisionEffect(getActor());
                    diedDelay.play();
                }
                if (!bullet.isCollision(getCamera())) {
                    bullets.remove(i);
                    i--;
                }
            }

            if (secDelay.count()) {
                timeCount--;
                numFigs[0].setNum(timeCount / 10);
                numFigs[1].setNum(timeCount % 10);

                if (timeCount % 5 == 0) {
                    System.out.println(timeCount);
                    rebornTime = timeCount;
                    rebornShoot = shootPosition;
                }
//                if (timeCount == -1) { // win
////                passDelay.loop();
////                secDelay.pause();
            }
            if (timeCount <= -1) { // win
                Global.fanny = true;
                super.fanny(Global.fanny);
                Global.fanny = false;

            }

            if (getActor().getState() == Actor.State.DEAD) {
                shootDelay.stop();
                if (diedDelay.count()) {
                    bullets = new ArrayList<>();
                    shootPosition = rebornShoot;
                    timeCount = rebornTime;
                    shootDelay.loop();
                }
            }
        }
        if (getActor().getState() == Actor.State.REBORN) {

        }
    }
}
