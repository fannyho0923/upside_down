package controller;

import scene.Scene;
import utils.CommandSolver;
import utils.GameKernel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SceneController implements GameKernel.PaintInterface, GameKernel.UpdateInterface
        , CommandSolver.KeyListener, CommandSolver.MouseListener {

    private static SceneController sceneController;
    private Scene currentScene;
    private Scene lastScene;

    private SceneController() {
    }

    public static SceneController getInstance() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    public void change(final Scene scene) {
        this.lastScene = this.currentScene;
        this.currentScene = scene;
        if (this.currentScene != null) {
            this.currentScene.sceneBegin();
        }
    }

    @Override
    public void paint(final Graphics g) {
        if (this.currentScene != null) {
            this.currentScene.paint(g);
        }
    }

    @Override
    public void update() {
        if (this.lastScene != null) {
            this.lastScene.sceneEnd();
            ImageController.getInstance().clear();
            this.lastScene = null;
        }
        if (this.currentScene != null) {
            this.currentScene.update();
        }
    }

    @Override
    public void keyPressed(final int commandCode, final long trigTime) {
        if (this.currentScene != null && this.currentScene.keyListener() != null) {
            this.currentScene.keyListener().keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(final int commandCode, final long trigTime) {
        if (this.currentScene != null && this.currentScene.keyListener() != null) {
            this.currentScene.keyListener().keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(final char c, final long trigTime) {
        if (this.currentScene != null && this.currentScene.keyListener() != null) {
            this.currentScene.keyListener().keyTyped(c, trigTime);
        }
    }

    @Override
    public void mouseTrig(final MouseEvent e, final CommandSolver.MouseState state, final long trigTime) {
        if (this.currentScene != null && this.currentScene.mouseListener() != null) {
            this.currentScene.mouseListener().mouseTrig(e, state, trigTime);
        }
    }
}
