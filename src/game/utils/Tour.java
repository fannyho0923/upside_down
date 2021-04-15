package game.utils;

import game.gameobj.Actor;
import game.gameobj.GameObject;

public class Tour {
    private GameObject obj;
    private Actor actor;
    private int dx;
    private int dy;
    private int step;
    private int count;

    public Tour(GameObject obj, int dx, int dy, int step) {
        this.obj = obj;
        this.dx = dx;
        this.dy = dy;
        this.step = step;
        this.count = 0;
    }

    public void update(){
        count %= (step * 4);
        if(count < step | count >= step * 3){
            obj.offset(dx, dy);
            count++;
        }else{
            obj.offset(-dx,-dy);
            count++;
        }
    }
}
