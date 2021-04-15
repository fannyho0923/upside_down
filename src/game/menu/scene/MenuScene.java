package game.menu.scene;

import java.awt.*;
import java.awt.event.*;

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

    //    private PopupWindowScene testPop;
    private Button button1;
    private Button button2;
    private Button rankButton;
    private Image image;
//    private SelectActorPopScene selectPop;
    private Image star;

    @Override
    public void sceneBegin() {
        image = ImageController.getInstance().tryGet("/img/menuPic.png");
        star = ImageController.getInstance().tryGet("/img/star-3.png");
//        selectPop = new SelectActorPopScene(90, 100, 650, 450);
//        testPop = new PopupWindowScene(50, 50, 650, 450);
//        selectPop.setCancelable();
//        testPop.setCancelable();
        button1 = new Button(300, 360);//430,122
        button2 = new Button(300, 430, Theme.get(1));//430,410
        rankButton = new Button(300, 500, Theme.get(2));//430,410

        button1.setClickedActionPerformed((int x, int y) -> {
//            selectPop.sceneBegin();
            image.getGraphics().drawImage(image, 0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, null);
            SceneController.getInstance().change(new SelectActorPopScene());
        });
        button2.setClickedActionPerformed((int x, int y) -> {
//            selectPop.sceneBegin();
            SceneController.getInstance().change(new SelectActorPopScene());
        });
        rankButton.setClickedActionPerformed((int x, int y) -> {
//            selectPop.sceneBegin();
            SceneController.getInstance().change(new SelectActorPopScene());
        });
    }


    @Override
    public void sceneEnd() {
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
//        if (selectPop.isShow()) {
//            selectPop.paint(g);
//        }
    }

    @Override
    public void update() {

    }

    @Override
    public MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
//            if (e != null) {
//                if (state == CommandSolver.MouseState.CLICKED) {
//                    System.out.println(e.getX() + ", " + e.getY());
//                }
//            }
            MouseTriggerImpl.mouseTrig(button1, e, state);
            MouseTriggerImpl.mouseTrig(button2, e, state);
            MouseTriggerImpl.mouseTrig(rankButton, e, state);
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
//                if (c == KeyEvent.VK_SHIFT && !editText.getIsFocus()) {
//                    if (testPop.isShow()) {
//                        testPop.hide();
//                        testPop.sceneEnd();
//                    } else {
//                        testPop.sceneBegin();
//                        testPop.show();
//                    }
//                }
            }
        };
    }
}
