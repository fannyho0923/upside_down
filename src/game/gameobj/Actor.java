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

    //private ActionAnimator actionAnimator;

    private Image imgRight;
    private Image imgLeft;
    private Image imgRightRev;
    private Image imgLeftRev;
    private Image img;
    private Delay AnimateDelay;
    private int AnimateCount;

    private State lifeState;
    private boolean stand;

    private int rebornX;
    private int rebornY;
    private Global.Direction rebornDir;

    private int keyCount;

    public Actor(int x, int y, int num) {
        super(x, y, Global.UNIT_X32, Global.UNIT_Y32);

        this.walkDir = Global.Direction.NO;
        this.fallDir = Global.Direction.DOWN;
        //actionAnimator = new ActionAnimator();
        this.img=ImageController.getInstance().tryGet("/img/actor_"+num+".png");
        setImage(num);

        AnimateDelay = new Delay(3);
        AnimateDelay.loop();
        AnimateCount = 0;

        lifeState = State.Alive;
        rebornX = x;
        rebornY = y;
        rebornDir = fallDir;
        keyCount  = 0;
        canReverse = false;
    }

    @Override
    public void paint(Graphics g) {
        if (AnimateDelay.count()){
            AnimateCount = AnimateCount++%8;
        }
        switch (fallDir){
            case UP:
                switch (walkDir){
                    case LEFT:
                        img = imgLeftRev;
                        break;
                    case RIGHT:
                        img = imgRightRev;
                        break;
                    case NO:
                        break;
                }
                break;
            case DOWN:
                switch (walkDir){
                    case LEFT:
                        img = imgLeft;
                        break;
                    case RIGHT:
                        img = imgRight;
                        break;
                    case NO:
                        break;
                }
                break;
        }

        if (walkDir != Global.Direction.NO){
            if (AnimateDelay.count()) {
                AnimateCount = ++AnimateCount % 8;
            }
        }

        g.drawImage(img, painter().left(), painter().top(),
                painter().right(), painter().bottom(),
                AnimateCount * Global.UNIT_X64 + 22,
                14,
                AnimateCount * Global.UNIT_X64 + Global.UNIT_X64 - 22,
                Global.UNIT_Y64 - 16,
                null);
    }

    private Image paintReverse(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
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
        this.stand = false;
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
                break;
        }
    }

    public void setStand(boolean stand){
        this.stand = stand;
    }

    public void beBlock(GameObject obj){

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

    public void getKey(){
        this.keyCount++;
    }

}
