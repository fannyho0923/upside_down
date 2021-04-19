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
    public static final int WALK_SPEED = 5;
    public static final int FALL_SPEED = 5;

    private Global.Direction walkDir;
    private Global.Direction fallDir;
    private boolean canReverse;

    private ActionAnimator actionAnimator;

    private Image imgRight;
    private Image imgLeft;
    private Image imgRightRev;
    private Image imgLeftRev;
    private Image img;
    private Image imgRev;

    private State lifeState;

    private int rebornX;
    private int rebornY;
    private Global.Direction rebornDir;

    private int keyCount;

    public Actor(int x, int y, int num) {
        super(x, y, Global.UNIT_X32, Global.UNIT_Y32);

        this.walkDir = Global.Direction.NO;
        this.fallDir = Global.Direction.DOWN;
        actionAnimator = new ActionAnimator();
        this.img=ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        setImage(num);

        lifeState = State.Alive;
        rebornX = x;
        rebornY = y;
        rebornDir = fallDir;
        keyCount  = 0;
        canReverse = false;
    }

    @Override
    public void paint(Graphics g) {
        switch (fallDir){
            case UP:
                actionAnimator.paint(g, this.imgRev, painter().left(), painter().top(),
                        painter().right(), painter().bottom(), walkDir);
                break;
            case DOWN:
                actionAnimator.paint(g, this.img, painter().left(), painter().top(),
                        painter().right(), painter().bottom(), walkDir);
                break;
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

    @Override
    public void update() {
        // walking, acceleration increase
        switch(walkDir){
            case RIGHT:
                offsetX(WALK_SPEED);
                break;
            case LEFT:
                offsetX(-WALK_SPEED);
                break;
        }
        switch(fallDir){
            case UP:
                offsetY(-FALL_SPEED);
                break;
            case DOWN:
                offsetY(FALL_SPEED);
        }
    }

    public Global.Direction walkDir() {
        return walkDir;
    }

    public void setWalkDir(Global.Direction direction) {
        this.walkDir = direction;
    }

    public Global.Direction fallDir() {
        return fallDir;
    }

    public void setFallDir(Global.Direction fallDir) {
        this.fallDir = fallDir;
    }

    public void fallReverse(){
        if(fallDir == Global.Direction.UP){
            fallDir = Global.Direction.DOWN;
        }else {
            fallDir = Global.Direction.UP;
        }
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

    public void setRebornState(Global.Direction rebornDir) {
        this.rebornDir = rebornDir;
    }

    public void reborn(){
        this.setXY(rebornX,rebornY);
        this.fallDir = rebornDir;
        this.walkDir = Global.Direction.NO;
    }

    public void beBlock(GameObject obj){
        // 物件移動角色時被阻擋
        canReverse = true;

        switch (walkDir){   //回覆走路狀態
            case LEFT:
                offsetX(WALK_SPEED);
                break;
            case RIGHT:
                offsetX(-WALK_SPEED);
                break;
        }
        if(this.isCollision(obj)){
            // Y方向撞到
            switch (fallDir){
                case UP:
                    this.setY(obj.collider().bottom() +1);
                    break;
                case DOWN:
                    this.setY(obj.collider().top()-this.collider().height() -1);
                    break;
            }
        }
        switch (walkDir){   //回覆走路狀態
            case LEFT:
                offsetX(-WALK_SPEED);
                break;
            case RIGHT:
                offsetX(WALK_SPEED);
                break;
        }
    }

    public void getKey(){
        this.keyCount++;
    }

}
