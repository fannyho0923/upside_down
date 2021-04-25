package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;
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
    private Image modeBackground;

    public ModeScene(int actorNum){
        this.actorNum=actorNum;
    }

    @Override
    public void sceneBegin() {
        modeBackground = ImageController.getInstance().tryGet("/img/background/mode.jpg");
        Style style = new Style.StyleRect(60, 114, false, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setTextColor(Color.WHITE)
                .setHaveBorder(false)
                .setBorderColor(new Color(162, 176, 198))
                .setBorderThickness(5);

        b1 = new Button(90 , 30, Theme.get(4));
        b2 = new Button(380 , 30, Theme.get(5));
        b3 = new Button(670 , 30, Theme.get(6));
        b4 = new Button(235 , 280, Theme.get(7));
        b5 = new Button(525 , 280, Theme.get(8));
        back = new Button(800, 650 - 105, Theme.get(14));
        b1.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new BasicScene(actorNum)));
        b2.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new SpeedRun(actorNum)));
        b3.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new Parkour(actorNum)));
        b4.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new CountDown(actorNum)));
        b5.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new BasicScene(actorNum)));
        back.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change((new SelectActorScene())));

    }

    @Override
    public void sceneEnd() {
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


}
