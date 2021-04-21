package game.scene;

import game.utils.CommandSolver;

import java.awt.*;
import java.io.*;

public class Ranking extends Scene{
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;

    public Ranking() throws IOException {
        fileWriter = new FileWriter("D:\\Buffered.txt");
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void writeOut(String Name, int time) throws IOException {
            bufferedWriter.write("Name"); //寫入字串
            bufferedWriter.newLine(); //換行
            bufferedWriter.write("Record");
            bufferedWriter.flush(); //將緩衝數據寫到文件去
            bufferedWriter.close(); //關閉使用BufferedWriter
        }

        public void ReadIn() {
            try {
                FileReader fr=new FileReader("D:\\Buffered.txt");    //建立檔案內容丟到實體fr
                BufferedReader br=new BufferedReader(fr); //將實體fr丟到讀取器br
                String line;
                while ((line=br.readLine()) != null) {    //若檔案內容不為null就執行
                    //readLine:讀取檔案每行資料，傳回為String，自動刪除跳行符號，因此字會擠在一起
                    //read: 讀取檔案一次一個字元傳回為int，因此如果要秀出內容還要做強制轉型為char，System.out.print((char)a)
                    System.out.print(line);
                }
            }
            catch (IOException e) {System.out.println(e);} //出錯就秀出錯誤
        }


    @Override
    public void sceneBegin() {

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
