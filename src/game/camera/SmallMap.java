package game.camera;

import game.gameobj.GameObject;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import game.utils.Global;
>>>>>>> 8606fae96e5ba903d449a7313015fc36f79395b4
=======
import game.utils.Global;
>>>>>>> 32444a950cc5d1f531c471d60a14b94000a8133a

import java.awt.*;

public class SmallMap extends Camera {
    private double smallMapZoomX; // 小地圖的X縮放率
    private double smallMapZoomY; // 小地圖的Y縮放率

    //顯示全圖：鏡頭大小與地圖大小相同，不要setChaseObj，並且要setCameraStartLocation將鏡頭起始位置設成(0,0)的位置
    //不顯示全圖：鏡頭大小自訂，設成要畫出來的大小，並且要setChaseObj。

    public SmallMap(final Camera camera, final Double smallMapZoomX, final Double smallMapZoomY) {
        super(camera);
        this.smallMapZoomX = smallMapZoomX;
        this.smallMapZoomY = smallMapZoomY;
        setChaseDivisorX(1);
        setChaseDivisorY(1);
    }

    /*
    以下paint為提供常用的方法overload目前除放入圖片外，皆是畫長方形，
    如果要改成其他形狀，請利用Paint介面自定義要怎麼paint。
    如果有自定義物件畫出大小時，由於鏡頭視野框線，是以該物件實際所在的中心點畫出，
    因此小地圖上的物件可能不在鏡頭中心點。
    */

    //將物件轉換成方格畫出，自動將該類型物件生成相同顏色，大小依物件縮放
    public void paint(final Graphics g, final GameObject obj) {
        final Color c = getColor("" + obj.getClass());
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(),
                obj.painter().width(), obj.painter().height());
        g.setColor(Color.BLACK);
    }

    //將物件轉換成方格畫出，自動將該類型物件生成相同顏色，大小自訂
    public void paint(final Graphics g, final GameObject obj, final int width, final int height) {
        final Color c = getColor("" + obj.getClass());
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(),
                width, height);
        g.setColor(Color.BLACK);
    }

    //將物件轉換成方格畫出，顏色自訂，大小依物件縮放
    public void paint(final Graphics g, final GameObject obj, final Color c) {
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(),
                obj.painter().width(), obj.painter().height());
        g.setColor(Color.BLACK);
    }

    //將物件轉換成方格畫出，顏色、大小自訂
    public void paint(final Graphics g, final GameObject obj, final Color c, final int width, final int height) {
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(),
                width, height);
        g.setColor(Color.BLACK);
    }

    // 小地圖的物件顯示圖片，大小依物件縮放
    public void paint(final Graphics g, final GameObject obj, final Image img) {
        g.drawImage(img, obj.painter().left(), obj.painter().top(),
                obj.painter().width(), obj.painter().height(), null);
    }

    // 小地圖的物件顯示圖片，大小自訂
    public void paint(final Graphics g, final GameObject obj, final Image img, final int width, final int height) {
        g.drawImage(img, obj.painter().left(), obj.painter().top(), width, height, null);
    }

    //畫追蹤物件的鏡頭框大小，camera放入追蹤物件的主鏡頭，即可在小地圖上顯示目前主鏡頭可見的範圍。
    public void paint(final Graphics g, final Camera camera, final Color color) {
        g.setColor(color);
        g.drawRect(camera.painter().left(), camera.painter().top(),
                camera.painter().width(), camera.painter().height());
        g.setColor(Color.BLACK);
    }

    /*使用時，請在場景的paint方法中
    1.smallMap.start(g) //將畫布移動到您的顯示視窗範圍(0,0)
    2.放入您的物件(請讓每個物件與smallMap做isCollision碰撞判斷，有重疊到才paint)
    EX: if(smallMap.isCollision(ac)){  // 如果只要主鏡頭內才顯示，就把smallMap 改成主鏡頭
            smallMap.paint(g,ac);
        }
    3. smallMap.end(g) 將畫布移回原位
    4. 如果有第二顆camera 再次操作 1 ~ 3。
    */
    @Override
    public void start(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g; //Graphics2D 為Graphics的子類，先做向下轉型。
        this.tmpCanvas = g2d.getTransform(); // 暫存畫布
        g2d.scale(this.smallMapZoomX, this.smallMapZoomY);  // 將畫布整體做縮放 ( * 縮放的係數)
        // 先將畫布初始移到(0,0) 然後再到自己想定位的地點(+ cameraWindowX. Y)，因為有被縮放的話要將為位移點調整-> (/ 縮放的係數)
        g2d.translate((-this.painter().left() + cameraWindowX() / this.smallMapZoomX), (-this.painter().top() + cameraWindowY() / this.smallMapZoomY));
        // 將畫布依照鏡頭大小作裁切
        g.setClip(this.painter().left(), this.painter().top(), this.painter().width(), this.painter().height());
    }

    public Color getColor(final String str) {
        int colorCode = 0;
        final char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            colorCode += arr[i];
        }
        final int[] colorCodeArr = new int[3];
        final int[] temp = {colorCode % 255, colorCode % 175, colorCode % 90}; // 裝可能性，將得到的數字分不同參數做mod運算，以獲得差異較大的色碼
        for (int i = 0; i < 2; i++) {
            colorCodeArr[i] = temp[i];
        }
        return new Color(colorCodeArr[0], colorCodeArr[1], colorCodeArr[2]);
    }

    public void setSmallMapZoomX(final double num) {
        this.smallMapZoomX = num;
    }

    public double smallMapZoomX() {
        return this.smallMapZoomX;
    }

    public void setSmallMapZoomY(final double num) {
        this.smallMapZoomY = num;
    }

    public double smallMapZoomY() {
        return this.smallMapZoomY;
    }

    public interface Paint { // 可以使用這個接口自定義paint 的方法
        void paint(Graphics g, GameObject obj);
    }
}





