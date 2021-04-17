package game.menu.menu;

import java.awt.Graphics;

public class Button extends Label {

    public Button(int x, int y, Style style) {
        super(x, y, style);

    }

    public Button(int x, int y, Theme theme) {
        super(x, y, theme);

    }

    public Button(int x, int y) {
        super(x, y);
    }

    @Override
    public void paint(Graphics g) {
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paint(g, super.getX(), super.getY());
        }
    }

    @Override
    public void update() {

    }
}
