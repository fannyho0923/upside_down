package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ActorV extends GameObject{
    private static int WALK_SPEED = 6;
    private float dy;
    private int dx;
    private boolean isReverse;
    private ActionAnimator actionAnimator;
    private final int num;


    public ActorV(int x, int y){
        super(x,y,32*2,32*2);
        dx = 0;
        dy = 0;
        isReverse = false;
        num = 1;
        actionAnimator = new ActionAnimator();
    }
    @Override
    public void paint(Graphics g) {
        if (!isReverse) {
            actionAnimator.paint(g, num, painter().left(), painter().top(), painter().right(), painter().bottom());
        }else {
            actionAnimator.paintReverse(g, num, painter().left(), painter().top(), painter().right(), painter().bottom());
        }
    }

    @Override
    public void update() {
        fall();
        walk();
    }

    public void reverse() {
        isReverse = !isReverse;
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


    public void fall(){
        if(isReverse){
            dy -= Global.GRAVITY;
        }else{
            dy += Global.GRAVITY;
        }

        offsetY((int)dy);
    }

    public void zeroDy(){
        dy = 0;
    }


    private static class ActionAnimator{
        private Image img;
        private Image imgR;
        private int count;
        private int[] WALK={0, 1, 2, 1};
        private int UNIT_X;
        private int UNIT_Y;
        private Global.Direction direction;
        private Delay delay;

        public ActionAnimator(){
            img = ImageController.getInstance().tryGet("/img/Actor1.png");
            imgR = ImageController.getInstance().tryGet("/img/Actor1_R.png");
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
            }
        }


        public void paintReverse(Graphics g, int num,
                          int left, int top, int right, int bottom){
            int tx;
            int ty;
            switch (direction){
                case NO:
                    tx = ((7-num)%4)*UNIT_X*3;
                    ty = ((7-num)/4)*UNIT_Y*4 + UNIT_Y*3;
                    g.drawImage(imgR, left, top, right, bottom, //畫布上的位置
                            tx+UNIT_X, ty, //依照來源圖片的位置取出圖片後，放到畫布上的位置
                            tx+UNIT_X*2, ty+UNIT_Y, null);
                    break;

                case LEFT:
                    tx = ((7-num)%4)*UNIT_X*3;
                    ty = ((7-num)/4)*UNIT_Y*4 + UNIT_Y;
                    paintFuncR(g, tx, ty, left, top, right, bottom);
                    break;

                case RIGHT:
                    tx = ((7-num)%4)*UNIT_X*3;
                    ty = ((7-num)/4)*UNIT_Y*4 + UNIT_Y*2;
                    paintFuncR(g, tx, ty, left, top, right, bottom);
                    break;
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
        public void paintFuncR(Graphics g, int tx, int ty,
                              int left, int top, int right, int bottom){
            if (delay.count()) {
                count = ++count % 4;
            }
            g.drawImage(imgR, left, top, right, bottom, //畫布上的位置
                    tx + UNIT_X*WALK[count], ty, //依照來源圖片的位置取出圖片後，放到畫布上的位置
                    tx + UNIT_X*WALK[count] + UNIT_X, ty+UNIT_Y, null);
        }



    }
}
