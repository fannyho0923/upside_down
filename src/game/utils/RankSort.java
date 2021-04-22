package game.utils;
import java.util.*;
import java.lang.*;
import java.io.*;

public class RankSort implements Comparator<RankResult>{
        //以time降序排列
        public int compare(RankResult a, RankResult b)
        {
            return a.getTime()-b.getTime();
        }
}
