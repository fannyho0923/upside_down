package game.scene;

import game.utils.CommandSolver;
import game.utils.GameKernel;

public abstract class Scene implements GameKernel.UpdateInterface, GameKernel.PaintInterface {
    public abstract void sceneBegin();

    public abstract void sceneEnd();

    public abstract CommandSolver.KeyListener keyListener();

    public abstract CommandSolver.MouseListener mouseListener();
}
