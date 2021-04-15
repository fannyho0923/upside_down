package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;
import game.utils.Velocity;
import game.utils.Vector;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Actor extends GameObject {
    private static final int JUMP_SPEED = 30;
    public static final int WALK_SPEED = 5;

    //private Velocity velocity;
    private Vector v;
    private boolean isReverse;
    private Global.Direction direction;
    private ActionAnimator actionAnimator;

    private int jumpCount;
    private boolean canReverse;

    private Image imgRight;
    private Image imgLeft;
    private Image imgRightRev;
    private Image imgLeftRev;
    private Image img;
    private Image imgRev;

    private int rebornX;
    private int rebornY;
    private boolean rebornState;
    private int keyCount;


    public Actor(int x, int y, int num) {
        super(x, y, Global.UNIT_X32, Global.UNIT_Y32);
        v = new Vector(0,0);
        //velocity = new Velocity(0, 0, 0, 0, false);
        direction = Global.Direction.NO;
        isReverse = false;
        jumpCount = 0;

        this.img=ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        setImage(num);
        actionAnimator = new ActionAnimator();

        rebornX = x;
        rebornY = y;
        rebornState = isReverse();
        keyCount  = 0;
        canReverse = false;
    }

    @Override
    public void update() {
        //velocity.update();
        if(isReverse){
            v.addY(-Global.GRAVITY);
        }else {
            v.addY(Global.GRAVITY);
        }
        move();

    }

    @Override
    public void paint(Graphics g) {
        //if (velocity.isReverse()) {
        if (isReverse)   {
            actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), getDirection());
        } else {
            actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), getDirection());
        }
    }

    private Image paintReverse(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
    }

    // 人物動畫
    private static class ActionAnimator {
        private int count;
        private Delay delay;

        public ActionAnimator() {
            count = 0;
            delay = new Delay(3);
            delay.loop();
        }

        public void paint(Graphics g, Image img, int left, int top, int right, int bottom, Global.Direction direction) {
            if (direction != Global.Direction.NO) {
                if (delay.count()) {
                    count = ++count % 8;
                }
            }
            g.drawImage(img, left, top, right, bottom,
                    count * Global.UNIT_X64 + 22,
                    14,
                    count * Global.UNIT_X64 + Global.UNIT_X64 - 22,
                    Global.UNIT_Y64 - 16, null);
        }
    }
    public enum State{
        Alive,
        Dead;
    }

    public void setImage(int num) {
        this.imgRight = ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        this.imgLeft = ImageController.getInstance().tryGet("/img/actor_"+num+"_l.png");
        this.imgRightRev = paintReverse(imgRight);
        this.imgLeftRev = paintReverse(imgLeft);
    }

    public Global.Direction getDirection() {
        return direction;
    }

    public void setDirection(Global.Direction direction) {
        this.direction = direction;
    }

    public void reverse(){
        this.isReverse = !isReverse;
    }

    public void serReverse(boolean state){
        this.isReverse = state;
    }

    public boolean isReverse(){
        return isReverse;
    }

    // 移動
    public Vector velocity(){
        return v;
    }

//    public Velocity velocity() {
//        return velocity;
//    }

    public void preMove() {
        offset(-(int)v.x(),-(int)v.y());
    }

//    public void preMove() {
//        offsetX(-velocity.x());
//        offsetY(-velocity.y());
//    }

    private void move(){
        offset((int)v.x(),(int)v.y());
        System.out.println(v.x());
        switch (this.direction){
            case LEFT:
                this.img = imgLeft;
                this.imgRev = imgLeftRev;
                break;
            case RIGHT:
                this.img = imgRight;
                this.imgRev = imgRightRev;
                break;
        }
    }

//    public void move() {
//        offsetX(velocity.x());
//        offsetY(velocity.y());
//        switch (this.direction) {
//            case LEFT:
//                this.img = imgLeft;
//                this.imgRev = imgLeftRev;
//                break;
//            case RIGHT:
//                this.img = imgRight;
//                this.imgRev = imgRightRev;
//                break;
//            case NO:
//                break;
//        }
//    }

    private void moveX(){
        offsetX((int)v.x());
    }

    private void moveY(){
        offsetY((int)v.y());
    }

//    public void moveX() {
//        offsetX(velocity.x());
//    }

//    public void moveY() {
//        offsetY(velocity.y());
//    }

    public void jump() {
        if (jumpCount > 0) {
            this.v.addY(-JUMP_SPEED);
            jumpCount--;
        }
    }

//    public void jump() {
//        if (jumpCount > 0) {
//            this.velocity.offsetY(-JUMP_SPEED);
//            jumpCount--;
//        }
//    }

    public void jumpReset() {
        jumpCount = Global.continueJump;
    }

    public boolean canReverse(){
        return canReverse;
    }

    public void setCanReverse(boolean canReverse) {
        this.canReverse = canReverse;
    }

    // 場景物件效果
    public void setRebornX(int rebornX) {
        this.rebornX = rebornX;
    }

    public void setRebornY(int rebornY) {
        this.rebornY = rebornY;
    }

    public void setRebornState(boolean rebornState) {
        this.rebornState = rebornState;
    }

    public void reborn(){
        this.setXY(rebornX,rebornY);
        this.isReverse = rebornState;
        //velocity.setGravityReverse(rebornState);
    }

    public void beBlock(GameObject obj){
        canReverse = true;
        this.preMove();
        this.moveY();
        if (this.isCollision(obj)) { // 撞到 Y
            this.jumpReset();
            if (this.velocity().y() < 0) {
                this.setY(obj.collider().bottom() +1 );
                //this.velocity().stopY();
                this.v.zeroX();
            } else if (this.velocity().y() > 0) {
                this.setY(obj.collider().top() - this.painter().height() -1);
                //this.velocity().stopY();
                this.v.zeroX();
            }
            this.moveX();
        }
        if (this.collider().bottom() == obj.collider().top() |
                this.collider().top() == obj.collider().bottom()){
            this.moveX();
        }
    }

    public void getKey(){
        this.keyCount++;
    }

    @Override
    public void collisionEffect(Actor actor) {

    }
}
