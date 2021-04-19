package game.scene;

import game.camera.MapInformation;
import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.Spikes;
import game.menu.menu.BackgroundType;
import game.menu.menu.Label;
import game.menu.menu.Style;
import game.utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;


public class BasicScene extends GameScene {
    private Label labelBorn, labelDir, labelDirImg;

    public BasicScene(int num) {
        super("/map/basicMap0419.bmp", "/map/Packour/genMap.txt",
                new Actor(0,0,num), new Background(),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 10,true);
        labelBorn = new Label(50, 100,
                new Style.StyleRect(150, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                .setText("“...得快點想辦法逃離這裡!”")
                .setTextFont(new Font("TimesRoman", Font.BOLD, 12))
                .setTextColor(Color.WHITE)
                .setHaveBorder(true)
                .setBorderColor(new Color(253, 253, 253)));
        labelDir = new Label(100, 100,
                new Style.StyleRect(150, 40, true, new BackgroundType.BackgroundColor(new Color(22, 34, 52)))
                        .setText("控制方向鍵左右移動")
                        .setTextFont(new Font("TimesRoman", Font.BOLD, 14))
                        .setTextColor(Color.WHITE)
                        .setHaveBorder(true)
                        .setBorderColor(new Color(253, 253, 253)));
        labelDirImg = new Label(100, 120,
                new Style.StyleRect(150, 40, true,
                        new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/img/arrow_white.png"))));
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(labelBorn!=null) {
            labelBorn.paint(g);
        }
        if (labelBorn==null && labelDir!= null) {
            labelDir.paint(g);
            labelDirImg.paint(g);
        }

    }

    @Override
    public void update(){
        super.update();
        if(getActor().painter().left()>1050){
            labelBorn=null;
        }

        if(getActor().painter().left()>1100){
            labelDir=null;
        }
    }

}

