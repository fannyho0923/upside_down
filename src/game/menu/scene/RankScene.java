package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import game.controller.AudioResourceController;
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
    private boolean isPlayedNext;
    private boolean isPlayedBack;
    private boolean isConfirmNext;
    private boolean isConfirmBack;
    private boolean isFromGame;
    private Button backToGame;

    public RankScene() {

    }

    public RankScene(int currentRankPage, boolean isFromGame) {
        this.currentRankPage = currentRankPage;
        this.isFromGame = isFromGame;
    }

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().play("/sound/ranking.wav");
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
        label = new Label(100, 60, new Style.StyleRect(100, 100, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 90))
                .setText("Rank")
                .setTextColor(new Color(231, 17, 110, 255)));
        back = new Button(50, 650 - 105, Theme.get(3));
        backToGame = new Button(50, 650 - 105, Theme.get(15));
        next = new Button(250, 650 - 105, Theme.get(13));
        if (!isFromGame) {
            back.setClickedActionPerformed((int x, int y) -> {
                if (currentRankPage == 0) {
                    SceneController.getInstance().change((new MenuScene()));
                }else {
                    --currentRankPage;
                }
            });
        }
        if (isFromGame) {
            backToGame.setClickedActionPerformed((int x, int y) -> {
                SceneController.getInstance().change((new ModeScene(Global.random(0, 5))));
            });
        }
        next.setClickedActionPerformed((int x, int y) -> {
            if (currentRankPage < allRank.size() - 1) {
                ++currentRankPage;
            }
        });
        label1 = new Label(40, 200, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("1.")
                .setTextColor(new Color(241, 18, 18)));
        label2 = new Label(40, 250, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("2.")
                .setTextColor(new Color(234, 75, 16)));
        label3 = new Label(40, 300, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("3.")
                .setTextColor(new Color(246, 224, 16)));
        label4 = new Label(40, 350, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("4.")
                .setTextColor(new Color(77, 241, 18)));
        label5 = new Label(40, 400, new Style.StyleRect(50, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText("5.")
                .setTextColor(new Color(34, 53, 220)));

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
        AudioResourceController.getInstance().stop("/sound/ranking.wav");
        title = null;
        label = null;
        back = null;
        next = null;
    }


    @Override
    public void update() {
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
                .setText(describe)
                .setTextColor(new Color(216, 80, 226)));
//name
        label6 = new Label(125, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 0))
                .setTextColor(new Color(241, 18, 18)));
        label7 = new Label(125, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 1))
                .setTextColor(new Color(234, 75, 16)));
        label8 = new Label(125, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 2))
                .setTextColor(new Color(246, 224, 16)));
        label9 = new Label(125, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 3))
                .setTextColor(new Color(77, 241, 18)));
        label10 = new Label(125, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(getName(allRank.get(currentRankPage), 4))
                .setTextColor(new Color(34, 53, 220)));

        //score
        int min = getTime(allRank.get(currentRankPage), 0) / 1000 / 60;
        int sec = getTime(allRank.get(currentRankPage), 0) / 1000 % 60;
        label11 = new Label(275, 200, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(min + ":" + ((sec < 10) ? "0" + sec : sec))
                .setTextColor(new Color(241, 18, 18)));
        min = getTime(allRank.get(currentRankPage), 1) / 1000 / 60;
        sec = getTime(allRank.get(currentRankPage), 1) / 1000 % 60;
        label12 = new Label(275, 250, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(min + ":" + ((sec < 10) ? "0" + sec : sec))
                .setTextColor(new Color(234, 75, 16)));
        min = getTime(allRank.get(currentRankPage), 2) / 1000 / 60;
        sec = getTime(allRank.get(currentRankPage), 2) / 1000 % 60;
        label13 = new Label(275, 300, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(min + ":" + ((sec < 10) ? "0" + sec : sec))
                .setTextColor(new Color(246, 224, 16)));
        min = getTime(allRank.get(currentRankPage), 3) / 1000 / 60;
        sec = getTime(allRank.get(currentRankPage), 3) / 1000 % 60;
        label14 = new Label(275, 350, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(min + ":" + ((sec < 10) ? "0" + sec : sec))
                .setTextColor(new Color(77, 241, 18)));
        min = getTime(allRank.get(currentRankPage), 4) / 1000 / 60;
        sec = getTime(allRank.get(currentRankPage), 4) / 1000 % 60;
        label15 = new Label(275, 400, new Style.StyleRect(100, 50, new BackgroundType.BackgroundNull())
                .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                .setText(min + ":" + ((sec < 10) ? "0" + sec : sec))
                .setTextColor(new Color(34, 53, 220)));
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
            MouseTriggerImpl.mouseTrig(backToGame, e, state);
            if (currentRankPage <= 3) {
                MouseTriggerImpl.mouseTrig(next, e, state);
            }
            if (next.getIsFocus() && (!isConfirmNext)) {
                AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
                isConfirmNext = true;
            }
            if (!next.getIsFocus()) {
                isConfirmNext = false;
            }

            if (back.getIsFocus() && (!isConfirmBack)) {
                AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
                isConfirmBack = true;
            }
            if (!back.getIsFocus()) {
                isConfirmBack = false;
            }

            if ((next.getIsHover()) && (!isPlayedNext)) {
                AudioResourceController.getInstance().shot("/sound/tab.wav");
                isPlayedNext = true;
            }
            if (!next.getIsHover()) {
                isPlayedNext = false;
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
        if (!isFromGame) {
            back.paint(g);
        } else {
            backToGame.paint(g);
        }
        title.paint(g);
        if ((currentRankPage < 3) && (!isFromGame)) {
            next.paint(g);
        }
    }


}
