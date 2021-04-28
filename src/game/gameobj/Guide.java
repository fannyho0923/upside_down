package game.gameobj;

import game.controller.ImageController;
import game.scene.GameScene;
import game.utils.Global;

import java.awt.*;

public class Guide extends GameObject{
    public enum Type{
        brokenRoad("/img/gameObj/guide/brokenRoad.png"),
        conveyor("/img/gameObj/guide/conv.png"),
        dirKey("/img/gameObj/guide/dir.png"),
        ESC("/img/gameObj/guide/ESC.png"),
        monster("/img/gameObj/guide/monster.png"),
        pass("/img/gameObj/guide/pass.png"),
        rubber("/img/gameObj/guide/rubber.png"),
        save("/img/gameObj/guide/save.png"),
        space("/img/gameObj/guide/space.png");

        private Image img;
        Type(String path){
            this.img = ImageController.getInstance().tryGet(path);
        }
    }

    private Type type;
    public Guide(int x, int y, Type type){
        super(x,y, Global.UNIT,Global.UNIT);
        this.type = type;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img,
                GameScene.getFrameX_count()*960+200,
                GameScene.getFrameY_count()*640+256,
                null);
    }

    @Override
    public void update() {
    }
}
