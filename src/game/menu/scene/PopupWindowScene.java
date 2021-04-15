package game.menu.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import game.menu.menu.*;
import game.menu.menu.impl.MouseTriggerImpl;
import game.utils.CommandSolver;

public class PopupWindowScene extends PopupWindow {

    private Label a;
    private Button b;

    public PopupWindowScene(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void sceneBegin() {
        a = new Label(180, 230);
        b = new Button(450, 220, Theme.get(0));
        b.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));
    }

    @Override
    public void sceneEnd() {
        a = null;
        b = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        a.paint(g);
        b.paint(g);
    }

    @Override
    public void update() {

    }

    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        MouseTriggerImpl.mouseTrig(a, e, state);
        MouseTriggerImpl.mouseTrig(b, e, state);
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
