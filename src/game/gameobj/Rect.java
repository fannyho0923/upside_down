package game.gameobj;

public class Rect {
    private int width;
    private int height;
    private int x; //(x,y)為長方形的左上角座標
    private int y;
    //左上角(x,y)開始生成矩形
    public Rect(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect(final Rect rect) {
        this.x = rect.left();
        this.y = rect.top();
        this.width = rect.width();
        this.height = rect.height();
    }

    //以中心點(x,y開始生成)
    public static Rect genWithCenter(final int x, final int y, final int width, final int height) {
        return new Rect(x - width / 2, y - height / 2, width, height);
    }

    public int left() {
        return this.x;
    }

    public int top() {
        return this.y;
    }

    public int right() {
        return this.x + this.width;
    }

    public int bottom() {
        return this.y + this.height;
    }

    public boolean isOverlap(final Rect rect) {
        if (this.left() > rect.right()) {
            return false;
        }
        if (this.right() < rect.left()) {
            return false;
        }
        if (this.top() > rect.bottom()) {
            return false;
        }
        if (this.bottom() < rect.top()) {
            return false;
        }
        return true;
    }

    public int centerX() {
        return (left() + right()) / 2;
    }

    public int centerY() {
        return (top() + bottom()) / 2;
    }

    public float exactCenterX() {
        return (left() + right()) / 2f;
    }

    public float exactCenterY() {
        return (top() + bottom()) / 2f;
    }

    public void offset(final int x, final int y) {
        offsetX(x);
        offsetY(y);
    }

    public void offsetX(final int x) {
        this.x += x;
    }

    public void offsetY(final int y) {
        this.y += y;
    }

    public void setXY(final int x, final int y) {
        setX(x);
        setY(y);
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int width() {
        return this.width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void offsetWidth(int dx){
        this.width += dx;
    }

    public int height() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void offsetHeight(int dy){
        this.height += dy;
    }

    public final void setCenter(final int x, final int y) {
        offset(x - centerX(), y - centerY());
    }

}
