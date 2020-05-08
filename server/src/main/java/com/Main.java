package com;

import first.business.Test;
import first.core.log.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.InvocationTargetException;

import static first.core.context.Context.initBeans;
import static first.core.net.http.HttpNetty.initHttpNetty;
import static first.core.net.tcp.TcpNetty.initTcpNetty;
import static first.core.net.udp.UdpKcp.initUdpNetty;
import static first.http.HttpDemo.readFileByLines;
import static second.Define.*;
import static second.Define.httpPort;

/**
 *
 * 构建好了网络层 TCP
 * 再构建协议层
 * 数据库层
 * 最后就是具体逻辑
 *
 */
@SpringBootApplication
@ComponentScan({"first"})
public class Main {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
        //readFileByLines("C:\\Users\\Administrator\\Desktop\\all\\server\\src\\main\\resources\\html\\passwd");
        SpringApplication application = new SpringApplication(Main.class);
        ConfigurableApplicationContext context= application.run(args);

        initBeans(context,Main.class);
        new Thread(Main::tcpNetty,"tcpNetty").start();
        new Thread(Main::httpNetty,"initHttpNetty").start();
        new Thread(Main::UDPNetty,"UDPNetty").start();
        Logger.MLOG.info("111");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("do ShutdownHook.......... ");
            }
        });
    }


    private static void UDPNetty(){ initUdpNetty(); }

    private static void httpNetty(){
        initHttpNetty(httpPort);
    }

    private static void tcpNetty() {
        initTcpNetty(tcpPort);
    }
}