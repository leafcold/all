package first.business;/*
 *创建者: zsq
 *创建时间:2020/3/21 20:40
 */

import first.com.protocol.move.PersonMove;
import first.core.context.FunctionDoName;
import org.springframework.stereotype.Controller;

import static first.core.log.Logger.MLOG;


@Controller
public class Test {
     @FunctionDoName(1)
     public void test(PersonMove.Person person){
         MLOG.info("person:{}",person);
     }
//    @FunctionDoName(1)
//    public void test(PersonMove.Person person){
//        MLOG.info("person:{}",person);
//    }
}
