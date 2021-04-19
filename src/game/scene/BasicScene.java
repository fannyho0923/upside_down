package game.scene;

import game.camera.MapInformation;
import game.controller.AudioResourceController;
import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.Spikes;
import game.menu.menu.BackgroundType;
import game.menu.menu.Label;
import game.menu.menu.Style;

import java.awt.*;
import java.util.ArrayList;


public class BasicScene extends GameScene {
    private Label labelBorn;

    public BasicScene(int num) {
        super("/map/basicMap0419.bmp", "/map/Packour/genMap.txt",
                new Actor(0,0,num), new Background(),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 10,true);
        labelBorn = new Label(100, 480, new Style.StyleRect(200, 80, false, new BackgroundType.BackgroundNull())
                .setText("“...得快點想辦法逃離這裡!"));
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        labelBorn.paint(g);
    }

}

