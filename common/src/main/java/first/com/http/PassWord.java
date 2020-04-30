package first.com.http;/*
 *创建者: zsq
 *创建时间:2020/4/18 15:57
 */

import first.core.net.http.ParseUrlUtil;
import lombok.Data;

@Data
public class PassWord {
     private String user;
     
     private String oldPassword;
     
     private String nowPassword;

     private String newPasswordAgain;

    public static PassWord parseFrom(ParseUrlUtil string) {
        PassWord passWord = new PassWord();
        passWord.setNowPassword(string.getStrUrlParas().get("new_password"));
        passWord.setNewPasswordAgain(string.getStrUrlParas().get("new_password_again"));
        passWord.setUser(string.getStrUrlParas().get("user"));
        passWord.setOldPassword(string.getStrUrlParas().get("old_password"));
        return passWord;
    }
     
     
}
