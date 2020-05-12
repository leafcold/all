import com.google.protobuf.InvalidProtocolBufferException;
import first.com.protocol.move.PersonMove;


public class Main {
    public static void main(String[] args) throws InvalidProtocolBufferException {


        PersonMove.Person.Builder person= PersonMove.Person.newBuilder();
        person.setName("111");
        person.setId(111);
        PersonMove.Person.PhoneNumber.Builder tmpPerson=PersonMove.Person.PhoneNumber.newBuilder();
        tmpPerson.setNumber("num");
        person.addPhones(tmpPerson.build());
        PersonMove.Person tmp=person.build();
        byte[] bytes=tmp.toByteArray();
        PersonMove.Person object = PersonMove.Person.parseFrom(bytes);
        Object(object);

    }
    public static void Object(Object test)
    {
        System.out.println(((PersonMove.Person)test).getName());
    }
}
