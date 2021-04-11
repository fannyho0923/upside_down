package game.gameobj;

public abstract class MapObject extends GameObject{

    public MapObject(int top, int left, int width, int height) {
        super(top, left, width, height);
    }

    public abstract void CollisionEffect(MapObject gameObject);
    public abstract void CollisionEffect(Actor actor);
}
