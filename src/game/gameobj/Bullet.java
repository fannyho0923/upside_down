package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Vector;
import game.utils.Global;

import java.awt.*;

public class Bullet extends GameObject{

    private static final int speed = 5;
    private Dir dir;
    private Type type;

    private Image imgDied;
    private Delay diedDelay;
    private int diedCount;

    public enum Dir{
        left(new Vector(-5,0)),//
        right(new Vector(5,0));

        private Vector velocity;
        Dir(Vector v){
            this.velocity = v;
        }
    }
    public enum Type{
        A("/img/gameObj/bullet/a.png"), B("/img/gameObj/bullet/b.png"), C("/img/gameObj/bullet/c.png"),
        D("/img/gameObj/bullet/d.png"), E("/img/gameObj/bullet/e.png"), F("/img/gameObj/bullet/f.png");
        private Image img;
        private Delay delay;
        private int count;
        private static int countMax = 5;
        Type(String path){
            this.img = ImageController.getInstance().tryGet(path);
            this.delay = new Delay(5);
            delay.loop();
            count = 0;
        }
    }
    public Bullet(int i, Type type, Dir dir){

        super(0,192+i* Global.UNIT, 32,32);
        this.dir = dir;
        this.type = type;
        this.offset(-32,-32);
        if(dir == Dir.left){
            setX(Global.WINDOW_WIDTH-32);
        }
        this.collider().offset(32,32);
        this.collider().offsetWidth(16);
        this.collider().offsetHeight(16);

        this.imgDied = ImageController.getInstance().tryGet("/img/gameObj/monster/actorDied.png");
        this.diedDelay = new Delay(3);
        this.diedCount = 0;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img,painter().left(), painter().top(),painter().left()+ 96,painter().top()+96,
                type.count*96,type.count*96,type.count*96+96,type.count*96+96,null);
        g.drawImage(imgDied,collider().left()-48,collider().top()-48,collider().right()+16,collider().bottom()+16,
                (diedCount%4) *128, (diedCount/4) *128,(diedCount%4) *128 +128,(diedCount/4) *128+128,null);
    }

    @Override
    public void update() {
        offset((int)dir.velocity.x(),(int)dir.velocity.y());
        if(type.delay.count()){
            type.count = ++type.count % type.countMax;
        }
        if(diedDelay.count()){
            diedCount = ++diedCount;
            if(diedCount == 8){
                diedDelay.stop();
                diedCount = 0;
            }
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/dead_short.wav");
        if (actor.getState() == Actor.State.ALIVE){
            actor.dead();
            diedDelay.loop();
        }
    }

    public Vector getVelocity(){
        return this.dir.velocity;
    }
}
