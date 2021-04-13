package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;
import game.utils.Vector;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Actor extends GameObject {

    private static final int JUMP_SPEED = 30;
    public static final int WALK_SPEED = 5;

    private Vector velocity;
    private int jumpCount;
    private boolean isReverse;
    private boolean canReverse;

    private int nextX;
    private int nextY;

    private Global.Direction direction;
    private ActionAnimator actionAnimator;

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
        super(x, y, 32, 32);
        velocity = new Vector(0,0);
        jumpCount = 0;
        this.isReverse = false;
        this.canReverse = false;

        direction = Global.Direction.NO;
        actionAnimator = new ActionAnimator();

        this.img=ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        setImage(num);

        rebornX = x;
        rebornY = y;
        rebornState = false;
        keyCount  = 0;
    }

    @Override
    public void update() {
        if (velocity.x() == 0){
            direction = Global.Direction.NO;
        }else if(velocity.x() > 0){
            direction = Global.Direction.RIGHT;
        }else{
            direction = Global.Direction.LEFT;
        }
        gravity();
        nextX = this.collider().left() + (int)velocity.x();
        nextY = this.collider().top() + (int)velocity.y();
    }

    @Override
    public void paint(Graphics g) {
        if (isReverse) {
            actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), direction);
        } else {
            actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), direction);
        }
    }

    public void setImage(int num) {
        this.imgRight = ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        this.imgLeft = ImageController.getInstance().tryGet("/img/actor_"+num+"_l.png");
        this.imgRightRev = paintReverse(imgRight);
        this.imgLeftRev = paintReverse(imgLeft);
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

    public void reverse(){
        if(canReverse){
            isReverse = !isReverse;
        }
        canReverse = false;
    }

    public void gravity(){
        if(isReverse){
            velocity.addY(-Global.GRAVITY);
        }else{
            velocity.addY(Global.GRAVITY);
        }
    }

    public Vector velocity(){
        return velocity;
    }

    public void move(){
        this.setXY(nextX, nextY);
    }

    public void jump() {
        if (jumpCount > 0) {
            this.velocity.addY(Actor.JUMP_SPEED);
            jumpCount--;
        }
    }

    public void jumpReset() {
        jumpCount = Global.continueJump;
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
        isReverse = rebornState;
    }

    public void block(GameObject obj){
        this.canReverse = true;
        Rect colliderX;
        Rect colliderY;
        if(velocity.x() < 0){ // 左
            colliderX = new Rect(this.collider().left() + (int)velocity.x(),this.collider().top(),
                    (int)velocity.absX(),this.collider().height());
            if(colliderX.isOverlap(obj.collider())){
                if(nextX < obj.collider().right()){
                    nextY = obj.collider().right();
                    velocity.zeroX();
                }

            }
        }else if(velocity.x() > 0){ //右
            colliderX = new Rect(this.collider().right(), this.collider().top(),
                    (int)velocity.absX(),this.collider().height());
            if(colliderX.isOverlap(obj.collider())){
                if(nextX > obj.collider().left() - this.collider().width()){
                    nextX = obj.collider().left() - this.collider().width();
                    velocity.zeroX();
                }

            }
        }
        if(velocity.y() > 0){ // 上
            colliderY = new Rect(this.collider().left(),this.collider().bottom() + (int)this.velocity.y(),
                    this.collider().width(), (int)velocity.absY());
            if(colliderY.isOverlap(obj.collider())){
                if( nextY < obj.collider().bottom()){
                    nextY = obj.collider().bottom();
                    velocity.zeroY();
                }

            }
        }else if(velocity.y() < 0){
            colliderY = new Rect(this.collider().left(),this.collider().top(),
                    this.collider().width(), (int)velocity.absY());
            if(colliderY.isOverlap(obj.collider())){
                if(nextY > obj.collider().top() - this.collider().height()){
                    nextY = obj.collider().top() - this.collider().height();
                    velocity.zeroY();
                }

                //velocity.setY(obj.collider().top() - this.collider().height());
            }
        }
    }


//        this.preMove();
//        this.moveY();
//        if (this.isCollision(obj)) { // 撞到 Y
//            this.jumpReset();
//            if (this.velocity().y() < 0) {
//                this.setY(obj.collider().bottom() +1 );
//                this.velocity().stopY();
//            } else if (this.velocity().y() > 0) {
//                this.setY(obj.collider().top() - this.painter().height() -1);
//                this.velocity().stopY();
//            }
//            this.moveX();
//            canReverse = true;
//
//        }
//        // y還沒被修正的掉落? 應該是撞到前一塊物件
//        if (this.collider().bottom() == obj.collider().top() |
//                this.collider().top() == obj.collider().bottom()) {
//            this.moveX();
//        }


    public void getKey(){
        this.keyCount++;
    }

    @Override
    public void collisionEffect(Actor actor) {
    }
}
