package com.first.business;/*
 *创建者: zsq
 *创建时间:2020/3/21 20:40
 */

import first.bean.Protocal;
import first.com.Global;
import first.com.model.FiberRoom;
import first.com.model.Player;
import first.com.model.Room;
import first.com.protocol.Login.PersonLogin;
import first.com.protocol.move.PersonMove;
import first.core.context.FunctionDoName;
import first.core.log.Logger;
import first.core.net.udp.UDPSenderCache;
import first.core.net.udp.UdpKcp;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.maven.shared.utils.StringUtils;
import org.springframework.stereotype.Controller;

import java.net.FileNameMap;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static first.core.invoke.Code.*;


@Controller
public class Test {
//     @FunctionDoName(1)
//     public void test(PersonMove.Person perso, ChannelHandlerContext x){
//         MLOG.info("person:{}",perso);
////         PersonLogin.SCPlayerLogin.Builder ps=PersonLogin.SCPlayerLogin.newBuilder();
////         ps.addPlayerId(1000L);
////         int lenth=ps.build().toByteArray().length;
////         byte[] arr=ps.build().toByteArray();
////         Protocal protocal =new Protocal((short) 3,lenth,arr);
////         x.channel().writeAndFlush(protocal);
//         PersonMove.Person.Builder person =PersonMove.Person.newBuilder();
//         person.setId(100);
//         //person.setEmail((System.currentTimeMillis()));
//         person.setName("my");
//         PersonMove.Person nowPerSon=person.build();
//         Protocal protocal=new Protocal((short) 1,nowPerSon.toByteArray().length,nowPerSon.toByteArray());
//         x.channel().writeAndFlush(protocal );
//         x.channel().writeAndFlush(protocal);
//
//     }


    @FunctionDoName(CSPlayerLogin)
    public void csplayerLogin(PersonLogin.CSPlayerLogin login, ChannelHandlerContext x) {
        if (StringUtils.equals(login.getUid(), "aaa")) {
            PersonLogin.SCPlayerLogin.Builder scPlayerLogin = PersonLogin.SCPlayerLogin.newBuilder();
            ArrayList<Long> arrayList = new ArrayList<>();
            arrayList.add(123L);
            scPlayerLogin.addAllPlayerId(arrayList);
            Protocal protocal = new Protocal((short) SCPlayerLogin, scPlayerLogin.build().toByteArray().length, 123L, scPlayerLogin.build().toByteArray());
            x.channel().writeAndFlush(protocal);
        }
        if (StringUtils.equals(login.getUid(), "bbb")) {
            PersonLogin.SCPlayerLogin.Builder scPlayerLogin = PersonLogin.SCPlayerLogin.newBuilder();
            ArrayList<Long> arrayList = new ArrayList<>();
            arrayList.add(1234L);
            scPlayerLogin.addAllPlayerId(arrayList);
            Protocal protocal = new Protocal((short) SCPlayerLogin, scPlayerLogin.build().toByteArray().length, 1234L, scPlayerLogin.build().toByteArray());
            x.channel().writeAndFlush(protocal);
        }
    }

    @FunctionDoName(CSPlayerMove)
    public void csPlayerMove(PersonMove.CSPlayerMove move, ChannelHandlerContext x) {
        Logger.MLOG.info("move");
        long playerId = move.getPlayerId();
        Room room = UDPSenderCache.get(playerId).getRoom();
        if (room != null) {
            room.addMoveMsg(move);
        } else {
            Logger.MLOG.info("玩家没有进入房间");
        }
        Logger.MLOG.info("endMove");
    }

    @FunctionDoName(CSUDP)
    public void csUdp(PersonLogin.CSUDP csudp, ChannelHandlerContext cx) {
        Logger.MLOG.info("connect");
        if (UDPSenderCache.size() >= 2) { //TODO 先这么写 没有想好 怎么分配房间
            //初始化一个房间
            Room room = new FiberRoom(Global.fiberScheduler, new ArrayList<>(), UUID.randomUUID().toString());
            Map<Long, Player> data = UDPSenderCache.getData();
            for (Map.Entry<Long, Player> longPlayerEntry : data.entrySet()) {
                room.addPlayer(longPlayerEntry.getValue());
            }
            room.notice();//通知
            room.open();//开启tick
        }
        Logger.MLOG.info("end connect");
    }
}
