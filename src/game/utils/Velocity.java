package game.utils;

import game.utils.Global;

public class Velocity {
    private int x;
    private int y;
    private float dx;
    private float dy;
    private boolean gravityReverse;

    private final int MAX_X = 30;
    private final int MAX_Y = 30;


    public Velocity(int x, int y, int dx, int dy, boolean gravityReverse){
        setX(x);
        setY(y);
        this.dx = dx;
        this.dy = dy;
        this.gravityReverse = gravityReverse;
    }

    public void gravityReverse(){
        gravityReverse = !gravityReverse;
    }

    public boolean isReverse(){
        return gravityReverse;
    }


    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public float dx() {
        return dx;
    }

    public float dy() {
        return dy;
    }

    public void setX(int x) {
        this.x = x;
        if(this.x > MAX_X){
            this.x = MAX_X;
        }else if(this.x < -MAX_X){
            this.x = -MAX_X;
        }
    }

    public void setY(int y) {
        this.y = y;
        if(this.y > MAX_Y){
            this.y = MAX_Y;
        }else if(this.y < -MAX_Y){
            this.y = - MAX_Y;
        }
    }

    public void offsetX(float x){
        this.x += x;
        if(this.x > MAX_X){
            this.x = MAX_X;
        }else if(this.x < -MAX_X){
            this.x = -MAX_X;
        }
    }

    public void offsetY(float y){
        this.y += y;
        if(this.y > MAX_Y){
            this.y = MAX_Y;
        }else if(this.y < -MAX_Y){
            this.y = - MAX_Y;
        }
    }

    public void offsetDX(float dx) {
        this.dx += dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public void stop(){
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
    }

    public void stopX(){
        x = 0;
        dx = 0;
    }

    public void stopY(){
        y = 0;
        dy = 0;
    }


    public void update(){
        if (gravityReverse){
            dy -= Global.GRAVITY;
        }else {
            dy += Global.GRAVITY;
        }

        offsetX(dx);
        offsetY(dy);

    }

}
