package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Actor extends GameObject{
    private Image img;
    private static int WALK_SPEED = 6;
    private static int JUMP_SPEED = 16;
    private float dy;
    private int dx;
    private int jumpState;


    public Actor(int x, int y){
        super(x,y,32,32);
        img = ImageController.getInstance().tryGet("/img/Actor1.png");
        dx = 0;
        dy = 0;
        jumpState = 0;
    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), painter().right(), painter().bottom(),
                0,0,32,32,null);
    }
    @Override
    public void update() {
        fall();
        walk();
    }

    public int dx(){
        return dx;
    }

    public int dy(){
        return (int)dy;
    }

    public void preMove(){
        offsetX(-dx);
        offsetY(-(int)dy);
    }

    public void walkLeft(){
        dx = -WALK_SPEED;
    }

    public void walkRight(){
        dx = WALK_SPEED;
    }

    public void walkStop(){
        dx = 0;
    }

    public void walk(){
        offsetX(dx);
    }

    public void jump(){
        System.out.println(jumpState);
        if(jumpState > 0){
            dy -= JUMP_SPEED;
            jumpState--;
        }
    }

    public void fall(){
        dy += Global.GRAVITY;
        offsetY((int)dy);
    }

    public void zeroDy(){
        dy = 0;
    }

    public void resetJumpState(){
        this.jumpState = Global.continueJump;
    }
}
