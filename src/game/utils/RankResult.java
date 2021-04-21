package game.utils;

public class RankResult {
    private String name;
    private long time;

    public RankResult(String name, long time){
        this.name = name;
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
