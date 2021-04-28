package game.scene;

import game.controller.AudioResourceController;
import game.controller.ImageController;
import game.gameobj.Actor;
import game.gameobj.Background;
import game.gameobj.Conveyor;
import game.gameobj.GameObject;
import game.menu.menu.BackgroundType;
import game.menu.menu.Label;
import game.menu.menu.Style;
import game.utils.Global;

import java.awt.*;
import java.util.ArrayList;


public class BasicScene extends GameScene {
    private Label labelBorn, labelDir, labelDirImg, labelSpace, labelSpaceImg, labelOK, labelMon1, labelMon2, labelBR,
            labelConveyor, labelRubber, labelDone, labelEnd, labelIns;


    public BasicScene(int num) {
        super(1, "/map/genMap.bmp",
                new Actor(0, 0, num), new Background(1920, 1920),
                //real 1300,1500 //plat test 150,2000
                960, 640, 0, 0,
                true, "basic.txt");
    }

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        AudioResourceController.getInstance().loop("/sound/Battle-Conflict-volumeReduce.wav", 50);
    }

    @Override
    public void sceneEnd(){
        super.sceneEnd();
        AudioResourceController.getInstance().stop("/sound/Battle-Conflict-volumeReduce.wav");
    }
}

