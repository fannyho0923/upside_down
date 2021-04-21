package game.scene;

import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.Bullet;
import game.gameobj.GameObject;
import game.utils.Global;
import game.utils.NumberFig;
import game.utils.Delay;
import game.utils.Tour;

import java.awt.*;
import java.util.ArrayList;

public class CountDown extends GameScene{
    private ArrayList<Bullet> bullets;
    private NumberFig[] numFigs;
    private Delay secDelay;
    private Delay shootDelay;
    private int timeCount;
    private int shootPosition;
    private static int timeMax = 30;
    private int rebornTime;
    private int rebornShoot;

    public CountDown(int num) {
        super("/map/genMap.bmp", new Actor(0, 0, num), new Background(960, 640),
                960, 640, 0, 0, true);
        // 不要寫東西!!
    }

    @Override
    public void init(String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera) {
        super.init(mapBmpPath, actor, background,
                cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera);
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
    }

    @Override
    public void midPaint(Graphics g) {
        bullets.forEach(bullet -> bullet.paint(g));
        numFigs[0].paint(g);
        numFigs[1].paint(g);
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
                //bullets.add(new Bullet(shootPosition, Bullet.Type.A, Bullet.Dir.left));
                bullets.add(new Bullet(7-shootPosition, Bullet.Type.E, Bullet.Dir.left));
                shootPosition++;
          }else {
                shootPosition--;
                //bullets.add(new Bullet(shootPosition, Bullet.Type.D, Bullet.Dir.right));
                bullets.add(new Bullet(7-shootPosition, Bullet.Type.F, Bullet.Dir.right));
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
            if(timeCount == -1){
                // level complete
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
