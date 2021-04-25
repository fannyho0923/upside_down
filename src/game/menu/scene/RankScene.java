package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.*;
import game.utils.CommandSolver;
import game.utils.Global;
import game.utils.RankResult;
import game.utils.Ranking;

public class RankScene extends Scene {
    private Button back;
    private Button next;
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
    private Ranking ranking;
    private ArrayList<RankResult> rankResults;
    private ArrayList<RankResult> rankResults1;
    private ArrayList<RankResult> rankResults2;
    private ArrayList<RankResult> rankResults3;
    private LinkedList<ArrayList<RankResult>> allRank;
    private int currentRankPage;
    private Label title;
    private String describe;

    public RankScene() {

    }

    public RankScene(int currentRankPage) {
        this.currentRankPage = currentRankPage;
    }

    @Override
    public void sceneBegin() {
        System.out.println(currentRankPage);
        allRank = new LinkedList<ArrayList<RankResult>>();
        rankResults = new ArrayList<RankResult>();
        try {
            ranking = new Ranking("basic.txt");
        } catch (IOException e) {
            System.out.println(e);
        }
        //讀目前的排行
        ArrayList<String> arr = ranking.readL();
        if (arr.size() > 0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
            }
        }
        allRank.add(rankResults);
        //SpeedRun(1)
        rankResults1 = new ArrayList<RankResult>();
        try {
            ranking = new Ranking("speedrun.txt");
        } catch (IOException e) {
            System.out.println(e);
        }
        //讀目前的排行
        arr = ranking.readL();
        if (arr.size() > 0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults1.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
            }
        }
        allRank.add(rankResults1);
        //Parkour(2)
        rankResults2 = new ArrayList<RankResult>();
        try {
            ranking = new Ranking("parkour.txt");
        } catch (IOException e) {
            System.out.println(e);
        }
        //讀目前的排行
        arr = ranking.readL();
        if (arr.size() > 0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults2.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
            }
        }
        allRank.add(rankResults2);
        //CountDown(3)
        rankResults3 = new ArrayList<RankResult>();
        try {
            ranking = new Ranking("countdown.txt");
        } catch (IOException e) {
            System.out.println(e);
        }
        //讀目前的排行
        arr = ranking.readL();
        if (arr.size() > 0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults3.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
            }
        }
        allRank.add(rankResults3);
        rankBackground = ImageController.getInstance().tryGet("/img/background/rankBackground.png");
        Style style = new Style.StyleRect(60, 114, false, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setTextColor(Color.WHITE)
                .setHaveBorder(false)
                .setBorderColor(new Color(162, 176, 198))
                .setBorderThickness(5);
        label = new Label(100, 60, new Style.StyleRect(200, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 100))
                .setText("Rank"));
        back = new Button(50, 650 - 105, Theme.get(3));
        next = new Button(250, 650 - 105, Theme.get(13));
        back.setClickedActionPerformed((int x, int y) -> {
            if (currentRankPage == 0) {
                SceneController.getInstance().change((new MenuScene()));
            } else {
                --currentRankPage;
            }
        });
        next.setClickedActionPerformed((int x, int y) -> {
            System.out.println("bef" + currentRankPage);
            if (currentRankPage < allRank.size() - 1) {
                ++currentRankPage;
                System.out.println("aft" + currentRankPage);
            }
        });
        label1 = new Label(15, 200, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("1."));
        label2 = new Label(15, 250, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("2."));
        label3 = new Label(15, 300, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("3."));
        label4 = new Label(15, 350, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("4."));
        label5 = new Label(15, 400, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("5."));

    }

    public String getName(ArrayList<RankResult> rankResults, int index) {
        if (index < rankResults.size()) {
            return rankResults.get(index).getName();
        }
        return "no record";
    }

    public int getTime(ArrayList<RankResult> rankResults, int index) {
        if (index < rankResults.size()) {
            return rankResults.get(index).getTime();
        }
        return 0;
    }

    @Override
    public void sceneEnd() {
        title = null;
        label = null;
        back = null;
        next = null;
    }


    @Override
    public void update() {
        label = new Label(100, 60, new Style.StyleRect(100, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 90))
                .setText("Rank"));
        if (currentRankPage == 1) {
            describe = "(SpeedRun)";
        } else if (currentRankPage == 2) {
            describe = "(Parkour)";
        } else if (currentRankPage == 3) {
            describe = "   (CountDown)";
        } else {
            describe = "(Basic)";
        }
        title = new Label(320, 65, new Style.StyleRect(100, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 40))
                .setText(describe));
//name
        label6 = new Label(100, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 0)));
        label7 = new Label(100, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 1)));
        label8 = new Label(100, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 2)));
        label9 = new Label(100, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 3)));
        label10 = new Label(100, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 4)));

        //score
        label11 = new Label(200, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("" + getTime(allRank.get(currentRankPage), 0)));
        label12 = new Label(200, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("" + getTime(allRank.get(currentRankPage), 1)));
        label13 = new Label(200, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("" + getTime(allRank.get(currentRankPage), 2)));
        label14 = new Label(200, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("" + getTime(allRank.get(currentRankPage), 3)));
        label15 = new Label(200, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("" + getTime(allRank.get(currentRankPage), 4)));
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
            MouseTriggerImpl.mouseTrig(back, e, state);
            if (currentRankPage <= 3) {
                MouseTriggerImpl.mouseTrig(next, e, state);
            }
        };
    }

    @Override
    public void paint(Graphics g) {//90, 100, 650, 450
        g.drawImage(rankBackground, 0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, null);//1024,760
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
        title.paint(g);
        if (currentRankPage < 3) {
            next.paint(g);
        }
    }


}
