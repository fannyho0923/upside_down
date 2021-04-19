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
    private static final int JUMP_SPEED = 15;
    public static final int WALK_SPEED = 3;

    private Velocity velocity;
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
        velocity = new Velocity(0,0, false);

        actionAnimator = new ActionAnimator();
        jumpCount = 0;
        setImage(num);
        this.img = imgRight;
        this.imgRev = imgRightRev;

        rebornX = x;
        rebornY = y;
        rebornState = velocity.isReverse();
        keyCount  = 0;
        canReverse = true;
    }

    @Override
    public void update() {
        setCanReverse(false);
        velocity.update();
        move();
    }

    @Override
    public void paint(Graphics g) {
        if (velocity.isReverse()) {
            actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                    painter().right(), painter().bottom());
        } else {
            actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                    painter().right(), painter().bottom());
        }
    }

    private Image paintReverse(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
    }

    // 人物動畫
    private class ActionAnimator {
        private int count;
        private Delay delay;
        public ActionAnimator() {
            count = 0;
            delay = new Delay(3);
            delay.loop();
        }
        public void paint(Graphics g, Image img, int left, int top, int right, int bottom) {
            if (velocity.x() != 0) {
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


    // 移動
    public Velocity velocity() {
        return velocity;
    }

    public void preMove() {
        offsetX(-velocity.x());
        offsetY(-velocity.y());
    }

    public void move() {
        offsetX(velocity.x());
        offsetY(velocity.y());
        System.out.println(velocity.y());
        if(velocity.x() > 0){
            this.img = imgRight;
            this.imgRev = imgRightRev;
        }else if(velocity.x() < 0){
            this.img = imgLeft;
            this.imgRev = imgLeftRev;
        }
    }

    public void moveX() {
        offsetX(velocity.x());
    }

    public void moveY() {
        offsetY(velocity.y());
    }

    public void jump() {
        if (jumpCount > 0) {
            this.velocity.offsetY(-JUMP_SPEED);
            jumpCount--;
        }
    }

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
        velocity.setGravityReverse(rebornState);
        velocity().stop();
    }

    public void beBlock(GameObject obj){

        canReverse = true;
        this.preMove();
        this.moveY();
        if (this.isCollision(obj)) { // 撞到 Y
            this.jumpReset();
            if (this.velocity().y() < 0) {
                this.setY(obj.collider().bottom() +1 );
                this.velocity().stopY();
            } else if (this.velocity().y() > 0) {
                this.setY(obj.collider().top() - this.painter().height() -1);
                this.velocity().stopY();
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
}
