package game.utils;

import game.controller.ImageController;

import java.awt.*;

public class NumberFig implements GameKernel.PaintInterface{

    private int num;
    private int UNIT = 128;
    private int x;
    private int y;
    private static Image img = ImageController.getInstance().tryGet("/img/num128.png");

    public NumberFig(int x, int y, int num){
        this.x = x;
        this.y = y;
        this.num = num;
    }
    public void setNum(int num){
        this.num = num;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,x,y,x+UNIT,y+UNIT,
                (num%5)*UNIT,(num/5)*UNIT,((num%5)*UNIT)+UNIT,((num/5)*UNIT)+UNIT,null);
    }
}
