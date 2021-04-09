package game.gameobj;

import java.awt.*;

public abstract class SceneObject extends GameObject{

    public SceneObject(int top, int left, int width, int height) {
        super(top, left, width, height);
    }

    public abstract void collisionEffect(Actor actor);
}
