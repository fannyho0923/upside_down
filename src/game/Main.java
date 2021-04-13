
package game;

import game.controller.SceneController;

import game.scene.BasicScene;
import game.scene.BasicSceneTest;
import game.scene.SceneSet;
import game.scene.fannyTest;
import game.utils.CommandSolver;
import game.utils.GameKernel;
import game.utils.Global;


import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {// 資料刷新時間

    public static void main(final String[] args) {
        final JFrame jFrame = new JFrame();

        final SceneController sceneController = SceneController.getInstance();
        sceneController.change(new BasicSceneTest());
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

        jFrame.setTitle("Game Merge");
        jFrame.setSize(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(gameKernel);

        jFrame.setVisible(true);

        gameKernel.run();
    }
}
