package game.menu.menu;

import game.controller.ImageController;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class Theme {

    public static final Theme DEFAULT_THEME = new Theme(
            //normal
            new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/buttonNor.png")))
                    .setText("單人模式")
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                    .setTextColor(Color.WHITE)
                    .setHaveBorder(false)
                    .setBorderColor(new Color(211, 211, 211))
                    .setBorderThickness(5),
            //HOVER
            new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/button.png")))
                    .setHaveBorder(false)
                    .setBorderColor(Color.WHITE)
                    .setBorderThickness(5)
                    .setText("準備")
                    .setTextColor(Color.WHITE)
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
            //FOCUS
            new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/button.png")))
                    .setText("開始")
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                    .setTextColor(new Color(162, 176, 198))
                    .setHaveBorder(false)
                    .setBorderColor(new Color(231, 8, 37))
                    .setBorderThickness(5)
    );
    private static ArrayList<Theme> themes;

    private final Style styleNormal;
    private final Style styleHover;
    private final Style styleFocus;

    public Theme(Style styleNormal, Style styleHover, Style styleFocus) {
        this.styleNormal = styleNormal;
        this.styleHover = styleHover;
        this.styleFocus = styleFocus;
    }

    private static ArrayList<Theme> instance() {
        if (themes == null) {
            themes = new ArrayList<>();
        }
        return themes;
    }

    public static void add(Theme theme) {
        instance().add(theme);
    }

    public static Theme get(int index) {
        return instance().get(index);
    }

    public Style normal() {
        return styleNormal;
    }

    public Style hover() {
        return styleHover;
    }

    public Style focus() {
        return styleFocus;
    }
}
