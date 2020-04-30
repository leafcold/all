package first.com.http;/*
 *创建者: zsq
 *创建时间:2020/4/16 23:57
 */

import first.core.net.http.ParseUrlUtil;

public class FirstHttp {
    private String  string;


    public static FirstHttp parseFrom(ParseUrlUtil parseUrlUtil) {
        FirstHttp firstHttp = new FirstHttp();
        return firstHttp;
    }
}
