package game.gameobj;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.utils.Velocity;

import java.awt.*;

public class Conveyor extends GameObject{
    // 另外載入, 不要放在GameObj Arr, 鏡頭轉換時要重新創建

    private Image img;
    private int shift_x;
    private int num; //畫頭身尾
    private int dir;
    public Conveyor(int top, int left, int width, int height, int shift_x, int dir ,int num) {
        super(top, left, width, height);
        this.shift_x = shift_x;
        this.num = num;
        this.dir = dir;
    }

    @Override
    public void collisionEffect(Actor actor) {
        AudioResourceController.getInstance().play("/sound/conveyor-crop.wav");
        actor.beBlock(this);
        if (dir == 1) {
            actor.offsetX(shift_x);
        }else{
            actor.offsetX(-shift_x);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    public Image getImage(){
        if (dir == 1) {
            switch (num) {
                case 1:
                    img = ImageController.getInstance().tryGet("/img/tile_0124.png");
                    break;
                case 2:
                    img = ImageController.getInstance().tryGet("/img/tile_0125.png");
                    break;
                case 3:
                    img = ImageController.getInstance().tryGet("/img/tile_0126.png");
                    break;
            }
        }else{
                switch (num){
                    case 1:
                        img = ImageController.getInstance().tryGet("/img/tile_0126_R.png");
                        break;
                    case 2:
                        img = ImageController.getInstance().tryGet("/img/tile_0125_R.png");
                        break;
                    case 3:
                        img = ImageController.getInstance().tryGet("/img/tile_0124_R.png");
                        break;
                }
            }
        return img;
        }
}