package first.core.net.udp;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * channel的地址
 */
public class UDPSenderCache {
     private  static final Map<Channel, InetSocketAddress> senderCache = new HashMap<>();

    public static void put(Channel key, InetSocketAddress value) {
        senderCache.put(key, value);

    }

    public static void remove(Channel key) {
        senderCache.remove(key);
    }

    public static InetSocketAddress get(Channel key){
        return senderCache.get(key);
    }
}
