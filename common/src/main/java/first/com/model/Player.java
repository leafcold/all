package first.com.model;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class Player {
    private long playerId;
    private InetSocketAddress address;
    private Room room;

    public Player(long playerId, InetSocketAddress address) {
        this.playerId = playerId;
        this.address = address;
    }

    void addRoom(Room room) {
        this.room = room;
    }
}
