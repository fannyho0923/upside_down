package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.*;
import game.utils.CommandSolver;

public class ModeScene extends Scene {
    private int actorNum;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button back;
    private boolean isPlayed1;
    private boolean isPlayed2;
    private boolean isPlayed3;
    private boolean isPlayed4;
    private boolean isPlayed5;
    private boolean isPlayedBack;
    private Image modeBackground;

    public ModeScene(int actorNum){
        this.actorNum=actorNum;
    }

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().loop("/sound/Prologue.wav", 50);
        modeBackground = ImageController.getInstance().tryGet("/img/background/mode.jpg");
        Style style = new Style.StyleRect(60, 114, false, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setTextColor(Color.WHITE)
                .setHaveBorder(false)
                .setBorderColor(new Color(162, 176, 198))
                .setBorderThickness(5);

        b1 = new Button(235 , 30, Theme.get(7));
        b2 = new Button(525 , 30, Theme.get(8));
        b3 = new Button(90 , 280, Theme.get(4));
        b4 = new Button(380 , 280, Theme.get(5));
        b5 = new Button(670 , 280, Theme.get(6));
        back = new Button(800, 650 - 105, Theme.get(14));
        b1.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change(new BasicScene(actorNum));
                GameScene.setIsStory(false);
                });
        b2.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change(new BasicScene(actorNum));
                GameScene.setIsStory(true);
                });
        b3.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change(new SpeedRun(actorNum));
                GameScene.setIsStory(false);
                });
        b4.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change(new Parkour(actorNum));
                GameScene.setIsStory(false);
                });
        b5.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change(new CountDown(actorNum));
                GameScene.setIsStory(false);
                });
        back.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change((new SelectActorScene())));
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/Prologue.wav");

        b1=null;
        b2=null;
        b3=null;
        b4=null;
        b5=null;
        back = null;
    }


    @Override
    public void update() {
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


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(b1, e, state);
            MouseTriggerImpl.mouseTrig(back, e, state);
            MouseTriggerImpl.mouseTrig(b2, e, state);
            MouseTriggerImpl.mouseTrig(b3, e, state);
            MouseTriggerImpl.mouseTrig(b4, e, state);
            MouseTriggerImpl.mouseTrig(b5, e, state);

            playConfirm(b1);
            playConfirm(b2);
            playConfirm(b3);
            playConfirm(b4);
            playConfirm(b5);
            playConfirm(back);

            if ((b1.getIsHover()) && (!isPlayed1)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed1 = true;
            }
            if (!b1.getIsHover()) {
                isPlayed1 = false;
            }

            if ((b2.getIsHover()) && (!isPlayed2)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed2 = true;
            }
            if (!b2.getIsHover()) {
                isPlayed2 = false;
            }

            if ((b3.getIsHover()) && (!isPlayed3)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed3 = true;
            }
            if (!b3.getIsHover()) {
                isPlayed3 = false;
            }
            if ((b4.getIsHover()) && (!isPlayed4)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed4 = true;
            }
            if (!b4.getIsHover()) {
                isPlayed4 = false;
            }
            if ((b5.getIsHover()) && (!isPlayed5)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayed5 = true;
            }
            if (!b5.getIsHover()) {
                isPlayed5 = false;
            }
            if ((back.getIsHover()) && (!isPlayedBack)) {

                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayedBack = true;
            }
            if (!back.getIsHover()) {
                isPlayedBack = false;
            }
        };
    }

    @Override
    public void paint(Graphics g) {//90, 100, 650, 450
        g.drawImage(modeBackground, 0, 0, 960, 640, null);//1024,760
        b1.paint(g);
        b2.paint(g);
        b3.paint(g);
        b4.paint(g);
        b5.paint(g);
        back.paint(g);
    }

    public void playConfirm(Button button){
        if (button.getIsFocus()){
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }


}
