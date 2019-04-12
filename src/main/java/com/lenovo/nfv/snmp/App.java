package com.lenovo.nfv.snmp;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangguan3
 * snmptrap v1、v2、v3 simulator
 */
public class App {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /*static {
        LogFactory.setLogFactory(new Log4jLogFactory());
        BasicConfigurator.configure();
        LogFactory.getLogFactory().getRootLogger().setLogLevel(LogLevel.ALL);
    }*/

    public static void main(String[] args) {
        String targetIP = "10.116.56.37";
        int trapNum = 1;
        for (String arg : args) {
            if (arg.contains(".")) {
                targetIP = arg;
            } else {
                trapNum = Integer.valueOf(arg);
            }
        }
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostIP = addr.getHostAddress().toString();
            Address targetAddress = GenericAddress.parse("udp:" + targetIP + "/162");      //目标主机的ip地址、端口号
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();
            for (int i = 0; i < trapNum; i++) {
                executorService.execute(new SnmpTrapSender(targetAddress, hostIP, snmp, i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
