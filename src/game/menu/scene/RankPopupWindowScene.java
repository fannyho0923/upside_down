package game.menu.scene;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.utils.*;


public class RankPopupWindowScene extends PopupWindow {

    private Label rankTitle;
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


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean setPlayer(RankResult result, String filePath, int currentRankPage) {
        this.player = result;
        this.costTime = result.getTime();
        this.filePath = filePath;
        this.currentRankPage = currentRankPage;
        if (pass(player)) {
            isPassScore = true;
        } else {
            isPassScore = false;
        }
        return isPassScore;
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
        rankResults = new ArrayList<>();
        try {
            ranking = new Ranking(filePath);
        } catch (IOException e) {
            System.out.println(e);
        }
        scoreLabel = new Label(this.getWidth() / 2 - 150, 150, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("Cost Time:").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        scoreLabelDescribe = new Label(this.getWidth() / 2 - 150, 165, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
        rankTitle = new Label(this.getWidth() / 2 - 150, 30, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("-Congratulations-").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 50)));
        confirm = new Button(this.getWidth() / 2 - 50, 400, Theme.get(12));
        confirm.setClickedActionPerformed(clickedAction);
        Style et = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.YELLOW))
                .setHaveBorder(true)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);

        Style eHover = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.WHITE))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setText("請點擊");

        Style eNormal = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(new Color(128, 128, 128)))
                .setHaveBorder(true)
                .setTextColor(Color.LIGHT_GRAY)
                .setText("請點擊")
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);

        this.editText = new EditText(this.getWidth() / 2 - 100, 300, "請在此輸入");
        editText.setStyleNormal(eNormal);
        editText.setStyleHover(eHover);
        editText.setStyleFocus(et);
        editText.setEditLimit(10);   //設定文字輸入長度限制
        confirm.setClickedActionPerformed((int x, int y) -> {
            player.setName(playerName);
            if (editText.getEditText() != null) {
                write();
                SceneController.getInstance().change(new RankScene(currentRankPage));
            }
        });

    }


    @Override
    public void sceneEnd() {
        rankTitle = null;
        editText = null;
        confirm = null;
        scoreLabel = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, super.getWidth(), super.getHeight());
        rankTitle.paint(g);
        editText.paint(g);
        confirm.paint(g);
        if (costTime > 0) {
            scoreLabel.paint(g);
            scoreLabelDescribe = new Label(this.getWidth() / 2 - 150, 200, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                    .setText(""+costTime).setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 30)));
scoreLabelDescribe.paint(g);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
//            System.out.println("PopScene mouse");
            MouseTriggerImpl.mouseTrig(editText, e, getX(), getY(), state);
            MouseTriggerImpl.mouseTrig(confirm, e, getX(), getY(), state);
        };

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        System.out.println("~~~!!!");
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                System.out.println("~~~press");
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                System.out.println("~~~release");
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                System.out.println("~~~");
                editText.keyTyped(c);
                player.setName(editText.getEditText());
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
//        gameTime = System.nanoTime() - startTime;
//        System.out.println(gameTime);
//        gameTime = TimeUnit.NANOSECONDS.toMillis(gameTime);
//        int gtInt = (int) gameTime;


        //到時畫面印出排行榜需要轉換的格式
        //gt = new SimpleDateFormat("mm:ss:SSS", Locale.TAIWAN).format(new Date(gameTime));


        //讀目前的排行
        ArrayList<String> arr = ranking.readL();
        if (arr.size() > 0) {
            //把檔案內容轉成arraylist
            for (int i = 0; i < arr.size() - 1; i = i + 2) {
                rankResults.add(new RankResult(arr.get(i), Integer.parseInt(arr.get(i + 1))));
            }
        }

        //如果目前榜上資料超過5筆，要進行比對，有進榜單資格的話，就add，排序後取前10輸出
        //不超過9筆就直接加入榜單後，排序輸出
        //fanny
        if (rankResults.size() >= 5) {
            if (!(rankResults.get(rankResults.size() - 1).compareTo(player))) {
                return false;//資料已有五筆且輸給最後一名->未挑戰成功
            }
        }
        return true;//擁有可寫入排行榜資格
    }

}
