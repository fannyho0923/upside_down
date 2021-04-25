package game.menu.scene;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.controller.SceneController;
import game.gameobj.StarObj;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.Scene;
import game.utils.CommandSolver;
import game.utils.CommandSolver.MouseListener;
import game.utils.Global;
/**
 * @author Fanny
 */

public class MenuScene extends Scene {
    private Label label;
    private Button button1;
    private Button rankButton;
    private Image image;
    private boolean isPlayed1;
    private boolean isPlayed3;
    private ArrayList<StarObj> starObj;
    private Image astronaut;
    private Image ast1;
    private Image ast2;

    @Override
    public void sceneBegin() {
        isPlayed1 = false;
        isPlayed3 = false;
        AudioResourceController.getInstance().play("/sound/Epilogue.wav");
        image = ImageController.getInstance().tryGet("/img/background/menu419.png");
        astronaut = ImageController.getInstance().tryGet("/img/background/astronaut.png");
        ast1 = ImageController.getInstance().tryGet("/img/background/ast1.png");
        ast2 = ImageController.getInstance().tryGet("/img/background/ast2.png");
        label = new Label(380, 100, new Style.StyleRect(200, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.ITALIC, 100))
                .setText("Upside Down")
                .setTextColor(new Color(248, 238, 62, 190))
        );
        button1 = new Button(Global.WINDOW_WIDTH / 2 - 100, 400);
        rankButton = new Button(Global.WINDOW_WIDTH / 2 - 100, 480, Theme.get(2));

        button1.setClickedActionPerformed((int x, int y) -> {
            SceneController.getInstance().change(new SelectActorScene());
        });
        rankButton.setClickedActionPerformed((int x, int y) -> {
            SceneController.getInstance().change(new RankScene());
        });
        starObj = new ArrayList<>();
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/Epilogue.wav");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, Global.WINDOW_WIDTH + 35, Global.WINDOW_HEIGHT, null);//1024,760
        g.drawImage(ast1, button1.right() + 20, button1.getY() , null);
        g.drawImage(ast1, rankButton.right() + 20, rankButton.getY() , null);
        if (button1.getIsHover()) {
            g.drawImage(ast2, button1.right() + 20, button1.getY() , null);
        }
        if (rankButton.getIsHover()) {
            g.drawImage(ast2, rankButton.right() + 20, rankButton.getY() , null);
        }
        label.paint(g);
        for (int i = 0; i < starObj.size(); i++) {
            starObj.get(i).paint(g);
        }
        button1.paint(g);
        rankButton.paint(g);
        if(button1.getIsHover()||rankButton.getIsHover()){
            g.drawImage(astronaut,Global.WINDOW_WIDTH/2+120,175,null);
        }
    }

    @Override
    public void update() {
        if (Global.random(1, 100) <= 10) {
            starObj.add(new StarObj(Global.random(0, 960), Global.random(320, 640), 5, 5));
        }
        for (int i = 0; i < starObj.size(); i++) {
            starObj.get(i).update();
            if (!starObj.get(i).isExist()) {
                starObj.remove(i);
                i--;
                continue;
            }
        }
    }

    @Override
    public MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {

            MouseTriggerImpl.mouseTrig(button1, e, state);
            MouseTriggerImpl.mouseTrig(rankButton, e, state);

            playConfirm(button1);
            playConfirm(rankButton);

            if ((button1.getIsHover()) && (!isPlayed1)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed1 = true;
            }
            if (!button1.getIsHover()) {
                isPlayed1 = false;
            }
            if ((rankButton.getIsHover()) && (!isPlayed3)) {
                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed3 = true;
            }
            if (!rankButton.getIsHover()) {
                isPlayed3 = false;
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

    public void playConfirm(Button button) {
        if (button.getIsFocus()) {
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }
}
