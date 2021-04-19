package game.menu.scene;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
    private ArrayList<Button> buttonArrayList;

    @Override
    public void sceneBegin() {
        buttonArrayList = new ArrayList<>();
        AudioResourceController.getInstance().play("/sound/Epilogue.wav");
        image = ImageController.getInstance().tryGet("/img/menuPic.png");
        star = ImageController.getInstance().tryGet("/img/star-3.png");

        button1 = new Button(300, 360);//430,122
        button2 = new Button(300, 430, Theme.get(1));//430,410
        rankButton = new Button(300, 500, Theme.get(2));//430,410

        buttonArrayList.add(button1);
        buttonArrayList.add(button2);
        buttonArrayList.add(rankButton);

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

            buttonArrayList.forEach(Button::playTabSound);
            buttonArrayList.forEach(Button::playConfirm);


            playConfirm(button1);
            playConfirm(button2);
            playConfirm(rankButton);

//            if (selectPop.isShow()) {
//                selectPop.mouseListener().mouseTrig(e, state, trigTime);
//            }
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
