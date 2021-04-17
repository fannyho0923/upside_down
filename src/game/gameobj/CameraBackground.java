package game.gameobj;

import game.controller.ImageController;

import java.awt.*;

public class CameraBackground extends GameObject{
    private Type type;

    public enum Type{
        A1("/img/gameObj/background/back1.jpg"),
        B1("/img/gameObj/background/back2.jpg"),
        C1("/img/gameObj/background/back3.jpg"),
        C2("/img/gameObj/background/back3-2.jpg"),
        C3("/img/gameObj/background/back3-3.jpg"),
        C4("/img/gameObj/background/back3-4.jpg"),
        D1("/img/gameObj/background/back4-1.jpg"),
        D2("/img/gameObj/background/back4-2.jpg"),
        D3("/img/gameObj/background/back4-3.jpg"),
        D4("/img/gameObj/background/back4-4.jpg"),
        E1("/img/gameObj/background/back5.jpg"),
        F1("/img/gameObj/background/back6.jpg"),
        G1("/img/gameObj/background/back11.jpg"),
        H1("/img/gameObj/background/back12.jpg");
        private Image img;
        Type(String path){
            img = ImageController.getInstance().tryGet(path);
        }
    }

    public CameraBackground(int left, int top, Type type) {
        super(left, top, 960, 640);
        this.type = type;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img,painter().left(),painter().top(),null);
    }

    @Override
    public void update() {

    }
}
