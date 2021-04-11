package game.gameobj;

import java.awt.*;

public abstract class MapObject extends GameObject {

    public MapObject(int x, int y, int weight, int height){
        super(x,y,weight,height);
    }

    public abstract void collisionEffect(Actor actor);
}
