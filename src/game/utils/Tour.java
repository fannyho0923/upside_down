package game.utils;

import game.gameobj.Actor;
import game.gameobj.GameObject;

public class Tour {
    private GameObject obj;
    private int dx;
    private int dy;
    private int step1;
    private int step2;
    private int count;

    public Tour(GameObject obj, int dx, int dy, int step) {
        this.obj = obj;
        this.dx = dx;
        this.dy = dy;
        this.step1 = step;
        this.step2 = step;
        this.count = 0;
    }

    public Tour(GameObject obj, int dx, int dy, int step1, int step2) {
        this.obj = obj;
        this.dx = dx;
        this.dy = dy;
        this.step1 = step1;
        this.step2 = step2;
        this.count = 0;
    }

    public void update(){
        count %= (2*(step1 + step2));
        if(count < step1 | count >= step1*2 + step2){
            obj.offset(dx, dy);
            count++;
        }else{
            obj.offset(-dx,-dy);
            count++;
        }
    }
}
