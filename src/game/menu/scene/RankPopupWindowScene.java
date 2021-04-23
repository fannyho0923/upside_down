package game.menu.scene;

import java.awt.*;

import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.utils.CommandSolver;


public class RankPopupWindowScene extends PopupWindow {

     Label rankTitle;
     Button confirm;
     EditText editText;
     Label.ClickedAction clickedAction;

public EditText getEditText(){
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
        rankTitle = new Label(getWidth() / 2 - 150, 30, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("-Rank-").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 100)));

        confirm = new Button(this.getWidth() / 2 - 50, 250, Theme.get(12));
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
                .setText("HOVER");

        Style eNormal = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(new Color(128, 128, 128)))
                .setHaveBorder(true)
                .setTextColor(Color.LIGHT_GRAY)
                .setText("請點擊")
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);

        this.editText = new EditText(430, 290, "請在此輸入");
        editText.setStyleNormal(eNormal);
        editText.setStyleHover(eHover);
        editText.setStyleFocus(et);
        editText.setEditLimit(10);   //設定文字輸入長度限制
        confirm.setClickedActionPerformed((int x, int y) -> System.out.println());


    }



    @Override
    public void sceneEnd() {
        rankTitle = null;
        editText = null;
        confirm = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, super.getWidth(), super.getHeight());
        rankTitle.paint(g);
        editText.paint(g);
        confirm.paint(g);

    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {

        return (e, state, trigTime) -> {
            MouseTriggerImpl.mouseTrig(editText, e, getX(), getY(), state);
            MouseTriggerImpl.mouseTrig(confirm, e, getX(), getY(), state);
        };

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }


}
