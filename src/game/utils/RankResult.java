package game.utils;

public class RankResult {
    private String name;
    private int time;

    public RankResult(String name, int time){
        this.name = name;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean compareTo(RankResult rankResult){
        if (rankResult.getTime()<this.time) {
            return true;
        } else {
            return false;
        }
    }

}
