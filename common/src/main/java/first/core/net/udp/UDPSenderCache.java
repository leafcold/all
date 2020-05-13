package first.core.net.udp;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * channel的地址
 */
public class UDPSenderCache {
     private  static final Map<Long, InetSocketAddress> senderCache = new HashMap<>();

    public static void put(Long key, InetSocketAddress value) {
        senderCache.put(key, value);

    }

    public static void remove(Long key) {
        senderCache.remove(key);
    }

    public static InetSocketAddress get(Long key){
        return senderCache.get(key);
    }

    public static int size(){
        return senderCache.size();
    }

    public static Map<Long, InetSocketAddress> getData(){
        return senderCache;
    }
}
