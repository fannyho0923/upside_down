package gameobj;

import controller.ImageController;
import utils.Delay;

import java.awt.*;

public class TestBoom extends GameObject {
    private static final int MOVE_SPEED = 10;
    private final Delay stateDelay;
    private final Image img1;
    private final Image img2;
    private State state;

    public TestBoom(final int x, final int y) {
        super(x, y, 30, 40, true);
        this.state = State.ALIVE;
        this.stateDelay = new Delay(30);
        final ImageController ic = ImageController.getInstance();
        this.img1 = ic.tryGet("/boom.png");
        this.img2 = ic.tryGet("/boom2.png");
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    @Override
    public void paint(final Graphics g) {
        if (this.state == State.ALIVE) {
            g.drawImage(this.img1, painter().left(), painter().top(), null);
            return;
        }
        g.drawImage(this.img2, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {
        switch (this.state) {
            case ALIVE :
                offsetY(-MOVE_SPEED);
                break;
            case BOOM :
                if (this.stateDelay.isStop()) {
                    this.stateDelay.play();
                }
                if (this.stateDelay.count()) {
                    this.state = State.DEAD;
                }
        }
    }


    public enum State {
        ALIVE,
        BOOM,
        DEAD
    }
}
