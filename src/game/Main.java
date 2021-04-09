<<<<<<< HEAD
<<<<<<< HEAD:src/game/Main.java
=======
>>>>>>> 8606fae96e5ba903d449a7313015fc36f79395b4
package game;

import game.controller.SceneController;
import game.scene.TestGame;
<<<<<<< HEAD
import game.scene.TestSceneA;
import game.scene.TestSceneB;
import game.utils.CommandSolver;
import game.utils.GameKernel;
import game.utils.Global;
//=======
import controller.SceneController;
import scene.TestGame;
import utils.CommandSolver;
import utils.GameKernel;
import utils.Global;
>>>>>>> 67f0f1c3d2778b1663c66bcaa19ef5ee271f7ecf:src/Main.java
=======
import game.utils.CommandSolver;
import game.utils.GameKernel;
import game.utils.Global;
>>>>>>> 8606fae96e5ba903d449a7313015fc36f79395b4

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {// 資料刷新時間

    public static void main(final String[] args) {
        final JFrame jFrame = new JFrame();

        final SceneController sceneController = SceneController.getInstance();
        sceneController.change(new TestGame());
        final GameKernel gameKernel = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream()
                        .mouseTrack().forceRelease().subscribe(sceneController)
                        .keyboardTrack()
                        .add(KeyEvent.VK_ENTER, Global.VK_ENTER)
                        .add(KeyEvent.VK_LEFT, Global.VK_LEFT)
                        .add(KeyEvent.VK_RIGHT, Global.VK_RIGHT)
                        .add(KeyEvent.VK_SPACE, Global.VK_SPACE)
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
