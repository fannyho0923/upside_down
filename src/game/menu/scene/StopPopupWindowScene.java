package game.menu.scene;

import java.awt.*;
import java.awt.event.MouseEvent;

import game.controller.SceneController;
import game.menu.menu.*;
import game.menu.menu.Button;
import game.menu.menu.Label;
import game.menu.menu.impl.MouseTriggerImpl;
import game.scene.BasicScene;
import game.scene.GameScene;
import game.utils.CommandSolver;


public class StopPopupWindowScene extends PopupWindow {

    private Label stopTitle;
    private Button continueGame;
    private Button restart;
    public Button back;
    private Label.ClickedAction clickedAction;

    public StopPopupWindowScene(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setRestartClicked(Label.ClickedAction clickedAction) {
        this.clickedAction = clickedAction;
    }

    @Override
    public void sceneBegin() {
        stopTitle = new Label(getWidth() / 2 - 150, 30, new Style.StyleRect(300, 100, new BackgroundType.BackgroundNull())
                .setText("-STOP-").setHaveBorder(false).setBorderColor(Color.black).setTextFont(new Font("TimesRoman", Font.BOLD, 100)));
        continueGame = new Button(this.getWidth() / 2 - 50, 150, Theme.get(10));
        restart = new Button(this.getWidth() / 2 - 50, 250, Theme.get(11));
        restart.setClickedActionPerformed(clickedAction);
        back = new Button(this.getWidth() / 2 - 50, 350, Theme.get(9));

        continueGame.setClickedActionPerformed((int x, int y) -> this.hide());
        back.setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new MenuScene()));
    }

    @Override
    public void sceneEnd() {
        stopTitle = null;
        continueGame = null;
        restart = null;
        back = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, super.getWidth(), super.getHeight());
        stopTitle.paint(g);
        continueGame.paint(g);
        restart.paint(g);
        back.paint(g);
    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {

        return (e, state, trigTime) -> {
            MouseTriggerImpl.mouseTrig(continueGame, e, getX(), getY(), state);
            MouseTriggerImpl.mouseTrig(restart, e, getX(), getY(), state);
            MouseTriggerImpl.mouseTrig(back, e, getX(), getY(), state);
        };

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }


}
