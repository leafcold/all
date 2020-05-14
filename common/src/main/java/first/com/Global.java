package first.com;

import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Global {
    // 存放一些公共资源

    public static NioDatagramChannel serverChannel = null;//服务器只有一个channel
    // 调度线程
    public static FiberScheduler fiberScheduler = new FiberForkJoinScheduler("roomScheduler", 8);


}
