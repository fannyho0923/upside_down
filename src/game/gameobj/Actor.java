package game.gameobj;

import game.controller.AudioResourceController;
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
    private static final float WALK_ACCELERATION = 0f;
    public static final int WALK_SPEED = 5;

    private Velocity velocity;
    private Vector shift;
    private final Vector zeroShift = new Vector(0,0);

    private Global.Direction direction;
    private ActionAnimator actionAnimator;

    private boolean leftSpeedUp;
    private boolean rightSpeedUp;
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
        velocity = new Velocity(0, 0, 0, 0, false);
        shift = new Vector(0,0);

        direction = Global.Direction.NO;
        actionAnimator = new ActionAnimator();
        leftSpeedUp = false;
        rightSpeedUp = false;
        jumpCount = 0;
        this.img=ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        setImage(num);

        rebornX = x;
        rebornY = y;
        rebornState = velocity.isReverse();
        keyCount  = 0;
        canReverse = false;
    }

    @Override
    public void update() {
        // walking, acceleration increase
        if (leftSpeedUp) {
            velocity.offsetDX(-WALK_ACCELERATION);
            setDirection(Global.Direction.LEFT);
        } else if (rightSpeedUp) {
            velocity.offsetDX(WALK_ACCELERATION);
            setDirection(Global.Direction.RIGHT);
        } else {
            setDirection(Global.Direction.NO);
        }
        velocity.update();
        move();
    }

    @Override
    public void paint(Graphics g) {
        if (velocity.isReverse()) {
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

    // 移動
    public Velocity velocity() {
        return velocity;
    }

    public void preMove() {
        offsetX(-velocity.x());
        offsetY(-velocity.y());
    }

    public void leftSpeedUp(boolean speedUp) {
        leftSpeedUp = speedUp;
    }

    public void rightSpeedUp(boolean speedUp) {
        rightSpeedUp = speedUp;
    }

    public void move() {
        offsetX(velocity.x());
        offsetY(velocity.y());
        switch (this.direction) {
            case LEFT:
                this.img = imgLeft;
                this.imgRev = imgLeftRev;
                break;
            case RIGHT:
                this.img = imgRight;
                this.imgRev = imgRightRev;
                break;
            case NO:
                break;
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
    }

    public void beBlock(GameObject obj){
        if(shift.x() > 0){

        }else if(shift.x() < 0){

        }
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

    public void shift(Vector shift){
        this.shift = shift;
    }

    public void shift(){
        offset((int)shift.x(),(int)shift.y());
        shift = zeroShift;
    }

    public void getKey(){
        this.keyCount++;
    }

}
