package com.first.business;/*
 *创建者: zsq
 *创建时间:2020/3/21 20:40
 */

import first.bean.Protocal;
import first.com.protocol.Login.PersonLogin;
import first.com.protocol.move.PersonMove;
import first.core.context.FunctionDoName;
import io.netty.channel.ChannelHandlerContext;
import org.apache.maven.shared.utils.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

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
            Protocal protocal = new Protocal((short) SCPlayerLogin, scPlayerLogin.build().toByteArray().length, scPlayerLogin.build().toByteArray());
            x.channel().writeAndFlush(protocal);
        }
        if (StringUtils.equals(login.getUid(), "bbb")) {
            PersonLogin.SCPlayerLogin.Builder scPlayerLogin = PersonLogin.SCPlayerLogin.newBuilder();
            ArrayList<Long> arrayList = new ArrayList<>();
            arrayList.add(1234L);
            scPlayerLogin.addAllPlayerId(arrayList);
            Protocal protocal = new Protocal((short) SCPlayerLogin, scPlayerLogin.build().toByteArray().length, scPlayerLogin.build().toByteArray());
            x.channel().writeAndFlush(protocal);
        }
    }

    @FunctionDoName(CSPlayerMove)
    public void csPlayerMove(PersonMove.CSPlayerMove move, ChannelHandlerContext x) {

        PersonMove.SCPlayerMove.Builder sc = PersonMove.SCPlayerMove.newBuilder();
        sc.setPlayerId(move.getPlayerId());//
        byte[] bytes = sc.build().toByteArray();
        Protocal protocal = new Protocal((short) SCPlayerMove, bytes.length, bytes);
        x.channel().writeAndFlush(protocal);
        //FIXME 需要转发给房间的所有人
    }
}
