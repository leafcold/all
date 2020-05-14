package first.com.model;

import first.com.protocol.move.PersonMove;

/**
 * 代表游戏内的一张图
 */
public interface Room {

    void notice();//通知两个玩家，连接成功

    void open();//开放一个room ，通常是第一个人来了

    void close(); // game over

    void addPlayer(Player player);//目前是来两个人了 同时进入房间

    void addMoveMsg(PersonMove.CSPlayerMove move); // 移动消息

}
