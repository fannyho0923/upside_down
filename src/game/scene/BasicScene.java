package game.scene;

import game.controller.ImageController;
import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.GameObject;
import game.menu.menu.BackgroundType;
import game.menu.menu.Label;
import game.menu.menu.Style;
import java.awt.*;


public class BasicScene extends GameScene {
    private Label labelBorn, labelDir, labelDirImg, labelSpace, labelSpaceImg, labelOK, labelMon1, labelMon2, labelBR,
            labelConveyor, labelRubber, labelDone, labelEnd, labelIns;

    public BasicScene(int num) {
        super(1,"/map/basicMap.bmp",
                new Actor(0, 0, num), new Background(1920, 1920),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 0,
                true, "basic.txt");
    }

    @Override
    public void init(int level,String mapBmpPath, Actor actor, GameObject background,
                     int cameraWidth, int cameraHeight, int cameraVelocityX, int cameraVelocityY,
                     boolean actorTrigCamera, String filePath){
        super.init(level,mapBmpPath, actor, background,
                cameraWidth, cameraHeight, cameraVelocityX, cameraVelocityY,
                actorTrigCamera, filePath);
        labelBorn = new Label(50, 100,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("“...得快點想辦法逃離這裡!”")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelDir = new Label(100, 80,
                new Style.StyleRect(150, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("控制方向鍵左右移動")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelDirImg = new Label(125, 130,
                new Style.StyleRect(80, 40, true,
                        new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/arrow_white.png"))));
        labelSpace = new Label(250, 100,
                new Style.StyleRect(170, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("進行重力切換來躲過尖刺")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelSpaceImg = new Label(290, 150,
                new Style.StyleRect(80, 40, true,
                        new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/space.png"))));
        labelOK = new Label(430, 100,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("做得好!請繼續完成此關卡吧!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelMon1 = new Label(650, 50,
                new Style.StyleRect(210, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("小心! 一旦碰到怪物，就會死亡!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelMon2 = new Label(665, 91,
                new Style.StyleRect(160, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("請看準時機躲過怪物吧!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelBR = new Label(75, 110,
                new Style.StyleRect(160, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("易碎地在踩到後會消失!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelConveyor = new Label(60, 100,
                new Style.StyleRect(230, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("傳輸帶會帶動人物往特定方向前進。")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelRubber = new Label(570, 330,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("彈力帶會改變人物重力方向。")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelDone = new Label(10, 330,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("利用學習到的內容通關吧!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));

        labelEnd = new Label(710, 350,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("恭喜你完成了教學關卡!")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelIns = new Label(710, 400,
                new Style.StyleRect(190, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("請按ESC選擇下一步")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
    }

    @Override
    public void midPaint(Graphics g) {
        if (labelBorn != null) {
            labelBorn.paint(g);
        }
        if (labelBorn == null && labelDir != null) {
            labelDir.paint(g);
            labelDirImg.paint(g);
        }

        if (labelDir == null && labelSpace != null) {
            labelSpace.paint(g);
            labelSpaceImg.paint(g);
        }

        if (labelSpace == null && labelOK != null) {
            labelOK.paint(g);
        }

        if (labelOK == null && getActor().painter().left() < 1750 && getActor().painter().bottom() < 400 && labelMon1 != null) {
            labelMon1.paint(g);
            labelMon2.paint(g);
        }
        if (labelMon1 == null && labelBR != null && getActor().painter().left() < 250 && getActor().painter().top() > 700) {
            labelBR.paint(g);
        }
        if (labelBR == null && labelConveyor != null && getActor().painter().left() > 970) {
            labelConveyor.paint(g);
        }
        if (labelConveyor == null && labelRubber != null && getActor().painter().bottom() > 900) {
            labelRubber.paint(g);
        }
        if (labelRubber == null && labelDone != null && getActor().painter().left() < 1200) {
            labelDone.paint(g);
        }
        if (labelDone == null && labelEnd != null && getActor().painter().right() > 810 && getActor().painter().bottom() > 1820) {
            labelEnd.paint(g);
            labelIns.paint(g);
        }
    }

    @Override
    public void update() {
        super.update();
        if (getActor().painter().left() > 1050) {
            labelBorn = null;
        }
        if (labelBorn == null && getActor().painter().left() > 1160) {
            labelDir = null;
        }
        if (labelDir == null && getActor().painter().left() > 1250) {
            labelSpace = null;
        }
        if (labelSpace == null && getActor().painter().left() > 1500) {
            labelOK = null;
        }
        if (getActor().painter().left() < 1650 && getActor().painter().top() < 300) {
            labelMon1 = null;
            labelMon2 = null;
        }
        if (labelMon1 == null && getActor().painter().left() > 300 && getActor().painter().top() > 700) {
            labelBR = null;
        }
        if (labelBR == null && getActor().painter().right() > 1160) {
            labelConveyor = null;
        }
        if (labelConveyor == null && getActor().painter().left() < 1500 && getActor().painter().bottom() > 900) {
            labelRubber = null;
        }
        if (labelRubber == null && getActor().painter().left() < 980) {
            labelDone = null;
        }
    }

}

