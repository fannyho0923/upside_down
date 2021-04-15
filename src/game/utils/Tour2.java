package game.utils;

import game.gameobj.MovePlatform;


public class Tour2 {
    private MovePlatform obj;
    private Vector vector;
    private int step1;
    private int step2;
    private int count;

    public Tour2(MovePlatform obj, Vector vector, int step1, int step2) {
        this.obj = obj;
        this.vector = vector;
        this.step1 = step1;
        this.step2 = step2;
        this.count = 0;
    }

    public void update(){
        count %= (2*(step1 + step2));
        if(count < step1 | count >= 2 * step1 + step2){
            obj.setVelocity(vector);
            obj.secondCollider().offset((int)vector.x(),(int)vector.y());
            count++;
        }else{
            obj.setVelocity(Vector.reverse(vector));
            obj.secondCollider().offset(-(int)vector.x(),-(int)vector.y());
            count++;
        }
    }
}
