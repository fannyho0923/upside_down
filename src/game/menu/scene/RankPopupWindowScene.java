package game.menu.scene;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.utils.*;


public class RankPopupWindowScene extends PopupWindow {

    private Label tryHard;
    private Button confirm;
    private EditText editText;
    private Label.ClickedAction clickedAction;
    private ArrayList<RankResult> rankResults;
    private RankResult player;
    private Ranking ranking;
    private String filePath;
    private boolean isPassScore;
    private boolean isWrote;
    private String playerName;
    private int currentRankPage;
    private Label scoreLabel;
    private Label scoreLabelDescribe;
    private int costTime;
    private Image rankBoard;
    private Image leaderboard;
    private Label no1;
    private Label no2;
    private Label no3;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayer(RankResult result, String filePath, int currentRankPage) {
        this.player = result;
        this.costTime = result.getTime();
        this.filePath = filePath;
        this.currentRankPage = currentRankPage;
        System.out.println(currentRankPage);
        if (pass(player)) {
            isPassScore = true;
        } else {
            isPassScore = false;
        }

    }

    public EditText getEditText() {
        return editText;
    }

    public RankPopupWindowScene(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    public void setRestartClicked(Label.ClickedAction clickedAction) {
        this.clickedAction = clickedAction;
    }

    @Override
    public void sceneBegin() {
//        AudioResourceController.getInstance().play("/sound/ranking.wav");
        leaderboard = ImageController.getInstance().tryGet("/img/background/leaderboard.png");
        rankBoard = ImageController.getInstance().tryGet("/img/background/rankingboard.png");
        rankResults = new ArrayList<>();
        try {
            ranking = new Ranking(filePath);
        } catch (IOException e) {
            System.out.println(e);
        }
        scoreLabel = new Label(this.getWidth() / 2 - 150, 250, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("Cost Time:").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        scoreLabelDescribe = new Label(this.getWidth() / 2 - 150, 165, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        tryHard = new Label(this.getWidth() / 2 - 150, 355, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("~Try Hard~").setTextColor(Color.black).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        confirm = new Button(this.getWidth() / 2 - 50, 450, Theme.get(12));
        confirm.setClickedActionPerformed(clickedAction);
        Style et = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.YELLOW))
                .setHaveBorder(true)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(new Color(45, 21, 21))
                .setBorderThickness(5);

        Style eHover = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.WHITE))
                .setHaveBorder(true)
                .setBorderColor(new Color(54, 30, 30))
                .setBorderThickness(5)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setText("CLICK");

        Style eNormal = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(new Color(128, 128, 128)))
                .setHaveBorder(true)
                .setTextColor(Color.LIGHT_GRAY)
                .setText("CLICK")
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(new Color(64, 39, 39))
                .setBorderThickness(3);

        this.editText = new EditText(this.getWidth() / 2 - 100, 380, "Enter Your Name");
        editText.setStyleNormal(eNormal);
        editText.setStyleHover(eHover);
        editText.setStyleFocus(et);
        editText.setEditLimit(10);   //設定文字輸入長度限制
        confirm.setClickedActionPerformed((int x, int y) -> {
            if (!isPassScore) {
                SceneController.getInstance().change(new RankScene(currentRankPage, true));
            } else {
                player.setName(playerName);
                if (editText.getEditText() != null) {
                    write();
                    SceneController.getInstance().change(new RankScene(currentRankPage, true));
                }
            }
        });


    }


    @Override
    public void sceneEnd() {
//        AudioResourceController.getInstance().stop("/sound/ranking.wav");
        tryHard = null;
        editText = null;
        confirm = null;
        scoreLabel = null;
        rankBoard = null;
        leaderboard = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.drawImage(rankBoard, this.getWidth() / 2 - 200, -100, 420, 650, null);
        g.drawImage(leaderboard, this.getWidth() / 2 - 190, 80, 380, 220, null);
        if (isPassScore) {
            editText.paint(g);
        } else {
            tryHard.paint(g);
        }
        confirm.paint(g);
        int min = costTime / 1000 / 60;
        int sec = costTime / 1000 % 60;
        scoreLabel.paint(g);
        scoreLabelDescribe = new Label(this.getWidth() / 2 - 150, 300, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("" + min + ":" + ((sec < 10) ? "0" + sec : sec)).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        scoreLabelDescribe.paint(g);
        if (no1 != null) {
            no1.paint(g);
        }
        if (no2 != null) {
            no2.paint(g);
        }
        if (no3 != null) {
            no3.paint(g);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            MouseTriggerImpl.mouseTrig(editText, e, getX(), getY(), state);
            MouseTriggerImpl.mouseTrig(confirm, e, getX(), getY(), state);

            if (confirm.getIsFocus()) {
                AudioResourceController.getInstance().shot("/sound/tab_confirm.wav");
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
                editText.keyTyped(c);
                setPlayerName(editText.getEditText());
            }
        };
    }


    public void write() {
        //成功且未寫入過
        if (isPassScore && (!isWrote)) {
            System.out.println("writing");
            rankResults.add(player);
            isWrote = true;
            //排序
            Collections.sort(rankResults, new RankSort());
            //取前5名轉回一串字串輸出
            String output = "";
            if (rankResults.size() > 5) {
                rankResults.remove(5);
            }
            for (int i = 0; i < rankResults.size(); i++) {
                output += rankResults.get(i).getName() + "," + rankResults.get(i).getTime() + ",";
            }
            //清資料用
            //rankResults.clear();
            ranking.writeOut(output);
        }
    }

    public boolean pass(RankResult player) {
        try {
            ranking = new Ranking(filePath);
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

        if (rankResults.size() >= 1) {
            no1 = new Label(this.getWidth() / 2 - 20, 130, new Style.StyleRect(50, 20, new BackgroundType.BackgroundNull())
                    .setText(rankResults.get(0).getName()).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 20)));
        }
        if (rankResults.size() >= 2) {
            no2 = new Label(this.getWidth() / 2 - 140, 205, new Style.StyleRect(50, 20, new BackgroundType.BackgroundNull())
                    .setText(rankResults.get(1).getName()).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 20)));
        }
        if (rankResults.size() >= 3) {
            no3 = new Label(this.getWidth() / 2 + 100, 205, new Style.StyleRect(50, 20, new BackgroundType.BackgroundNull())
                    .setText(rankResults.get(2).getName()).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 20)));
        }

        //如果目前榜上資料超過5筆，要進行比對，有進榜單資格的話，就add，排序後取前5輸出
        //不超過9筆就直接加入榜單後，排序輸出
        if (rankResults.size() >= 5) {
            if (!(rankResults.get(rankResults.size() - 1).compareTo(player))) {
                return false;//資料已有五筆且輸給最後一名->未挑戰成功
            }
        }
        return true;//擁有可寫入排行榜資格
    }

}
