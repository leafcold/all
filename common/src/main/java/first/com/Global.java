package first.com;

import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import first.com.model.RoomPoducer;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global {
    // 存放一些公共资源

    public static NioDatagramChannel serverChannel = null;//服务器只有一个channel
    // 调度线程
    public static FiberScheduler fiberScheduler = new FiberForkJoinScheduler("roomScheduler", 8);

    //房间生产者
    public static RoomPoducer roomPoducer = new RoomPoducer();

}
