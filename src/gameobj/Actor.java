package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Actor extends GameObject{
    public static int WALK_SPEED = 6;
    private final float WALK_ACCELERATION = 0.7f;
    private Velocity velocity;

    private Animator animator;
    private final int num;

    private boolean leftSpeedUp;
    private boolean rightSpeedUp;


    public Actor(int x, int y){
        super(x,y,32*2,32*2);
        velocity = new Velocity(0,0,0,0,false);
        leftSpeedUp = false;
        rightSpeedUp = false;

        num = 1;
        animator = new Animator();
    }
    @Override
    public void paint(Graphics g) {
        if (!velocity.isReverse()) {
            animator.paint(g, num, painter().left(), painter().top(), painter().right(), painter().bottom());
        }else {
            animator.paintReverse(g, num, painter().left(), painter().top(), painter().right(), painter().bottom());
        }
    }

    @Override
    public void update() {

        // walking, acceleration increase
        if (leftSpeedUp){
            velocity.offsetDX(-WALK_ACCELERATION);
        }
        if (rightSpeedUp){
            velocity.offsetDX(WALK_ACCELERATION);
        }

        velocity.update();
        move();
    }

    public Velocity velocity() {
        return velocity;
    }

    public void preMove(){
        offsetX(-velocity.x());
        offsetY(-velocity.y());
    }

    public void leftSpeedUp(boolean speedUp){
        leftSpeedUp = speedUp;
        actionAnimator.setDirection(Global.Direction.LEFT);
    }

    public void rightSpeedUp(boolean speedUp){
        rightSpeedUp = speedUp;
        actionAnimator.setDirection(Global.Direction.RIGHT);
    }

    public void move(){
        offsetX(velocity.x());
        offsetY(velocity.y());
    }

    public void moveX(){
        offsetX(velocity.x());
    }

    public void moveY(){
        offsetY(velocity.y());
    }

    private enum IMG_DIRECTION{
        FORWARD(0),
        LEFT(1),
        RIGHT(2),
        BACKWARD(3);

        private int index;
        IMG_DIRECTION(int index){
            this.index = index;
        }
    }

    private class Animator{
        private Image img;
        private Image imgR;
        private int count; // ??
        private final int[] loopArr = {0, 1, 2, 1};
        // private int[] WALK = {0, 1, 2, 1};
        private final int  UNIT_X;
        private final int  UNIT_Y;

        private final int ROW_ACTOR;
        private final int ROW_ACTION;
        private final int COLUMN_ACTION;

        // private Global.Direction direction;
        private Delay delay;

        public Animator(){
            img = ImageController.getInstance().tryGet("/img/Actor1.png");
            imgR = ImageController.getInstance().tryGet("/img/Actor1_R.png");
            count = 0; // ??
            UNIT_X = 32;
            UNIT_Y = 32;
            ROW_ACTOR = 4;
            ROW_ACTION = 3;
            COLUMN_ACTION = 4;
            //direction = Global.Direction.NO;
            delay = new Delay(10);
            delay.loop();
        }
//        public void setDirection(Global.Direction direction) {
//            this.direction = direction;
//        }
    }

    public void paint(Graphics g, int num, int left, int top, int right, int bottom){
        int tx = (num % animator.ROW_ACTOR) * animator.UNIT_X * animator.ROW_ACTION;
        int ty = (num / animator.ROW_ACTOR) * animator.UNIT_Y * animator.COLUMN_ACTION;

        if (this.leftSpeedUp){
            ty += IMG_DIRECTION.LEFT.index * animator.UNIT_Y;
        }else if(this.rightSpeedUp){
            ty += IMG_DIRECTION.RIGHT.index * animator.UNIT_Y;
        }else{
            tx +=animator.UNIT_X;
        }

        g.drawImage(img, left, top, right, bottom,  // 會縮放! 要修掉!
                tx, ty, tx + animator.UNIT_X, ty + animator.UNIT_Y);



//            switch (direction){
//                case NO:
//                    tx = (num%4)*UNIT_X*3;
//                    ty = (num/4)*UNIT_Y*4;
//                    g.drawImage(img, left, top, right, bottom, //畫布上的位置
//                            tx+UNIT_X, ty, //依照來源圖片的位置取出圖片後，放到畫布上的位置
//                            tx+UNIT_X*2, ty+UNIT_Y, null);
//                    break;
//
//                case LEFT:
//                    tx = (num%4)*UNIT_X*3;
//                    ty = (num/4)*UNIT_Y*4 + UNIT_Y; //第一個4是因為一排四個人物，第二個4是因為直排每過4個圖片就換一個人物
//                    paintFunc(g, tx, ty, left, top, right, bottom);
//                    break;
//
//                case RIGHT:
//                    tx = (num%4)*UNIT_X*3;
//                    ty = (num/4)*UNIT_Y*4 + UNIT_Y*2;
//                    paintFunc(g, tx, ty, left, top, right, bottom);
//                    break;
//            }
//    }

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
