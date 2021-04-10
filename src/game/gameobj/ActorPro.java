package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;
import game.utils.Delay;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ActorPro extends GameObject {
    private static final int JUMP_SPEED = 30;
    private static final float WALK_ACCELERATION = 0.5f;
    private Velocity velocity;
    private Global.Direction direction;
    private ActionAnimator actionAnimator;

    private boolean leftSpeedUp;
    private boolean rightSpeedUp;
    private int jumpCount;
    private Image img;


    public ActorPro(int x, int y) {
        super(x, y, Global.UNIT_X32, Global.UNIT_Y32);
        velocity = new Velocity(0, 0, 0, 0, false);
        direction = Global.Direction.NO;
        actionAnimator = new ActionAnimator();
        leftSpeedUp = false;
        rightSpeedUp = false;
        jumpCount = 0;
        img = ImageController.getInstance().tryGet("/img/actor_1.png");
    }

    public void setDirection(Global.Direction direction) {
        this.direction = direction;
    }

    public Global.Direction getDirection() {
        return direction;
    }

    @Override
    public void paint(Graphics g) {
        if (velocity.isReverse()) {
            actionAnimator.paint(g, paintReverse(img), painter().left(), painter().top(),
                    painter().right(), painter().bottom(), getDirection());
        } else {
            actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), getDirection());
        }
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

//    public void setVelocity(Velocity velocity) {
//        this.velocity = velocity;
//    }

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
                this.img = ImageController.getInstance().tryGet("/img/actor_1_l.png");
                break;
            case RIGHT:
                this.img = ImageController.getInstance().tryGet("/img/actor_1.png");
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
}
