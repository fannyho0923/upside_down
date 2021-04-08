import controller.SceneController;
import scene.TestGame;
import scene.TestSceneA;
import utils.CommandSolver;
import utils.GameKernel;
import utils.Global;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {// 資料刷新時間

    public static void main(final String[] args) {
        final JFrame jFrame = new JFrame();

        final SceneController sceneController = SceneController.getInstance();
        sceneController.change(new TestGame());
        //sceneController.change(new TestSceneA());
        final GameKernel gameKernel = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream()
                        .mouseTrack().forceRelease().subscribe(sceneController)
                        .keyboardTrack()
                        .add(KeyEvent.VK_ENTER, 1)
                        .add(KeyEvent.VK_LEFT, 2)
                        .add(KeyEvent.VK_RIGHT, 3)
                        .add(KeyEvent.VK_SPACE, 4)
                        .add(KeyEvent.VK_UP,5)
                        .add(KeyEvent.VK_DOWN,6)
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
