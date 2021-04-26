
package game;

import game.controller.ImageController;
import game.controller.SceneController;

import game.menu.menu.BackgroundType;
import game.menu.menu.Style;
import game.menu.menu.Theme;
import game.menu.scene.*;
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
                        .add(KeyEvent.VK_ESCAPE, Global.VK_ESCAPE)
                        .add(KeyEvent.VK_R, Global.VK_R)
                        .add(KeyEvent.VK_A, Global.VK_A)
                        .next().trackChar().keyTypedMode().subscribe(sceneController)
                )
                .paint(sceneController)
                .update(sceneController)
                .gen();

        jFrame.setTitle("Upside Down");
        jFrame.setSize(Global.WINDOW_WIDTH+12, Global.WINDOW_HEIGHT+32);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(gameKernel);
        jFrame.setVisible(true);
        gameKernel.run();
    }

    private static void initTheme() {
        //0
        //start button
        Style hover = new Style.StyleOval(100, 100, true, new BackgroundType.BackgroundNull())
                .setTextColor(new Color(213, 9, 131))
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
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Start");
        Style normal = new Style.StyleOval(100, 100, false, new BackgroundType.BackgroundNull())
                .setHaveBorder(false)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(15)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Ready");

        Theme.add(new Theme(normal, hover, focus));
//1已在menuScene刪除
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
        //rank button in menu
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/buttonNor.png")))
                        .setText("RANK")
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
                        .setText("VIEW")
                        .setTextColor(Color.WHITE)
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/button.png")))
                        .setText("VIEW")
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
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 30))
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
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 30)),
                //FOCUS
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Back")
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));

        //4
        //mode4
        Theme.add(new Theme(new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_1.png")))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(147, 147, 138))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("SpeedRun"),
                new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_1.png")))
                        .setTextColor(new Color(30, 70, 118))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(30, 70, 118))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                        .setText("SpeedRun"),
                new Style.StyleOval(200, 200, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_1.png")))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(147, 147, 138))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                        .setText("SpeedRun")));
        //5
        //mode4
        Theme.add(new Theme(new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_2.png")))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(147, 147, 138))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Parkour"),
                new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_2.png")))
                        .setTextColor(new Color(30, 70, 118))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(30, 70, 118))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                        .setText("Parkour"),
                new Style.StyleOval(200, 200, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_2.png")))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(147, 147, 138))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                        .setText("Parkour")));
        //6
        //mode5
        Theme.add(new Theme(new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_3.png")))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(147, 147, 138))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Stamina"),
                new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_3.png")))
                        .setTextColor(new Color(30, 70, 118))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(30, 70, 118))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                        .setText("Stamina"),
                new Style.StyleOval(200, 200, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_3.png")))
                        .setHaveBorder(true)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                        .setText("Stamina")));
        //7
        //mode1
        Theme.add(new Theme(new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_4.png")))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(147, 147, 138))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Practice"),
                new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_4.png")))
                        .setTextColor(new Color(30, 70, 118))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(30, 70, 118))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                        .setText("Practice"),
                new Style.StyleOval(200, 200, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_4.png")))
                        .setHaveBorder(true)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                        .setText("Practice")));
        //8
        //mode2
        Theme.add(new Theme(new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_5.png")))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(147, 147, 138))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("Story"),
                new Style.StyleOval(200, 200, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_5.png")))
                        .setTextColor(new Color(30, 70, 118))
                        .setHaveBorder(true)
                        .setBorderColor(new Color(30, 70, 118))
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 28))
                        .setText("Story"),
                new Style.StyleOval(200, 200, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/mode/mode_5.png")))
                        .setHaveBorder(true)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                        .setText("Story")));

        //9
        //pop back to Menu button
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Menu")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(222, 29, 88))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("Menu")
                        .setTextColor(new Color(220, 46, 106, 238))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Menu")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
        //10
        //pop continue button
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Continue")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(222, 29, 88))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("Continue")
                        .setTextColor(new Color(220, 46, 106, 238))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Continue")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
        //11
        //pop restart button
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Restart")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(222, 29, 88))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("Restart")
                        .setTextColor(new Color(220, 46, 106, 238))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Restart")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
        //12
        //confirm button
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("CONFIRM")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(222, 29, 88))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("SEND")
                        .setTextColor(new Color(220, 46, 106, 238))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("CONFIRM")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(184, 162, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
        //13
        //"next" button in RankScene
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Next")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(false)
                        .setBorderColor(new Color(222, 29, 88))
                        .setBorderThickness(5),
                //HOVER
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setHaveBorder(false)
                        .setBorderColor(Color.WHITE)
                        .setBorderThickness(5)
                        .setText("Next")
                        .setTextColor(new Color(220, 46, 106, 238))
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
                //FOCUS
                new Style.StyleRect(100, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Next")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
        //14
        //back button in mode
        Theme.add(new Theme(
                //normal
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Back")
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 30))
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
                        .setTextColor(new Color(30, 70, 118))
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 30)),
                //FOCUS
                new Style.StyleRect(50, 50, false, new BackgroundType.BackgroundNull())
                        .setText("Back")
                        .setTextFont(new Font("TimesRoman", Font.TYPE1_FONT, 30))
                        .setTextColor(new Color(162, 176, 198))
                        .setHaveBorder(false)
                        .setBorderColor(new Color(231, 8, 37))
                        .setBorderThickness(5)
        ));
    }
}
