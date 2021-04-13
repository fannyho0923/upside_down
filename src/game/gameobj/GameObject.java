package game.gameobj;

import game.camera.MapInformation;
import game.utils.GameKernel;

public abstract class GameObject implements GameKernel.UpdateInterface, GameKernel.PaintInterface {
    private final Rect collider; //物件的碰撞方塊
    private final Rect painter;  //物件的繪圖方塊
    private int id;
    private boolean isExist;

    //預設以(top,left)為物件左上角生成物件
    public GameObject(final int top, final int left, final int width, final int height) {
        this.collider = new Rect(top, left, width, height);
        this.painter = new Rect(top, left, width, height);
        this.isExist = true;
    }

    //true:以(x,y)為物件中心生成物件 false:預設以(x,y)為物件左上角生成物件
    public GameObject(final int x, final int y, final int width, final int height, final boolean isGenWithCenter) {
        if (isGenWithCenter) {
            this.collider = Rect.genWithCenter(x, y, width, height);
            this.painter = Rect.genWithCenter(x, y, width, height);
        } else {
            this.collider = new Rect(x, y, width, height);
            this.painter = new Rect(x, y, width, height);
        }
    }

    public GameObject(final Rect collider, final Rect painter) {
        this.collider = new Rect(collider);
        this.painter = new Rect(painter);
    }

    //以(cCenterX,cCenterY)為中心生成物件繪圖方塊 以(pCenterX,pCenterY)為中心生成物件碰撞方塊
    public GameObject(final int cCenterX, final int cCenterY, final int cWidth, final int cHeight, final int pCenterX, final int pCenterY, final int pWidth, final int pHeight) {
        this.collider = Rect.genWithCenter(cCenterX, cCenterY, cWidth, cHeight);
        this.painter = Rect.genWithCenter(pCenterX, pCenterY, pWidth, pHeight);
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public Rect collider() {
        return this.collider;
    }

    public Rect painter() {
        return this.painter;
    }

    public boolean isCollision(final GameObject gameObject) {
        return this.collider.isOverlap(gameObject.collider());
    }

    public void offset(final int x, final int y) {
        this.collider.offsetX(x);
        this.collider.offsetY(y);
        this.painter.offsetX(x);
        this.painter.offsetY(y);
    }

    public void offsetX(final int x) {
        this.collider.offsetX(x);
        this.painter.offsetX(x);
    }

    public void offsetY(final int y) {
        this.collider.offsetY(y);
        this.painter.offsetY(y);
    }

    public void setXY(final int x, final int y) {
        this.collider.setX(x);
        this.collider.setY(y);
        this.painter.setX(x);
        this.painter.setY(y);
    }

    public void setX(final int x) {
        this.collider.setX(x);
        this.painter.setX(x);
    }

    public void setY(final int y) {
        this.collider.setY(y);
        this.painter.setY(y);
    }

    public boolean isOutOfScreen() {
        if (this.painter.bottom() <= 0) {
            return true;
        }
        if (this.painter.right() <= 0) {
            return true;
        }
        if (this.painter.left() >= MapInformation.getInstance().right()) {
            return true;
        }
        return this.painter.top() >= MapInformation.getInstance().bottom();
    }

    public boolean touchLeft() {
        return this.collider.left() <= 0;
    }

    public boolean touchTop() {
        return this.collider.top() <= 0;
    }

    public boolean touchRight() {
        return this.collider.right() >= MapInformation.getInstance().right();
    }

    public boolean touchBottom() {
        return this.collider.bottom() >= MapInformation.getInstance().bottom();
    }


    public boolean isExist(){
        return isExist;
    }

    public void setExist(boolean isExist){
        this.isExist = isExist;
    }

    public abstract void collisionEffect(Actor actor);

}
