package game.utils;
import java.io.*;
import java.security.KeyPair;
import java.util.*;

public class Ranking {
//    public static void main(String[] args){
//        try {
//            Ranking ranking = new Ranking("test1.txt");
//            ranking.writeOut("testtest");
//            ranking.readL();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
        String filePath;

    public Ranking(String filePath) throws IOException {
        this.filePath = filePath;
    }

    public void writeOut(String output){
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(output); //寫入字串
//            bufferedWriter.write(Name);
//            bufferedWriter.newLine(); //換行
//            bufferedWriter.write(time);
//            bufferedWriter.newLine();
            bufferedWriter.flush(); //將緩衝數據寫到文件去
            bufferedWriter.close(); //關閉使用BufferedWriter
        }catch (IOException e){
            System.out.println(e);
        }
        }

        public ArrayList<String> readL(){
            ArrayList<String> strings=new ArrayList<>();
            try {
                FileReader fr=new FileReader(filePath);    //建立檔案內容丟到實體fr
                BufferedReader br=new BufferedReader(fr); //將實體fr丟到讀取器br
                String line;
                while ((line=br.readLine()) != null) {    //若檔案內容不為null就執行
                    //readLine:讀取檔案每行資料，傳回為String，自動刪除跳行符號，因此字會擠在一起
                    //read: 讀取檔案一次一個字元傳回為int，因此如果要秀出內容還要做強制轉型為char，System.out.print((char)a)
                    strings = new ArrayList<>(Arrays.asList(line.split(",")));
                }
                br.close();
            }
            catch (IOException e) {System.out.println(e);} //出錯就秀出錯誤
            return strings;
        }
}
