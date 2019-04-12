package com.lenovo.nfv.snmp;

import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * 监听Agent发送的Trap信息
 *
 * @author wangguan3
 */
public class MultiThreadedTrapReceiver implements CommandResponder {
    private String username1 = "Administrator";
    private String username2 = "user2";
    private String authPassword = "Admin@9000";
    private String privPassword = "LXCA_9000";
    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;

    public MultiThreadedTrapReceiver() {
//        BasicConfigurator.configure();
    }

    private void init() throws UnknownHostException, IOException {
        threadPool = ThreadPool.create("Trap", 2);  //创建接收SnmpTrap的线程池（参数： 线程名称、线程数）
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:10.116.56.37/162"));  //监听端的ip地址和监听端口号

        TransportMapping<?> transport;
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);

        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        //MPv3.setEnterpriseID(35904);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());

        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        usm.setEngineDiscoveryEnabled(true);

        SecurityModels.getInstance().addSecurityModel(usm);
        // 添加安全协议,如果没有发过来的消息没有身份认证,可以跳过此段代码
        SecurityProtocols.getInstance().addDefaultProtocols();

        // 创建并添加用户
        OctetString userName1 = new OctetString(username1);
//        OctetString userName2 = new OctetString(username2);
        OctetString authPass = new OctetString(authPassword);
        OctetString privPass = new OctetString("privPassword");
        UsmUser usmUser1 = new UsmUser(userName1, AuthSHA.ID, authPass, PrivAES128.ID, privPass);
//        UsmUser usmUser1 = new UsmUser(userName1, AuthSHA.ID, authPass, null, null);
//        UsmUser usmUser1 = new UsmUser(userName1, AuthMD5.ID, authPass, PrivDES.ID, privPass);
//        UsmUser usmUser2 = new UsmUser(userName2, AuthMD5.ID, authPass, PrivDES.ID, privPass);
        //因为接受的Trap可能来自不同的主机，主机的Snmp v3加密认证密码都不一样，所以根据加密的名称，来添加认证信息UsmUser。添加了加密认证信息的便可以接收来自发送端的信息。
        snmp.getUSM().addUser(usmUser1);
//        UsmUserEntry userEnty1 = new UsmUserEntry(userName1, usmUser1);
//        UsmUserEntry userEnty2 = new UsmUserEntry(userName2, usmUser2);
//        UsmUserTable userTable = snmp.getUSM().getUserTable();
//        userTable.addUser(userEnty1);
//        userTable.addUser(userEnty2);
        snmp.listen();  //开启Snmp监听，可以接收来自Trap端的信息。

       /* UsmUserTable userTable = snmp.getUSM().getUserTable();
        userTable.addUser(userEnty1);
        userTable.addUser(userEnty2);*/

        /*threadPool = ThreadPool.create("Trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:10.116.57.157/162")); // 本地IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理  
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        UsmUser usmUser = new UsmUser(new OctetString(username), AuthMD5.ID, new OctetString(authPassword), Priv3DES.ID, new OctetString(privPassword));
        USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(), new OctetString(MPv3.createLocalEngineID()), 0);
        usm.addUser(usmUser);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();

        //add the enginID in the trap 
        //OctetString engineID = new OctetString(MPv3.createLocalEngineID()); 
        byte[] enginId = "TEO_ID".getBytes();
        OctetString engineID = new OctetString(enginId);
        //create and add the userSecurityModel 
        USM usm = new USM(SecurityProtocols.getInstance(), engineID, 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        //add the securityProtocols,you can skip it if your users are noAuthNoPriv 
        SecurityProtocols.getInstance().addDefaultProtocols();

        //create and add the user 
        UsmUser usmUser = new UsmUser(new OctetString(username), AuthMD5.ID, new OctetString(authPassword), Priv3DES.ID, new OctetString(privPassword));
        usm.addUser(usmUser);

        //snmp.getUSM().addUser(usmUser); 
        snmp.listen();*/
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
            System.out.println("开始监听Trap信息!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 实现CommandResponder接口的processPdu方法, 用于处理传入的请求、PDU等信息。当接收到trap时，会自动进入这个方法。
     *
     * @param respEvnt
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        System.out.println("a new trap coming：" + respEvnt.getSecurityLevel());
        if (respEvnt != null && respEvnt.getPDU() != null) {
            Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getPDU().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static void main(String[] args){
        MultiThreadedTrapReceiver multithreadedtrapreceiver = new MultiThreadedTrapReceiver();
        multithreadedtrapreceiver.run();     //start monitor
    }
}