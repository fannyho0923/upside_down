package game.scene;

import game.utils.CommandSolver;
import game.utils.Ranking;

import java.awt.*;
import java.io.IOException;

public class RankingScene extends Scene{
    private Ranking ranking;


    public RankingScene(){
    }

    @Override
    public void sceneBegin() {
        try {
            ranking = new Ranking("D:\\test1.txt");
        }catch (IOException e){
            System.out.println(e);
        }
        ranking.readL();
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }
}
