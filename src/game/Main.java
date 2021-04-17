
package game;

import game.controller.ImageController;
import game.controller.SceneController;

import game.menu.menu.BackgroundType;
import game.menu.menu.Style;
import game.menu.menu.Theme;
import game.menu.scene.MenuScene;
import game.utils.CommandSolver;
import game.utils.GameKernel;
import game.utils.Global;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {// 資料刷新時間

    public static void main(final String[] args) {
        initTheme();
        final JFrame jFrame = new JFrame();
        final SceneController sceneController = SceneController.getInstance();
        sceneController.change(new MenuScene());
        final GameKernel gameKernel = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream()
                        .mouseTrack().forceRelease().subscribe(sceneController)
                        .keyboardTrack()
                        .add(KeyEvent.VK_ENTER, Global.VK_ENTER)
                        .add(KeyEvent.VK_LEFT, Global.VK_LEFT)
                        .add(KeyEvent.VK_RIGHT, Global.VK_RIGHT)
                        .add(KeyEvent.VK_SPACE, Global.VK_SPACE)
                        .add(KeyEvent.VK_A, Global.VK_A)
                        .next().keyCleanMode().subscribe(sceneController)
                )
                .paint(sceneController)
                .update(sceneController)
                .gen();

        jFrame.setTitle("Upside Down");
        jFrame.setSize(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(gameKernel);
        jFrame.setVisible(true);
        gameKernel.run();
    }

    private static void initTheme() {
        //0
        Style hover = new Style.StyleOval(100, 100, true, new BackgroundType.BackgroundNull())
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(false)
                .setBorderColor(new Color(0, 157, 230))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Start");
        Style focus = new Style.StyleOval(100, 100, true, new BackgroundType.BackgroundColor(Color.MAGENTA))
                .setTextColor(Color.WHITE)
                .setHaveBorder(false)
                .setBorderColor(new Color(0, 157, 230))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                .setText("Start");
        Style normal = new Style.StyleOval(100, 100, false, new BackgroundType.BackgroundNull())
                .setHaveBorder(false)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(15)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Ready");

        Theme.add(new Theme(normal, hover, focus));
//1
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/buttonNor.png")))
                        .setText("雙人模式")
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
        ));
        //2
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/buttonNor.png")))
                        .setText("排行榜")
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
        ));
        //3
        //back button
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Back")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(162, 176, 198))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("Back")
                        .setTextColor(new Color(72, 239, 239, 35))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Back")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
    }
}
