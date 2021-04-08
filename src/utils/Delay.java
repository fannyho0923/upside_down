package utils;

public class Delay {
    private int count;// 當下的經過幀數
    private final int countLimit;// 總共要計時的幀數
    private boolean isPause;
    private boolean isLoop;

    public Delay(final int countLimit) {
        this.countLimit = countLimit;
        this.count = 0;
        this.isPause = true;
    }

    public void stop() {
        this.count = 0;
        this.isPause = true;
    }

    public void loop() {
        this.isLoop = true;
        this.isPause = false;
    }

    public void play() {
        this.isLoop = false;
        this.isPause = false;
    }

    public void pause() {
        this.isPause = true;
    }

    public boolean isStop() {
        return this.count == 0 && this.isPause;
    }

    public boolean isPause() {
        return this.isPause;
    }

    public boolean isPlaying() {
        return !this.isPause;
    }

    public boolean count() {
        if (this.isPause) {
            return false;
        }
        if (this.count >= this.countLimit) {
            if (this.isLoop) {
                this.count = 0;
            } else {
                stop();
            }
            return true;
        }
        this.count++;
        return false;
    }

}
