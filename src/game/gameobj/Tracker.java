package game.gameobj;

import game.camera.Camera;
import game.utils.Global;

import java.awt.*;

public class Tracker extends GameObject {

    private int moveSpeed;
    private int goalX;
    private int goalY;

    public Tracker(int x, int y, int moveSpeed){
        super(x,y, Global.UNIT_X,Global.UNIT_Y);
        this.moveSpeed = moveSpeed;
        goalX = x;
        goalY = y;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void tryMove(){
        if(this.painter().left() < goalX){ // 左
            this.offsetX(moveSpeed);
        }
        if(this.painter().top() < goalY){  // 上
            this.offsetY(moveSpeed);
        }
        if(this.painter().left() > goalX){ // 右
            this.offsetX(-moveSpeed);
        }
        if(this.painter().top() > goalY){  // 下
            this.offsetY(-moveSpeed);
        }
    }

    public void leftShift(){
        this.offsetX(-moveSpeed);
        goalX = this.painter().left(); // 暫時沒用到goal~ 但為了合理性還是先改移下好了
    }

    public void up(){
        this.offsetY(-moveSpeed);
        goalY = this.painter().top();
    }

    public void rightShift(){
        this.offsetX(moveSpeed);
        goalX = this.painter().left();
    }

    public void down(){
        this.offsetY(moveSpeed);
        goalY = this.painter().top();
    }

    public void moveTo(int x, int y){
        goalX = x;
        goalY = y;
    }


}
