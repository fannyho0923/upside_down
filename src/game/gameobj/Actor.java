package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;
import game.utils.Velocity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Actor extends GameObject {
    public static final int WALK_SPEED = 4;

    private Velocity velocity;
    public enum State {
        ALIVE,
        REBORN,
        DEAD;
    }

    private ActionAnimator actionAnimator;
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

    private State state;
    private Delay rebornDelay;
    private Delay splashDelay;
    private Delay deadDelay;

    public Actor(int x, int y, int num) {
        super(x, y, 40, 40);
        velocity = new Velocity(0,0, false);

        actionAnimator = new ActionAnimator();
        setImage(num);
        img = imgRight;
        imgRev  = imgRightRev;
        state = State.ALIVE;

        setReborn(x,y,velocity.isReverse());
        canReverse = false;

        splashDelay = new Delay(3);
        rebornDelay = new Delay(90);
        deadDelay = new Delay(50);
        splashDelay.loop(); //??
    }

    public State getState() {
        return state;
    }

    @Override
    public void update() {
        setCanReverse(false);
        switch (state) {
            case ALIVE:
                velocity.update();
                move();
                break;
            case DEAD:
                velocity.update();
                moveY();
                if(deadDelay.count()){
                    reborn();
                }
                break;
            case REBORN:
                velocity.update();
                move();
                if (rebornDelay.count()) {
                    state = State.ALIVE;
                }
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        switch (state) {
            case ALIVE:
                if (velocity.isReverse()) {
                    actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                            painter().right(), painter().bottom());
                } else {
                    actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                            painter().right(), painter().bottom());
                }
                break;
            case DEAD:
                if (velocity.isReverse()) {
                    actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                            painter().right(), painter().bottom());
                } else {
                    actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                            painter().right(), painter().bottom());
                }
            case REBORN:
                if (rebornDelay.isPlaying()) {
                    if (splashDelay.count()) {
                        if (velocity.isReverse()) {
                            actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                                    painter().right(), painter().bottom());
                        }else {
                            actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                                    painter().right(), painter().bottom());
                        }
                    }
                }
                break;
        }
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

    public void setImage(int num) {
        this.imgRight = ImageController.getInstance().tryGet("/img/actor/actor_" + num + "R.png");
        this.imgLeft = ImageController.getInstance().tryGet("/img/actor/actor_" + num + "L.png");
        this.imgRightRev = paintReverse(imgRight);
        this.imgLeftRev = paintReverse(imgLeft);
    }

    private Image paintReverse(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
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

    // 場景物件效果
    public boolean canReverse() {
        return canReverse;
    }

    public void setCanReverse(boolean canReverse) {
        this.canReverse = canReverse;
    }

    public void dead() {
        if (state == State.ALIVE) {
            deadDelay.play();
            state = State.DEAD;
            //AudioResourceController.getInstance().play("/sound/dead.wav");
        }
    }

    public void reborn(){
        this.setXY(rebornX, rebornY);
        velocity.setGravityReverse(rebornState);
        velocity().stop();
        state = State.REBORN;
        rebornDelay.play();
    }

    public void setReborn(int rebornX, int rebornY, boolean rebornState) {
        this.rebornX = rebornX;
        this.rebornY = rebornY;
        this.rebornState = rebornState;
    }

    public void beBlock(GameObject obj) {
        canReverse = true;
        this.preMove();
        this.moveY();
        if (this.isCollision(obj)) { // 撞到 Y
            if (this.velocity().y() < 0) {
                this.setY(obj.collider().bottom() + 1);
                this.velocity().stopY();
            } else if (this.velocity().y() > 0) {
                this.setY(obj.collider().top() - this.painter().height() - 1);
                this.velocity().stopY();
            }
            this.moveX();
        }
        if (this.collider().bottom() == obj.collider().top() |
                this.collider().top() == obj.collider().bottom()) {
            this.moveX();
        }
    }
}
