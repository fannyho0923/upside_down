package game.gameobj;

import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Tile extends GameObject{
    public static enum Type{
        tile_0195("/img/gameObj/tile/tile_0195.png"),
        tile_0196("/img/gameObj/tile/tile_0196.png"),
        tile_0197("/img/gameObj/tile/tile_0197.png"),
        tile_0198("/img/gameObj/tile/tile_0198.png"),
        tile_0215("/img/gameObj/tile/tile_0215.png"),
        tile_0235("/img/gameObj/tile/tile_0235.png"),
        tile_0236("/img/gameObj/tile/tile_0236.png"),
        tile_0237("/img/gameObj/tile/tile_0237.png"),
        tile_0238("/img/gameObj/tile/tile_0238.png"),
        tile_0255("/img/gameObj/tile/tile_0255.png"),
        tile_0256("/img/gameObj/tile/tile_0256.png"),
        tile_0257("/img/gameObj/tile/tile_0257.png"),
        tile_999("/img/gameObj/tile/tile_999.png");

        private Image img;
        Type(String path){
            img = ImageController.getInstance().tryGet(path);
        }
    }

    private Type type;

    public Tile(int top, int left, Type type) {
        super(top, left, Global.UNIT, Global.UNIT);
        this.type = type;
    }

    @Override
    public void collisionEffect(Actor actor) {
        actor.beBlock(this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
