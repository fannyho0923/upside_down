package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;
import game.utils.Tour;

import java.awt.*;

public class Monster extends GameObject{

    public enum Type{
        monster_240("/img/gameObj/monster/light1.png",10,5),
        monster_260("/img/gameObj/monster/light2.png",10,5), //get
        monster_280("/img/gameObj/monster/light3.png",10,5),
        monster_300("/img/gameObj/monster/light4.png",10,5),
        monster_320("/img/gameObj/monster/light5.png",10,5),
        monster_340("/img/gameObj/monster/light6.png",10,5),
        monster_380("/img/gameObj/monster/light7.png",10,5);

        private Image img;
        private Delay delay;
        private int count;
        private int countMax;
        Type(String path,int delayTime,int countMax){
            this.img = ImageController.getInstance().tryGet(path);
            this.delay = new Delay(delayTime);
            delay.loop();
            count = 0;
            this.countMax = countMax;
        }
    }

    private Type type;
    private Tour tour;
    public Monster(int top, int left, int width, int height, Type type) {
        super(top, left, width, height);
        this.type = type;
        this.offsetX(-64);
        this.collider().offset(64,64);
        this.collider().offsetWidth(32);
        this.collider().offsetHeight(32);
        switch (type){
            case monster_240:
                tour = new Tour(this, 0 ,-3,60,10);
                break;
            case monster_260:
                tour = new Tour(this, 0 ,3,10,60);
                break;
            case monster_280:
                tour = new Tour(this, 0 ,0,8,0);
                break;
            case monster_300:
                tour = new Tour(this, 0 ,-2,40,0); //
                break;
            case monster_320:
                tour = new Tour(this, 0 ,2,15,25);
                break;
            case monster_340:
                tour = new Tour(this, 3 ,0,150,0);
                break;
            case monster_380:
                tour = new Tour(this, -3 ,0,150,0);
                break;
        }
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().shot("/sound/dead_short.wav");
        if (actor.getState() == Actor.State.ALIVE){
            actor.dead();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img,painter().left(), painter().top(),painter().left()+ 192,painter().top()+192,
                type.count*192,type.count*192,type.count*192+192,type.count*192+192,null);
        g.setColor(Color.RED);
        g.drawRect(collider().left(),collider().top(),collider().width(),collider().width());


    }

    @Override
    public void update() {
        tour.update();
        if(type.delay.count()){
            type.count = ++type.count % type.countMax;
        }
    }
}