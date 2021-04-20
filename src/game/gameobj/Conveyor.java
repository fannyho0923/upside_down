package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Global;

import java.awt.*;

public class Conveyor extends GameObject{
    // 另外載入, 不要放在GameObj Arr, 鏡頭轉換時要重新創建

    private static final int SHIFT = 1;
    private Type type;

    public enum Type{
        TopL1("/img/gameObj/conveyor/conveyor_top_L1.png",Global.Direction.LEFT),
        TopL2("/img/gameObj/conveyor/conveyor_top_L2.png",Global.Direction.LEFT),
        TopL3("/img/gameObj/conveyor/conveyor_top_L3.png",Global.Direction.LEFT),
        DownL1("/img/gameObj/conveyor/conveyor_down_L1.png",Global.Direction.LEFT),
        DownL2("/img/gameObj/conveyor/conveyor_down_L2.png",Global.Direction.LEFT),
        DownL3("/img/gameObj/conveyor/conveyor_down_L3.png",Global.Direction.LEFT),
        TopR1("/img/gameObj/conveyor/conveyor_top_R1.png",Global.Direction.RIGHT),
        TopR2("/img/gameObj/conveyor/conveyor_top_R2.png",Global.Direction.RIGHT),
        TopR3("/img/gameObj/conveyor/conveyor_top_R3.png",Global.Direction.RIGHT),
        DownR1("/img/gameObj/conveyor/conveyor_down_R1.png",Global.Direction.RIGHT),
        DownR2("/img/gameObj/conveyor/conveyor_down_R2.png",Global.Direction.RIGHT),
        DownR3("/img/gameObj/conveyor/conveyor_down_R3.png",Global.Direction.RIGHT);

        private Image img;
        private Global.Direction dir;
        Type(String path,Global.Direction dir){
            this.img = ImageController.getInstance().tryGet(path);
            this.dir = dir;
        }
    }

    public Conveyor(int left, int top, Type type) {
        super(left, top, Global.UNIT, Global.UNIT);
        this.type = type;
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/conveyor-crop.wav");
        actor.beBlock(this);
        if(type.dir == Global.Direction.LEFT){
            actor.offsetX(-SHIFT);
        }else if(type.dir == Global.Direction.RIGHT){
            actor.offsetX(SHIFT);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(type.img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}