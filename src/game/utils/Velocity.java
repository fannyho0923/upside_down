package game.utils;

public class Velocity {
    private int x;
    private int y;
    private boolean gravityReverse;

    private final int MAX_Y = 6;

    public Velocity(int x,int y, boolean gravityReverse){
        this.x = x;
        this.y = y;
        this.gravityReverse = gravityReverse;
    }

    public void gravityReverse(){
        gravityReverse = !gravityReverse;
        if (gravityReverse){
            offsetY(-5);
        }else {
            offsetY(5);
        }
    }

    public boolean isReverse(){
        return gravityReverse;
    }

    public void setGravityReverse(boolean gravityReverse) {
        this.gravityReverse = gravityReverse;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void offsetY(float y){
        this.y += y;
        if(this.y > MAX_Y){
            this.y = MAX_Y;
        }else if(this.y < -MAX_Y){
            this.y = - MAX_Y;
        }
    }

    public void stop(){
        x = 0;
        y = 0;
    }

    public void stopX(){
        x = 0;
    }

    public void stopY(){
        y = 0;
    }

    public void update(){
        if (gravityReverse){
            offsetY(-1);
        }else {
            offsetY(1);
        }
    }
}
