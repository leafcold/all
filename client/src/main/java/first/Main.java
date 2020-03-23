package first;/*
 *创建者: zsq
 *创建时间:2020/3/18 14:38
 */

import first.bean.Protocal;
import first.com.protocol.move.PersonMove;
import first.core.TimeClientHandler;
import first.core.net.tcp.DecoderHandler;
import first.core.net.tcp.EncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = "172.16.2.24";
        int port = 12306;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DecoderHandler());
                    ch.pipeline().addLast(new TimeClientHandler());
                    ch.pipeline().addLast(new EncoderHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            PersonMove.Person.Builder person =PersonMove.Person.newBuilder();
            person.setId(100);
            person.setEmail("bat文件获取当前目录\n" + "转载千羽12138 最后发布于2018-08-22 15:48:10 阅读数 13748  收藏\n" + "展开\n" + "转自http://blog.chinaunix.net/uid-20449851-id-133998.html\n" + "\n" + "（win10）上代码：\n" + "\n" + "cd /d %~dp0\n" + "\n" + "切换到当前bat文件所在目录\n" + "\n" + " \n" + "\n" + "原文：\n" + "\n" + "知道windows的bat脚本很强大，但是具体命令向来很少接触，今天在win7上运行自己以前写的一个安装mysql数据库到系统服务的脚本时，遇到一些问题，下面记录一下。\n" + "\n" + " \n" + "\n" + "问题：mysql服务安装脚本在win7下运行失败\n" + "\n" + " \n" + "\n" + "原因：win7下，以右键的“以管理员身份运行”默认进入的目录是C:\\Windows\\System32目录，因此后续的cd Demo_V2.0.4.9命令肯定进入不到正确的目录\n" + "\n" + " \n" + "\n" + "解决办法：使用cd /d %~dp0\n" + "\n" + "首先，脚本如下，对具体StartDemo.bat命令不多做解释\n" + "\n" + "cd Demo_V2.0.4.9\n" + "\n" + "cd mysql\n" + "\n" + " \n" + "\n" + "@echo off\n" + "\n" + "set MYSQL_HOME=%cd%\n" + "\n" + " \n" + "\n" + "net stop \"DemoMySql\"\n" + "\n" + "call \"%MYSQL_HOME%\\bin\\mysqld.exe\" remove  DemoMySql\n" + "\n" + " \n" + "\n" + "call \"%MYSQL_HOME%\\bin\\mysqld.exe\" install DemoMySql --defaults-file=\"%MYSQL_HOME%\\bin\\my.ini\"\n" + "\n" + "net start \"DemoMySql\"\n" + "\n" + " \n" + "\n" + "cd..\n" + "\n" + "cd..\n" + "\n" + " \n" + "\n" + "这个脚本在xp、2000、2003等系统中都可以正常双击运行。在win7系统中双击运行时，会以普通用户身份运行，此时所获取的文件路径的确是当前路径，而不是C:\\Windows\\System32。但是运行到卸载以及安装DemoMysql的系统服务时，普通用户显然权限是不够的。\n" + "\n" + "于是在StartDemo.bat右键选择“以管理员身份运行”，此时又会出问题，win7可能出于安全问题考虑，此时获得的目录是C:\\Windows\\System32，于是后面的执行都会出错或者无效。\n" + "\n" + "此时在脚本开始尝试加入命令cd %cd%，来获取当前路径，实验得知，这行语句在xp等系统中有效，但是在win7中依然无效。得到的目录依然是C:\\Windows\\System32。\n" + "\n" + "上网查了一下才知道要使用cd /d %~dp0命令来获取脚本所在的目录。在脚本最开始添加cd /d %~dp0即可。之后在xp系统上运行此脚本，确认也没有问题。下面对命令中涉及到的参数做一解释。\n" + "\n" + "问题解释一：关于cd的/d参数\n" + "关于cd 的/d参数，在cmd中敲入cd /?\n" + "\n" + "可以看到/d参数的解释如下：\n" + "\n" + "使用 /D 命令行开关，除了改变驱动器的当前目录之外，\n" + "\n" + "还可改变当前驱动器。\n" + "\n" + " \n" + "\n" + "这句话貌似不太好理解，我做个试验给大家看就明白了：\n" + "\n" + "通常我们在xp系统中打开cmd窗口时，会显示\n" + "\n" + "C:\\Documents and Settings\\Administrator>\n" + "\n" + "如果我们执行如下命令，发现目录依然还是在C:\\Documents and Settings\\Administrator\n" + "\n" + "C:\\Documents and Settings\\Administrator>cd d:\\tomcat6.0.18\n" + "\n" + " \n" + "\n" + "C:\\Documents and Settings\\Administrator>\n" + "\n" + "此时，我们键入d:，不但会切换到d盘，而且会切换到d:/tomcat6.0.18的目录\n" + "\n" + "C:\\Documents and Settings\\Administrator>cd d:\\tomcat6.0.18\n" + "\n" + " \n" + "\n" + "C:\\Documents and Settings\\Administrator>d:\n" + "\n" + " \n" + "\n" + "D:\\tomcat6.0.18>\n" + "\n" + "下面语句我们就能看到/d参数的作用了。发现加了/d参数之后直接切换到d盘的tomcat6.0.18目录了。\n" + "\n" + "C:\\Documents and Settings\\Administrator>cd /d d:\\tomcat6.0.18\n" + "\n" + " \n" + "\n" + "D:\\tomcat6.0.18>\n" + "\n" + " \n" + "\n" + "结论：不加/d参数只能在同一驱动器的目录之间切换，加上/d参数则能在不同驱动器之间的目录之间切换\n" + "\n" + " \n" + "\n" + "问题解释二：关于%~dp0的批处理命令的详细解释\n" + "对此命令并不清楚，以下内容都来自互联网：\n" + "\n" + "%~dp0 “d”为Drive的缩写，即为驱动器，磁盘、“p”为Path缩写，即为路径，目录\n" + "\n" + "cd是转到这个目录，不过我觉得cd /d %~dp0 还好些\n" + "\n" + " \n" + "\n" + "选项语法:\n" + "\n" + "    ~0         - 删除任何引号(\")，扩充 %0\n" + "\n" + "    %~f0        - 将 %0 扩充到一个完全合格的路径名(“f”是file，即文件)\n" + "\n" + "    %~d0        - 仅将 %0 扩充到一个驱动器号\n" + "\n" + "    %~p0        - 仅将 %0 扩充到一个路径\n" + "\n" + "    %~n0        - 仅将 %0 扩充到一个文件名(“n”是name 文件名)\n" + "\n" + "    %~x0        - 仅将 %0 扩充到一个文件扩展名\n" + "\n" + "    %~s0        - 扩充的路径只含有短名(“s”为Short，短的)\n" + "\n" + "    %~a0        - 将 %0 扩充到文件的文件属性(“a”为attribute，即属性)\n" + "\n" + "    %~t0        - 将 %0 扩充到文件的日期/时间(“t”time)\n" + "\n" + "    %~z0        - 将 %0 扩充到文件的大小(Size 大小)\n" + "\n" + "    %~$PATH:0   - 查找列在路径环境变量的目录，并将 %0 扩充\n" + "\n" + "                  到找到的第一个完全合格的名称。如果环境变量名\n" + "\n" + "                  未被定义，或者没有找到文件，此组合键会扩充到\n" + "\n" + "                  空字符串\n" + "\n" + "可以组合修饰符来得到多重结果:\n" + "\n" + "    %~dp0       - 仅将 %0 扩充到一个驱动器号和路径\n" + "\n" + "    %~nx0       - 仅将 %0 扩充到一个文件名和扩展名\n" + "\n" + "    %~fs0       - 仅将 %0 扩充到一个带有短名的完整路径名\n" + "\n" + "    %~dp$PATH:0 - 查找列在路径环境变量的目录，并将 %I 扩充\n" + "\n" + "                  到找到的第一个驱动器号和路径。\n" + "\n" + "    %~ftza0     - 将 %0 扩充到类似输出线路的 DIR\n" + "\n" + "%0为当前批处理文件\n" + "\n" + "如果0换成1为第一个文件，2为第2个\n" + "\n" + " \n" + "\n" + "%0代指批处理文件自身\n" + "\n" + "%~d0 是指批处理所在的盘符\n" + "\n" + "%~dp0 是盘符加路径\n" + "\n" + " \n" + "\n" + "cd %~dp0 就是进入批处理所在目录了");
            person.setName("my");
            PersonMove.Person nowPerSon=person.build();
            Protocal protocal=new Protocal((short) 1,nowPerSon.toByteArray().length,nowPerSon.toByteArray());
            f.channel().writeAndFlush(protocal.toArray());

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
