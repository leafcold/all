package first.http;/*
 *创建者: zsq
 *创建时间:2020/4/16 23:34
 */

import first.com.http.FirstHttp;
import first.com.http.PassWord;
import first.core.context.HttpContext;
import javafx.util.Pair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static first.core.log.Logger.MLOG;

@Controller
public class HttpDemo {

    static String path="/home/svn/project/conf/passwd";
    @HttpContext("/value")
    public String test(FirstHttp firstHttp) throws IOException {
        Resource resource = new ClassPathResource("html/index.html");
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String dateValue="";
        String data = null;
        while((data = br.readLine()) != null) {
            dateValue=dateValue.concat(data);
        }
        br.close();
        isr.close();
        is.close();
        return dateValue;
    }
    @HttpContext("/css/style.css")
    public String test1(FirstHttp firstHttp) throws IOException {
        Resource resource = new ClassPathResource("html/css/style.css");
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String dateValue="";
        String data = null;
        while((data = br.readLine()) != null) {
            dateValue=dateValue.concat(data);
        }
        br.close();
        isr.close();
        is.close();
        return dateValue;
    }

    @HttpContext("/js/jquery.min.js")
    public String test2(FirstHttp firstHttp) throws IOException {
        Resource resource = new ClassPathResource("html/change.html");
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String dateValue="";
        String data = null;
        while((data = br.readLine()) != null) {
            dateValue=dateValue.concat(data);
        }
        br.close();
        isr.close();
        is.close();
        return dateValue;
    }


    @HttpContext("/passWorld")
    public String changePassword(PassWord passWord) throws IOException {

        Resource resource = new ClassPathResource("html/show.html");
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String dateValue="";
        String data = null;
        while((data = br.readLine()) != null) {
            dateValue=dateValue.concat(data);
        }
        if(passWord.getNewPasswordAgain()==null||passWord.getNowPassword()==null||passWord.getOldPassword()==null||passWord.getUser()==null) {
            dateValue=dateValue.replace("${1}","error");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }
        if(contains(passWord.getNewPasswordAgain())||contains(passWord.getNowPassword())||contains(passWord.getOldPassword())||contains(passWord.getUser()))
        {
            dateValue=dateValue.replace("${1}","包含特殊字符不符合");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }
        if(!passWord.getNewPasswordAgain().equals(passWord.getNowPassword())){
            dateValue=dateValue.replace("${1}","账户名密码不符");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }
        //开始读取文件
        Map<String, Pair<Integer,String>> map =readFileByLines(path);
        if(!map.containsKey(passWord.getUser()))
        {
            dateValue=dateValue.replace("${1}","账户无");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }
        if(!map.get(passWord.getUser()).getValue().equals(passWord.getOldPassword()))
        {
            dateValue=dateValue.replace("${1}","密码不对");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }
        //开始修改密码 已经存在 重新再读一次
        String oldString=passWord.getUser()+" = "+passWord.getOldPassword();
        String newString=passWord.getUser()+" = "+passWord.getNowPassword();
        if(!replaceTxtByLineNo(path,map.get(passWord.getUser()).getKey(),newString,oldString)){
            dateValue=dateValue.replace("${1}","解析错误");
            br.close();
            isr.close();
            is.close();
            return dateValue;
        }

        dateValue=dateValue.replace("${1}","成功");
        br.close();
        isr.close();
        is.close();
        return dateValue;
    }

    public static Map<String, Pair<Integer,String>> readFileByLines(String fileName) {
        File file = new File(fileName);
        Map<String, Pair<Integer,String>> map=new HashMap<>();
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                tempString = tempString.trim();
                if(tempString.length()>0&&tempString.charAt(0)=='#') {
                    line++;
                    continue;
                }
                //String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
                String strings[]=tempString.split("=");
                if(strings.length!=2) {
                    line++;
                    continue;
                }
                String name =strings[0];
                String password=strings[1];
                name=name.replaceAll(" ","");
                password=password.replaceAll(" ","");
                Pair<Integer,String> pair=new Pair<>(line,password);
                map.put(name,pair);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return map;
    }
    //判断一个字符串 是否包含特殊字符
    public Boolean contains(String s) {
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        for(int i=0;i<regEx.length();i++)
        {
            if(s.contains(regEx.charAt(i)+""))
            {
                return true;
            }
        }
        return false;
    }

    public static Boolean replaceTxtByLineNo(String path,int lineNo,String newStr,String oldStr) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<String> replaced = new ArrayList<>();
        int lineNum = 1;
        int flag=0;
        for (String line : lines) {
            if (lineNum == lineNo&&line.contains(oldStr)) {
                flag=1;
                replaced.add(newStr);
            } else {
                replaced.add(line);
            }
            lineNum++;
        }
        if(flag==0)
        {
            return false;
        }
        Files.write(Paths.get(path), replaced);
        return true;
    }

}
