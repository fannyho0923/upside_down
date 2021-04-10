//package game.gameobj;
//
//import java.awt.*;
//
//public class Button extends GameObject {
//    private Image img;
//
//    public Button(int x, int y, Image img) {
//        super(x, y, 10, 10);
//        this.img = img;
//    }
//
//    public boolean state(Point point) {
//        return (point.getX() >= this.painter().left())
//                && (point.getX() <= this.painter().right())
//                && (point.getY() < this.painter().bottom())
//                && (point.getY() > this.painter().top());
//    }
//
//    @Override
//    public void CollisionEffect(GameObject gameObject) {
//
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        g.drawImage(img, this.painter().left(), this.painter().top(), null);
//    }
//
//    @Override
//    public void update() {
//
//    }
//}
