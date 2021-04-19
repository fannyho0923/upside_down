package game.menu.menu;

import game.controller.AudioResourceController;

import java.awt.Graphics;
import java.util.function.Consumer;

public class Button extends Label {

    //0416 Anne新增isPlayed屬性，存取音效撥放狀態
    private boolean isPlayed;

    public Button(int x, int y, Style style) {
        super(x, y, style);

    }

    public Button(int x, int y, Theme theme) {
        super(x, y, theme);

    }

    public Button(int x, int y) {
        super(x, y);
    }

    @Override
    public void paint(Graphics g) {
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paint(g, super.getX(), super.getY());
        }
    }

    @Override
    public void update() {

    }
<<<<<<< HEAD
=======

    //Anne新增音效方法
    public void played() {
        isPlayed = true;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void resetPlay(){
        isPlayed = false;
    }

    public void playTabSound(){
        if (getIsHover()&&(!isPlayed())){
            AudioResourceController.getInstance().shot("/sound/tab.wav");
            played();
        }
        if (!getIsHover()){
            resetPlay();
        }
    }

    public void playConfirm(){
        if (getIsFocus()){
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }

>>>>>>> b886f29db8da8d6047141b5affe7b24264f02f8f
}
