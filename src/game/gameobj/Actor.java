package game.gameobj;

<<<<<<< HEAD
<<<<<<< HEAD:src/game/gameobj/Actor.java
import game.controller.ImageController;
import game.utils.Global;
=======
import controller.ImageController;
import utils.Delay;
import utils.Global;
>>>>>>> 67f0f1c3d2778b1663c66bcaa19ef5ee271f7ecf:src/gameobj/Actor.java
=======
import game.controller.ImageController;
import game.utils.Delay;
import game.utils.Global;
>>>>>>> 8606fae96e5ba903d449a7313015fc36f79395b4

import java.awt.*;

public class Actor extends GameObject{
    private Image img;
    private static int WALK_SPEED = 6;
    private static int JUMP_SPEED = 16;
    private float dy;
    private int dx;
    private int jumpState;
    private ActionAnimator actionAnimator;
    private final int num;

    public Actor(int x, int y){
        super(x,y,32*2,32*2);
        img = ImageController.getInstance().tryGet("/img/Actor1.png");
        dx = 0;
        dy = 0;
        jumpState = 0;
        num = 1;
        actionAnimator = new ActionAnimator();
    }


    @Override
    public void paint(Graphics g) {
//        g.drawImage(img, painter().left(), painter().top(), painter().right(), painter().bottom(),
//                0,0,32,32,null);
        actionAnimator.paint(g, num, painter().left(), painter().top(), painter().right(), painter().bottom());
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
        actionAnimator.setDirection(Global.Direction.LEFT);
    }

    public void walkRight(){
        dx = WALK_SPEED;
        actionAnimator.setDirection(Global.Direction.RIGHT);
    }

    public void walkStop(){
        dx = 0;
        actionAnimator.setDirection(Global.Direction.NO);
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


    private static class ActionAnimator{
        private Image img;
        private int count;
        private int[] WALK={0, 1, 2, 1};
        private int UNIT_X;
        private int UNIT_Y;
        private Global.Direction direction;
        private Delay delay;


        public ActionAnimator(){
//            try {
//                img = ImageIO.read(getClass().getResource("/monster.png"));
//            }catch (IOException e){
//                e.printStackTrace();
//            }
            img = ImageController.getInstance().tryGet("/Actor1.png");
            count = 0;
            UNIT_X = 32;
            UNIT_Y = 32;
            direction = Global.Direction.NO;
            delay = new Delay(10);
            delay.loop();
        }

        public void setDirection(Global.Direction direction) {
            this.direction = direction;
        }

        public void paint(Graphics g, int num,
                          int left, int top, int right, int bottom){
            int tx;
            int ty;
            switch (direction){
                case NO:
                    tx = (num%4)*UNIT_X*3;
                    ty = (num/4)*UNIT_Y*4;
                    g.drawImage(img, left, top, right, bottom, //畫布上的位置
                            tx+UNIT_X, ty, //依照來源圖片的位置取出圖片後，放到畫布上的位置
                            tx+UNIT_X*2, ty+UNIT_Y, null);
                    break;

//                case UP:
//                    tx = (num%4)*UNIT_X*3;
//                    ty = (num/4)*UNIT_Y*4 + UNIT_Y*3;
//                    paintFunc(g, tx, ty, left, top, right, bottom);
//                    break;

                case LEFT:
                    tx = (num%4)*UNIT_X*3;
                    ty = (num/4)*UNIT_Y*4 + UNIT_Y; //第一個4是因為一排四個人物，第二個4是因為直排每過4個圖片就換一個人物
                    paintFunc(g, tx, ty, left, top, right, bottom);
                    break;

                case RIGHT:
                    tx = (num%4)*UNIT_X*3;
                    ty = (num/4)*UNIT_Y*4 + UNIT_Y*2;
                    paintFunc(g, tx, ty, left, top, right, bottom);
                    break;

//                case DOWN:
//                    tx = UNIT_X*0;
//                    ty = (num%8)*UNIT_Y;
//                    paintFunc(g, tx, ty, left, top, right, bottom);
//                    break;
            }
        }


        public void paintFunc(Graphics g, int tx, int ty,
                              int left, int top, int right, int bottom){
            if (delay.count()) {
                count = ++count % 4;
            }
            g.drawImage(img, left, top, right, bottom, //畫布上的位置
                    tx + UNIT_X*WALK[count], ty, //依照來源圖片的位置取出圖片後，放到畫布上的位置
                    tx + UNIT_X*WALK[count] + UNIT_X, ty+UNIT_Y, null);
        }

    }
}
