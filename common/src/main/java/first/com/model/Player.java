package first.com.model;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class Player {
    private long playerId;
    private InetSocketAddress address;
    private Room room;
    private boolean die; //是否死亡

    public Player(long playerId, InetSocketAddress address) {
        this.playerId = playerId;
        this.address = address;
    }

    void addRoom(Room room) {
        this.room = room;
    }
}
