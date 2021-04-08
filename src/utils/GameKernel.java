package utils;

import java.awt.*;
import java.awt.image.BufferStrategy;

public final class GameKernel extends Canvas {
    // 畫面更新時間
    private final long frameDeltaTime;
    private final long nsPerUpdates;// 每一次邏輯更新要花費的奈秒數
    private final UpdateInterface updateInterface;
    private final PaintInterface pi;
    private CommandSolver cs;

    private GameKernel(final UpdateInterface updateInterface, final PaintInterface pi, final CommandSolver.BuildStream buildStream, final int updatesPerSec, final int framePerSec) {
        this.updateInterface = updateInterface;
        this.pi = pi;
        this.nsPerUpdates = Global.NANOSECOND_PER_UPDATE;
        this.frameDeltaTime = Global.LIMIT_DELTA_TIME;
        if (buildStream != null) {
            this.cs = buildStream.bind(this, updatesPerSec);
        }
    }

    private void paint() {
        // 當沒有緩衝機制時我們便調用方法創建
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        final Graphics g = bs.getDrawGraphics();// 從BufferStrategy中取出Graphics 緩衝機制會自行判斷並進行Cache處理
        g.fillRect(0, 0, this.getWidth(), this.getHeight());// 先畫一個跟畫布一樣大小的區塊

        // 利用 Graphics 進行繪圖
        if (this.pi != null) {
            this.pi.paint(g);
        }
        // end

        g.dispose();// 畫完之後釋放掉相關資源
        bs.show();// 畫出畫面
    }

    public void run() {
        this.cs.start();
        final long startTime = System.nanoTime();
        long passedUpdated = 0;
        long lastRepaintTime = System.nanoTime();
        int paintTimes = 0;
        long timer = System.nanoTime();
        while (true) {
            final long currentTime = System.nanoTime();// 系統當前時間
            final long totalTime = currentTime - startTime;// 從開始到現在經過的時間
            final long targetTotalUpdated = totalTime / this.nsPerUpdates;// 開始到現在應該更新的次數
            while (passedUpdated < targetTotalUpdated) {// 如果當前經過的次數小於實際應該要更新的次數
                //update 更新追上當前次數
                if (this.cs != null) {
                    this.cs.update();
                }
                if (this.updateInterface != null) {
                    this.updateInterface.update();
                }
                passedUpdated++;
            }
            if (currentTime - timer >= 1000000000) {
                System.out.println("FPS: " + paintTimes);
                paintTimes = 0;
                timer = currentTime;
            }
            if (this.frameDeltaTime <= currentTime - lastRepaintTime) {
                lastRepaintTime = currentTime;
                paint();
                paintTimes++;
            }
        }
    }

    @FunctionalInterface
    public interface PaintInterface {
        void paint(Graphics g);
    }

    @FunctionalInterface
    public interface UpdateInterface {
        void update();
    }

    public static class Builder {
        private PaintInterface paintInterface;
        private UpdateInterface updateInterface;
        private CommandSolver.BuildStream buildStream;
        private int framePerSec;
        private int updatesPerSec;

        public Builder() {
            this.paintInterface = null;
            this.updateInterface = null;
            this.buildStream = null;
            this.framePerSec = this.updatesPerSec = 60;
        }

        public Builder paint(final PaintInterface paintInterface) {
            this.paintInterface = paintInterface;
            return this;
        }

        public Builder update(final UpdateInterface updateInterface) {
            this.updateInterface = updateInterface;
            return this;
        }

        public Builder fps(final int framePerSec) {
            this.framePerSec = framePerSec;
            return this;
        }

        public Builder ups(final int updatesPerSec) {
            this.updatesPerSec = updatesPerSec;
            return this;
        }

        public Builder input(final CommandSolver.BuildStream buildStream) {
            this.buildStream = buildStream;
            return this;
        }

        public GameKernel gen() {
            return new GameKernel(this.updateInterface
                    , this.paintInterface
                    , this.buildStream
                    , this.updatesPerSec
                    , this.framePerSec);
        }
    }
}
