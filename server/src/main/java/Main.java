import first.business.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.InvocationTargetException;

import static first.core.context.Context.initBeans;
import static first.core.net.tcp.TcpNetty.initTcpNetty;

/**
 *
 * 构建好了网络层 TCP
 * 再构建协议层
 * 数据库层
 * 最后就是具体逻辑
 *
 */
@SpringBootApplication
@ComponentScan({"first.business"})
public class Main {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {

        SpringApplication application = new SpringApplication(Main.class);
        ConfigurableApplicationContext context= application.run(args);

        initBeans(context,Main.class);
        initTcpNetty();
        Thread.currentThread().wait();
    }
}
