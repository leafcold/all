package first.core.net.udp;

import first.com.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * channel的地址
 */
public class UDPSenderCache {
     private  static final Map<Long, Player> senderCache = new HashMap<>();

    public static void put(Long key, Player value) {
        senderCache.put(key, value);

    }

    public static void remove(Long key) {
        senderCache.remove(key);
    }

    public static Player get(Long key){
        return senderCache.get(key);
    }

    public static int size(){
        return senderCache.size();
    }

    public static Map<Long, Player> getData(){
        return senderCache;
    }
}
