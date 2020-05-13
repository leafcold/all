import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class genCode {
    public static final String ENCODING = "utf-8";

    public static void main(String[] args) {
        File dir = new File("tools\\protoc-3.11.4-win64\\file");
        File[] fs = dir.listFiles();
        if(fs == null){
            return;
        }
        List<String> code = new ArrayList<String>();
        BufferedReader bufferedReader;
        for (File f : fs) {
            try {
                if (!f.isDirectory()) {
                    String fileName = f.getName();
                    String fileSuffix = fileName.split("\\.")[1];
                    if ("proto".equals(fileSuffix)) {
                        InputStreamReader read = new InputStreamReader(new FileInputStream(f), ENCODING);
                        bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            String str = lineTxt.trim();
                             if(str.contains("message SC")||str.contains("message CS")||str.contains("message SS")){
                                 System.out.println("public final static  short"+
                                         (str.replaceAll("message","").
                                                 replaceAll("/","").
                                                 replaceAll("\\{"," = ").replaceAll("-",";//")));
                             }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
