package game.camera;

import game.gameobj.GameObject;
import game.utils.Global;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Camera extends GameObject {

    //地圖資訊由 MapInformation 類別直接呼叫，可以進行將相關資訊設定到Global 其實只需要地圖的寬跟長即可
    //相機設定
    protected AffineTransform tmpCanvas;// 暫存畫布移動前的位置
    private int cameraMoveSpeed; //鏡頭移動速度
    private double chaseDivisorX; // 追焦時X軸要除的值，越小追越快
    private double chaseDivisorY; // 追焦時Y軸要除的值，越小追越快
    private GameObject obj;  //要跟焦的對象，如果null 就代表不用跟畫面可自由移動
    private boolean lockLeft; //鎖住鏡頭左方向(鏡頭無法向左移動)
    private boolean lockUp; //鎖住鏡頭上方向(鏡頭無法向上移動)
    private boolean lockRight; //鎖住鏡頭右方向(鏡頭無法向右移動)
    private boolean lockDown; //鎖住鏡頭下方向(鏡頭無法向下移動)
    private int cameraWindowX; // 此顆鏡頭實際在畫面的左上角X座標
    private int cameraWindowY; // 此顆鏡頭實際在畫面的左上角Y座標

    private Camera(final int width, final int height) {
        super(0, 0, width, height);
        this.cameraMoveSpeed = 10;
        this.chaseDivisorX = 1;
        this.chaseDivisorY = 1;
        this.lockLeft = false;
        this.lockUp = false;
        this.lockRight = false;
        this.lockDown = false;
        this.cameraWindowX = 0;
        this.cameraWindowY = 0;

    }

    protected Camera(final Camera cam) {
        super(cam.collider().centerX(), cam.collider().centerY(), cam.collider().width(), cam.collider().height(),
                cam.painter().centerX(), cam.painter().centerY(), cam.painter().width(), cam.painter().height());
        setObj(cam.obj());
        this.cameraMoveSpeed = cam.cameraMoveSpeed;
        this.chaseDivisorX = cam.chaseDivisorX;
        this.chaseDivisorY = cam.chaseDivisorY;
        this.cameraWindowX = cam.cameraWindowX;
        this.cameraWindowY = cam.cameraWindowY;
        this.lockLeft = cam.lockLeft;
        this.lockUp = cam.lockUp;
        this.lockRight = cam.lockRight;
        this.lockDown = cam.lockDown;
    }

    //讓鏡頭跟著物件移動
    public void setObj(final GameObject obj) {
        this.obj = obj;
        if (this.obj != null) {
            //目前設定物件在鏡頭正中央
            int left = obj.painter().centerX() - this.painter().width() / 2;
            int right = obj.painter().centerX() + this.painter().width() / 2;
            int top = obj.painter().centerY() - this.painter().height() / 2;
            int bottom = obj.painter().centerY() + this.painter().height() / 2;
            if (touchLeft()) {
                left = MapInformation.getInstance().left();
                right = left + this.painter().width();
            }
            if (touchTop()) {
                top = MapInformation.getInstance().top();
                bottom = top + this.painter().height();
            }
            if (touchRight()) {
                right = MapInformation.getInstance().right();
                left = right - this.painter().width();
            }
            if (touchBottom()) {
                bottom = MapInformation.getInstance().bottom();
                top = bottom - this.painter().height();
            }
            this.setX(left);
            this.setY(top);
        }
    }

    private void chaseMove() { //鏡頭追蹤加速度 數字越大追越慢 當數字為1時鏡頭移動速度與物件移動速度相同
        final double targetX = (this.obj.painter().centerX() - this.painter().centerX()) / this.chaseDivisorX;
        final double targetY = (this.obj.painter().centerY() - this.painter().centerY()) / this.chaseDivisorY;
        if (targetX > 0 && !touchRight() && !this.lockRight) {
            offsetX((int) targetX);
        } else if (targetX < 0 && !touchLeft() && !this.lockLeft) {
            offsetX((int) targetX);
        }
        if (targetY > 0 && !touchBottom() && !this.lockUp) {
            offsetY((int) targetY);
        } else if (targetY < 0 && !touchTop() && !this.lockDown) {
            offsetY((int) targetY);
        }
    }

    /*使用時，請在場景的paint方法中
    1.game.camera.start(g) //將畫布移動到您的顯示視窗範圍(0,0)
    2.放入您的物件(請讓每個物件與camera做isCollision碰撞判斷，有重疊到才paint)
    EX: if(game.camera.isCollision(ac)){
            ac.paint(g);
        }
    3. game.camera.end(g) 將畫布移回原位
    4. 如果有第二顆camera 再次操作 1 ~ 3。
    */
    public void start(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        this.tmpCanvas = g2d.getTransform(); // 暫存畫布
        // 先將畫布初始移到(0,0)（-painter().left()/top()) 然後再到自己想定位的地點(+ cameraWindowX/Y)
        g2d.translate(-this.painter().left() + this.cameraWindowX, -this.painter().top() + this.cameraWindowY);
        // 將畫布依照鏡頭大小作裁切
        g.setClip(this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height());
    }

    public void end(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(this.tmpCanvas); // 將畫布移回原位
        g.setClip(null); //把畫布的裁切還原。
    }

    @Override
    public void paint(final Graphics g) {
        if (Global.IS_DEBUG) {
            //畫圖形測試外框
            g.setColor(Color.RED);
            g.drawRect(this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height());
            g.setColor(Color.BLUE);
            g.drawRect(this.collider().left(), this.collider().top(), this.collider().width(), this.collider().height());
        }
    }

    @Override
    public void update() {
        if (this.obj != null) {
            chaseMove(); // 追焦功能
        }
    }

    public void setCameraMoveSpeed(final int num) {
        this.cameraMoveSpeed = num;
    }

    public void setChaseDivisorX(final double num) {
        this.chaseDivisorX = num;
    }

    public void setChaseDivisorY(final double num) {
        this.chaseDivisorY = num;
    }

    public GameObject obj() {
        return this.obj;
    }

    public void lockLeft(final boolean lock) {
        this.lockLeft = lock;
    }

    public void lockRight(final boolean lock) {
        this.lockRight = lock;
    }

    public void lockUp(final boolean lock) {
        this.lockUp = lock;
    }

    public void lockDown(final boolean lock) {
        this.lockDown = lock;
    }

    public void setCameraWindowX(final int num) {
        this.cameraWindowX = num;
    }

    public int cameraWindowX() {
        return this.cameraWindowX;
    }

    public void setCameraWindowY(final int num) {
        this.cameraWindowY = num;
    }

    public int cameraWindowY() {
        return this.cameraWindowY;
    }

    public static class Builder {
        private final Camera cam;

        //設定鏡頭的寬度與高度
        public Builder(final int width, final int height) {
            this.cam = new Camera(width, height);
        }

        //設定要追蹤的物件(鏡頭跟著該物件移動)。物件為null時，鏡頭可自由移動
        public Builder setChaseObj(final GameObject obj) {
            this.cam.setObj(obj);
            return this;
        }

        //設定要追蹤的物件與追焦速度。後兩個參數越大，物件移動時鏡頭越慢跟上物件。參數需大於1.0，1.0的時候是與物件移動速度相同
        public Builder setChaseObj(final GameObject obj, final double chaseDivisorX, final double chaseDivisorY) {
            this.cam.setObj(obj);
            this.cam.setChaseDivisorX(chaseDivisorX);
            this.cam.setChaseDivisorY(chaseDivisorY);
            return this;
        }

        //設定鏡頭沒有追蹤物件時，自由移動畫面的速度
        public Builder setCameraMoveSpeed(final int num) {
            this.cam.setCameraMoveSpeed(num);
            return this;
        }

        //設定鏡頭在地圖絕對座標的初始位置(參數代表鏡頭左上角座標)。預設為(0,0)
        public Builder setCameraStartLocation(final int left, final int top) {
            this.cam.setXY(left, top);
            return this;
        }

        //設定鏡頭在視窗中顯示位置(左上角)，預設為(0, 0)
        public Builder setCameraWindowLocation(final int left, final int top) {
            this.cam.setCameraWindowX(left);
            this.cam.setCameraWindowY(top);
            return this;
        }

        //限定鏡頭移動方位，未設定時皆可移動
        public Builder setCameraLockDirection(final boolean left, final boolean up, final boolean right, final boolean down) {
            this.cam.lockLeft(left);
            this.cam.lockUp(up);
            this.cam.lockRight(right);
            this.cam.lockDown(down);
            return this;
        }

        public Camera gen() {
            return new Camera(this.cam);
        }
    }
}
