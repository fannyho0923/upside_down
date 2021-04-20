package game.menu.scene;

import java.awt.*;
import java.awt.event.*;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.Scene;
import game.utils.CommandSolver;
import game.utils.CommandSolver.MouseListener;
import game.utils.Global;

public class MenuScene extends Scene {

    private Button button1;
    private Button button2;
    private Button rankButton;
    private Image image;

    private Image star;
    private boolean isPlayed1;
    private boolean isPlayed2;
    private boolean isPlayed3;


    @Override
    public void sceneBegin() {
        isPlayed1=false;
        isPlayed2=false;
        isPlayed3=false;
        AudioResourceController.getInstance().play("/sound/Epilogue.wav");
        image = ImageController.getInstance().tryGet("/img/menuPic.png");
        star = ImageController.getInstance().tryGet("/img/star-3.png");

        button1 = new Button(300, 360);//430,122
        button2 = new Button(300, 430, Theme.get(1));//430,410
        rankButton = new Button(300, 500, Theme.get(2));//430,410

        button1.setClickedActionPerformed((int x, int y) -> {
            image.getGraphics().drawImage(image, 0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, null);
            SceneController.getInstance().change(new SelectActorPopScene());
        });
        button2.setClickedActionPerformed((int x, int y) -> {
            SceneController.getInstance().change(new SelectActorPopScene());
        });
        rankButton.setClickedActionPerformed((int x, int y) -> {
            SceneController.getInstance().change(new SelectActorPopScene());
        });
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/Epilogue.wav");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, 1024, 760, null);//1024,760
        if (button1.getIsHover()) {
            g.drawImage(star, button1.right() + 20, button1.getY() + 10, null);
        }
        if (button2.getIsHover()) {
            g.drawImage(star, button2.right() + 20, button2.getY() + 10, null);
        }
        if (rankButton.getIsHover()) {
            g.drawImage(star, rankButton.right() + 20, rankButton.getY() + 10, null);
        }
        button1.paint(g);
        button2.paint(g);
        rankButton.paint(g);
    }

    @Override
    public void update() {

    }

    @Override
    public MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {

            MouseTriggerImpl.mouseTrig(button1, e, state);
            MouseTriggerImpl.mouseTrig(button2, e, state);
            MouseTriggerImpl.mouseTrig(rankButton, e, state);

            playConfirm(button1);
            playConfirm(button2);
            playConfirm(rankButton);

            if((button1.getIsHover())&&(!isPlayed1)){
                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed1=true;
            }
            if (!button1.getIsHover()){
                isPlayed1=false;
            }

            if((button2.getIsHover())&&(!isPlayed2)){
                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed2=true;
            }
            if (!button2.getIsHover()){
                isPlayed2=false;
            }

            if((rankButton.getIsHover())&&(!isPlayed3)){
                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed3=true;
            }
            if (!rankButton.getIsHover()){
                isPlayed3=false;
            }

        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    public void playConfirm(Button button){
        if (button.getIsFocus()){
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }
}
