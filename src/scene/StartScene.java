package scene;

import Internect.ClientClass;
import Internect.Server;
import controller.SceneController;
import gameobj.StartImage;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartScene extends Scene{
    private StartImage startImage;

    @Override
    public void sceneBegin() {
        startImage = new StartImage(50,10);
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                Scanner sc = new Scanner(System.in);
                switch (commandCode) {
                    case Global.ENTER:
                        SceneController.getInstance().change(new TestSceneB());
                        break;
                    case Global.SPACE:
                        System.out.println("創建伺服器 => 1, 連接其他伺服器 => 2");
                        int opt = sc.nextInt();
                        switch (opt) {
                            case 1:
                                Global.IS_INTERNET = true;
                                Global.IS_SERVER = true;
                                //??為何一定要拉出來package
                                Server.instance().create(12345);
                                Server.instance().start();
                                System.out.println( Server.instance().getLocalAddress()[0]);
                                try {
                                    ClientClass.getInstance().connect("127.0.0.1", 12345); // ("SERVER端IP", "SERVER端PORT")
                                } catch (IOException ex) {
                                    Logger.getLogger(TestSceneB.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case 2:
                                Global.IS_INTERNET = true;
                                System.out.println("請輸入主伺服器IP:");
                                try {
                                    ClientClass.getInstance().connect(sc.next(), 12345); // ("SERVER端IP", "SERVER端PORT")
                                } catch (IOException ex) {
                                    Logger.getLogger(TestSceneB.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                        }
                        SceneController.getInstance().change(new TestSceneB());
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        startImage.paint(g);
    }

    @Override
    public void update() {

    }
}