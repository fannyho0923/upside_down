package game.scene;

import game.controller.ImageController;
import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.Bullet;
import game.gameobj.GameObject;
import game.utils.Global;
import game.utils.NumberFig;
import game.utils.Delay;

import java.awt.*;
import java.util.ArrayList;

public class CountDown extends GameScene{
    private ArrayList<Bullet> bullets;
    private NumberFig[] numFigs;
    private Delay secDelay;
    private Delay shootDelay;
    private int timeCount;
    private int shootPosition;
    private static int timeMax = 10;
    private int rebornTime;
    private int rebornShoot;
    private Image[] passBlood;
    private Image pass;
    private Delay passDelay;
    private int passCount;

    public CountDown(int num) {
        super("/map/genMap.bmp", new Actor(0, 0, num), new Background(960, 640),
                960, 640, 0, 0, true, "countdown.txt");
        // 不要寫東西!!
    }

    @Override
    public void init(String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filepath) {
        super.init(mapBmpPath, actor, background,
                cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera, filepath);
        secDelay = new Delay(Global.UPDATE_TIMES_PER_SEC);
        secDelay.loop();
        timeCount = timeMax;
        numFigs = new NumberFig[2];
        numFigs[0] = new NumberFig(370,0,timeCount/10);
        numFigs[1] = new NumberFig(480,0,timeCount%10);

        bullets = new ArrayList<>();
        shootDelay = new Delay(60);
        shootDelay.loop();
        shootPosition = 0;

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
    public void midPaint(Graphics g) {
        bullets.forEach(bullet -> bullet.paint(g));
        numFigs[0].paint(g);
        numFigs[1].paint(g);
        if(timeCount == -1){
            // level complete

            if(passDelay.count()){
                passCount++;
                if(passCount == 3){
                    passDelay.pause();
                }
            }
            g.drawImage(passBlood[passCount],160,0,null);
            g.drawImage(pass,160,80,null );
            // 加音效
        }
    }

    @Override
    public void update() {
        super.update();
        if(shootDelay.count()){
            if(timeCount > timeMax - 8){
                if (shootPosition %2 ==0){
                    bullets.add(new Bullet(shootPosition, Bullet.Type.A, Bullet.Dir.left));
                }else {
                    bullets.add(new Bullet(shootPosition, Bullet.Type.B, Bullet.Dir.left));
                }
                shootPosition++;
            }
            else if(timeCount > timeMax - 16){
                shootPosition--;
                if (shootPosition %2 ==0){
                    bullets.add(new Bullet(shootPosition, Bullet.Type.C, Bullet.Dir.right));
                }else {
                    bullets.add(new Bullet(shootPosition, Bullet.Type.D, Bullet.Dir.right));
                }
            }else if(timeCount > timeMax - 24){
                if (shootPosition %2 ==0) {
                    bullets.add(new Bullet(shootPosition, Bullet.Type.A, Bullet.Dir.left));
                }else {
                    bullets.add(new Bullet(7-shootPosition, Bullet.Type.E, Bullet.Dir.left));
                }

                shootPosition++;
          }else if(timeCount > 0){
                shootPosition--;
                if (shootPosition %2 ==0){
                    bullets.add(new Bullet(shootPosition, Bullet.Type.D, Bullet.Dir.right));
                }else {
                    bullets.add(new Bullet(7-shootPosition, Bullet.Type.F, Bullet.Dir.right));
                }
            }
        }
        for(int i = 0; i < bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.isCollision(getActor())){
                //bullet.collisionEffect(getActor());
            }
            if(!bullet.isCollision(getCamera())){
                bullets.remove(i);
                i--;
            }
        }
        if(secDelay.count()){
            timeCount--;
            numFigs[0].setNum(timeCount/10);
            numFigs[1].setNum(timeCount%10);

            if(timeCount %5 == 0){
                rebornTime = timeCount;
                rebornShoot = shootPosition;
            }
            if (timeCount == -1){
                passDelay.loop();
                secDelay.pause();
            }
        }
        if (getActor().getState() == Actor.State.DEAD){
            shootDelay.pause();
            timeCount = rebornTime;
            shootPosition = rebornShoot;
        }
        if (getActor().getState() == Actor.State.REBORN){
            shootDelay.loop();
        }
    }
}
