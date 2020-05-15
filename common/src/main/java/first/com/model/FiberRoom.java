package first.com.model;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import first.bean.Protocal;
import first.com.Global;
import first.com.protocol.Login.PersonLogin;
import first.com.protocol.move.PersonMove;
import first.core.log.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static first.core.invoke.Code.SCPlayerMove;
import static first.core.invoke.Code.SCUDP;


/**
 * 一个房间 是一个 Fiber，即 协程 ，坑很多 ，需要踩
 * <p>
 * 在 VM options 中加入参数 -javaagent:D://quasar-core-0.8.0.jar
 */
public class FiberRoom extends Fiber<Void> implements Room {
    private List<Player> players;//房间内的人
    private String roomId;
    private SwapableQueuePair<PersonMove.CSPlayerMove> moves;
    private final PersonMove.SCPlayerMove.Builder sc = PersonMove.SCPlayerMove.newBuilder();
    private boolean gameOver = false; //刚开始


    /**
     * 初始化一个房间
     *
     * @param scheduler 调度线程
     * @param players   房间的人
     */
    public FiberRoom(FiberScheduler scheduler, List<Player> players, String roomId) {
        super("Fiber_" + roomId, scheduler);
        this.roomId = roomId;
        this.players = players;
        this.moves = new SwapableQueuePair<>();
    }

    @Override
    public Void run() throws SuspendExecution, InterruptedException {
        Logger.MLOG.info("#######room:[" + roomId + "]tick start########");
        while (!gameOver) {
            moves.swap();
            Queue<PersonMove.CSPlayerMove> dealQueue = moves.first();
            while (!dealQueue.isEmpty()) {
                PersonMove.CSPlayerMove msg = dealQueue.poll();
                if (null == msg) {
                    break;
                }
                sc.setPlayerId(msg.getPlayerId());
                sc.setMove(msg.getMove());
                long sTime = System.currentTimeMillis(); //服务器的时间
                sc.getMove().toBuilder().setStime(sTime);
                byte[] bytes = sc.build().toByteArray();
                Protocal protocal = new Protocal(SCPlayerMove, bytes.length, 0, bytes);

                for (Player player : this.players) {
                    protocal.setPid(player.getPlayerId());
                    Global.serverChannel.writeAndFlush(protocal);
                }
//                gameOver = true;
                Logger.MLOG.info("msg" + msg + "发送成功");
            }
            Strand.sleep(40);

        }
        return null;
    }


    @Override
    public void notice() { //通知玩家准备进入
        PersonLogin.SCUDP.Builder sc = PersonLogin.SCUDP.newBuilder();
        for (Player player : players) {
            sc.addPlayerId(player.getPlayerId());
        }
        byte[] bytes = sc.build().toByteArray();
        for (Player player : players) {
            Protocal protocal = new Protocal(SCUDP, bytes.length, player.getPlayerId(), bytes);
            Global.serverChannel.writeAndFlush(protocal);
        }

    }

    @Override
    public void open() {
        this.start();
    }

    @Override
    public void close() {
        for (Player player : players) {
            player.setRoom(null);//让玩家不在房间
        }
        this.players.clear();//清空房间的人
        this.cancel(true);
    }

    @Override
    public void addPlayer(Player player) {
        player.addRoom(this); //互相持有引用
        this.players.add(player);
    }

    @Override
    public void addMoveMsg(PersonMove.CSPlayerMove move) {
        if (gameOver) {
            return;//如果游戏结束，不再广播消息
        }
        moves.second().add(move);
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public SwapableQueuePair<PersonMove.CSPlayerMove> getMoves() {
        return moves;
    }

    public void setMoves(SwapableQueuePair<PersonMove.CSPlayerMove> moves) {
        this.moves = moves;
    }
}
