package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.Scene;
import game.utils.CommandSolver;
import game.utils.Delay;
import game.utils.Global;

public class SelectActorScene extends Scene {

    private Button start;
    private Button back;
    private Image bk;
    private Label a1;
    private Label a2;
    private Label a3;
    private Label a4;
    private Label a5;
    private Label a6;
    private Image actor1;
    private Image actor2;
    private Image actor3;
    private Image actor4;
    private Image actor5;
    private Image actor6;
    private Image menuBackground;
    private Label label;
    private Image star;

    private ActionAnimator actionAnimator;
    private ArrayList<Label> arrayList = new ArrayList<>();

    @Override
    public void sceneBegin() {
        star = ImageController.getInstance().tryGet("/img/starButton.png");
        menuBackground = ImageController.getInstance().tryGet("/img/background/actorbackground.png");
        bk = ImageController.getInstance().tryGet("/img/bk.png");
        actor1 = ImageController.getInstance().tryGet("/img/actor/actorFront1.png");
        actor2 = ImageController.getInstance().tryGet("/img/actor/actorFront2.png");
        actor3 = ImageController.getInstance().tryGet("/img/actor/actorFront3.png");
        actor4 = ImageController.getInstance().tryGet("/img/actor/actorFront4.png");
        actor5 = ImageController.getInstance().tryGet("/img/actor/actorFront5.png");
        actor6 = ImageController.getInstance().tryGet("/img/actor/actorFront6.png");
        Style style = new Style.StyleRect(60, 114, false, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setTextColor(Color.WHITE)
                .setHaveBorder(true)
                .setBorderColor(new Color(162, 176, 198))
                .setBorderThickness(5);
        a1 = new Label(255, 422, style);
        a2 = new Label(335, 422, style);
        a3 = new Label(415, 422, style);
        a4 = new Label(495, 422, style);
        a5 = new Label(575, 422, style);
        a6 = new Label(655, 422, style);
        arrayList.add(a1);
        arrayList.add(a2);
        arrayList.add(a3);
        arrayList.add(a4);
        arrayList.add(a5);
        arrayList.add(a6);
        label = new Label(385, 320, new Style.StyleRect(200, 80, new BackgroundType.BackgroundNull())
                .setText("CHOOSE and CLICK the Actor"));

        actionAnimator = new ActionAnimator();//90, 100, 650, 450
        start = new Button(480 + 125, 650 - 130, Theme.get(0));//Button(bk.getX() + getWidth() - 150, this.getY() + getHeight() - 130, Theme.get(0))
        back = new Button(480 - 200, 650 - 105, Theme.get(14));
        start.setClickedActionPerformed((int x, int y) -> {
            for (int i = 1; i <= arrayList.size(); i++) {
                if (arrayList.get(i-1).getIsFocus()) {

                    SceneController.getInstance().change(new ModeScene(i));
//                    SceneController.getInstance().change(new Parkour(i));

                    return;
                }
            }
        });
        back.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new MenuScene()));

    }

    @Override
    public void sceneEnd() {
        start = null;
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
            MouseTriggerImpl.mouseTrig(start, e, state);
            MouseTriggerImpl.mouseTrig(back, e, state);
            MouseTriggerImpl.mouseTrig(a1, e, state);
            MouseTriggerImpl.mouseTrig(a2, e, state);
            MouseTriggerImpl.mouseTrig(a3, e, state);
            MouseTriggerImpl.mouseTrig(a4, e, state);
            MouseTriggerImpl.mouseTrig(a5, e, state);
            MouseTriggerImpl.mouseTrig(a6, e, state);
        };
    }

    @Override
    public void paint(Graphics g) {//90, 100, 650, 450
        g.drawImage(menuBackground, 0, 0, 1024, 760, null);//1024,760
//        g.drawImage(bk, 200, 280, 570, 350, null);//bk, 90, 100, 650, 450, null
        label.paint(g);
        start.paint(g);
        back.paint(g);
        actionAnimator.paintWoman(g, actor1, 255, 422, 255 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a1.getIsHover() || a1.getIsFocus()));
        actionAnimator.paintWoman(g, actor2, 335, 422, 335 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a2.getIsHover() || a2.getIsFocus()));
        actionAnimator.paintWoman(g, actor3, 415, 422, 415 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a3.getIsHover() || a3.getIsFocus()));
        actionAnimator.paintMan(g, actor4, 495, 422, 495 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a4.getIsHover() || a4.getIsFocus()));
        actionAnimator.paintMan(g, actor5, 575, 422, 575 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a5.getIsHover() || a5.getIsFocus()));
        actionAnimator.paintMan(g, actor6, 655, 422, 655 + Global.UNIT_X64, 472 + Global.UNIT_Y64, (a6.getIsHover() || a6.getIsFocus()));
        if (a1.getIsFocus()) {
            g.drawImage(star, a1.left()+30-14, a1.top()- 32, null);
        }if (a2.getIsFocus()) {
            g.drawImage(star, a2.left()+30-15, a2.top()- 32, null);
        }if (a3.getIsFocus()) {
            g.drawImage(star, a3.left()+30-15, a3.top()- 32, null);
        }if (a4.getIsFocus()) {
            g.drawImage(star, a4.left()+30-15, a4.top()- 32, null);
        }if (a5.getIsFocus()) {
            g.drawImage(star, a5.left()+30-15, a5.top()- 32, null);
        }if (a6.getIsFocus()) {
            g.drawImage(star, a6.left()+30-16, a6.top()- 32, null);
        }
    }

    public void playConfirm(Label label){
        if (label.getIsFocus()){
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }

    public void playConfirm(Button button){
        if (button.getIsFocus()){
            AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
        }
    }

    private static class ActionAnimator {
        private int count;
        private Delay delay;

        public ActionAnimator() {
            count = 0;
            delay = new Delay(10);
            delay.loop();
        }


        public void paintWoman(Graphics g, Image img, int left, int top, int right, int bottom, boolean isChoose) {
            if (isChoose) {
                if (delay.count()) {
                    count = ++count % 7;
                }
                g.drawImage(img, left, top, right, bottom,
                        count * Global.UNIT_X64 + 22,
                        14,
                        count * Global.UNIT_X64 + Global.UNIT_X64 - 22,
                        Global.UNIT_Y64 - 16, null);
            } else {
                g.drawImage(img, left, top, right, bottom,
                        4*Global.UNIT_X64 + 22,
                        14,
                        4*Global.UNIT_X64 + Global.UNIT_X64 - 22,
                        Global.UNIT_Y64 - 16, null);
            }
        }

        public void paintMan(Graphics g, Image img, int left, int top, int right, int bottom, boolean isChoose) {
            if (isChoose) {
                if (delay.count()) {
                    count = ++count % 6;
                }

                g.drawImage(img, left, top, right, bottom,
                        count * Global.UNIT_X64 + 22,
                        14,
                        count * Global.UNIT_X64 + Global.UNIT_X64 - 22,
                        Global.UNIT_Y64 - 16, null);
            } else {
                g.drawImage(img, left, top, right, bottom,
                        Global.UNIT_X64 + 22,
                        14,
                        Global.UNIT_X64 + Global.UNIT_X64 - 22,
                        Global.UNIT_Y64 - 16, null);
            }
        }
    }
}