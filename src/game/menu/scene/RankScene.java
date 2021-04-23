package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;
import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.*;
import game.utils.CommandSolver;
import game.utils.Global;

public class RankScene extends Scene {
    private Button b1;
    private Button back;
    private Image rankBackground;
    private Label label;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private Label label6;
    private Label label7;
    private Label label8;
    private Label label9;
    private Label label10;
    private Label label11;
    private Label label12;
    private Label label13;
    private Label label14;
    private Label label15;
    private int score1=20;
    private int score2=39;
    private int score3=28;
    private int score4=28;
    private int score5=75;
    private String name1 = "fanny";
    private String name2 = "amy";
    private String name3 = "amy";
    private String name4 = "amy";
    private String name5 = "amy";


    public RankScene(){

    }

    @Override
    public void sceneBegin() {
        rankBackground = ImageController.getInstance().tryGet("/img/background/rankBackground.png");
        Style style = new Style.StyleRect(60, 114, false, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setTextColor(Color.WHITE)
                .setHaveBorder(false)
                .setBorderColor(new Color(162, 176, 198))
                .setBorderThickness(5);

        b1 = new Button(90 , 30, Theme.get(4));
        label=new Label(100, 60, new Style.StyleRect(200, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 100))
                .setText("Rank"));
        back = new Button(50, 650 - 105, Theme.get(3));
//        b1.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new BasicScene));
        back.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change((new MenuScene())));
        label1=new Label(15, 200, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("1."));
        label2=new Label(15, 250, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("2."));
        label3=new Label(15, 300, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("3."));
        label4=new Label(15, 350, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("4."));
        label5=new Label(15, 400, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("5."));
        label6=new Label(100, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(name1));
        label7=new Label(100, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(name2));
        label8=new Label(100, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(name3));
        label9=new Label(100, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(name4));
        label10=new Label(100, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(name5));
        label11=new Label(200, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(""+score1));
        label12=new Label(200, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(""+score2));
        label13=new Label(200, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(""+score3));
        label14=new Label(200, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(""+score4));
        label15=new Label(200, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(""+score5));

    }

    @Override
    public void sceneEnd() {
        b1=null;
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

        };
    }

    @Override
    public void paint(Graphics g) {//90, 100, 650, 450
        g.drawImage(rankBackground, 0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, null);//1024,760
//        b1.paint(g);
        label.paint(g);
        label1.paint(g);
        label2.paint(g);
        label3.paint(g);
        label4.paint(g);
        label5.paint(g);
        label6.paint(g);
        label7.paint(g);
        label8.paint(g);
        label9.paint(g);
        label10.paint(g);
        label11.paint(g);
        label12.paint(g);
        label13.paint(g);
        label14.paint(g);
        label15.paint(g);
        back.paint(g);
    }


}
