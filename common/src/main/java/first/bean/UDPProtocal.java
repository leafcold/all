package first.bean;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class UDPProtocal extends Protocal {
    private InetSocketAddress target;//接受者

    public UDPProtocal(short code, int length, byte[] probuffer, InetSocketAddress target) {
        super(code, length, probuffer);
        this.target = target;
    }
}
