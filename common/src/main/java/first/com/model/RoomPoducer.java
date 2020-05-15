package first.com.model;

import com.google.common.util.concurrent.Runnables;
import first.com.Global;
import first.core.net.udp.UDPSenderCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class RoomPoducer extends Thread {

    // 缓存玩家
    private final BlockingQueue<Player> playersBlockingQueue;


    private final List<Player> playerList;//满2个人开一个房间

    public RoomPoducer() {
        playersBlockingQueue=new ArrayBlockingQueue<>(100);
        playerList = new ArrayList<>();

    }

    public void addPlayer(Player player) {
        if (player == null) {
            return;
        }
        try {
            playersBlockingQueue.add(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readygo(){
        //初始化一个房间
        Room room = new FiberRoom(Global.fiberScheduler, new ArrayList<>(), UUID.randomUUID().toString());
        for (Player player : playerList) {
            room.addPlayer(player);
        }
        room.notice();//通知
        room.open();//开启tick
    }







    @Override
    public void run() {
        while(true){
            try {
                Player take = playersBlockingQueue.take();
                playerList.add(take);
                if(playerList.size()==2){
                    readygo();//准备好了房间
                    playerList.clear();//清空
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
