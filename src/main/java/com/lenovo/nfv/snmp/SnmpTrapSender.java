package com.lenovo.nfv.snmp;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * 发送SNMPTrap
 *
 * @author wangguan3
 */
public class SnmpTrapSender implements Runnable {
    private Snmp snmp = null;
    private Address targetAddress = null;
    private String username1 = "user1";             //UsmUser的userName
    private String authPassword = "password1";      //认证协议的密码（如MD5）
    private String privPassword = "password2";      //加密协议密码 （如DES、AES）
    private static String hostIP = "127.0.0.1";
    private volatile int currentNum = 0;

    public SnmpTrapSender(Address targetAddress, String hostIP, Snmp snmp, int currentNum) throws IOException {
        this.targetAddress = targetAddress;
        this.hostIP = hostIP;
        this.snmp = snmp;
        this.currentNum = currentNum;
    }

    /**
     * Snmp V1 测试发送Trap
     *
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV1Trap() throws IOException {
        PDUv1 pdu = new PDUv1();
        pdu.setType(PDU.V1TRAP);
        pdu.setEnterprise(new OID(".1.3.6.1.4.1.2011.2.235.1.1.500.10.256"));
        pdu.setGenericTrap(6);
        pdu.setSpecificTrap(1);

        VariableBinding v = new VariableBinding();
        //v.setOid(SnmpConstants.sysName);
        v.setOid(new OID(".1.3.6.1.4.1.2011.2.235.1.1.500.1.1"));
        v.setVariable(new OctetString("24817776-3CE8-8E19-E811-6B1D0A3D2841"));
        pdu.add(v);
        VariableBinding v1 = new VariableBinding();
        v1.setOid(new OID(".1.3.6.1.4.1.2011.2.235.1.1.500.1.3"));
        v1.setVariable(new OctetString("HUAWEI SNMPV1Trap from " + hostIP + " n" + currentNum));
        pdu.add(v1);
        VariableBinding v2 = new VariableBinding();
        v2.setOid(new OID(".1.3.6.1.4.1.2011.2.235.1.1.500.1.4"));
        v2.setVariable(new OctetString("2"));
        pdu.add(v2);
        VariableBinding v3 = new VariableBinding();
        v3.setOid(new OID(".1.3.6.1.4.1.2011.2.235.1.1.500.1.10"));
        v3.setVariable(new OctetString("1970-01-01 00:00:00"));
        pdu.add(v3);

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);   // retry times when commuication error
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version1);

        System.out.println("Sending SNMPV1Trap n" + currentNum + "......");
        return snmp.send(pdu, target);  // send pdu, return response
    }

    /**
     * Snmp V2c 测试发送Trap
     *
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV2cTrap() throws IOException {
        PDU pdu = new PDU();
        VariableBinding v = new VariableBinding();
        v.setOid(SnmpConstants.sysName);
        v.setVariable(new OctetString("HUAWEI SNMPV2Trap from " + hostIP + " n" + currentNum));
        pdu.add(v);
        pdu.setType(PDU.TRAP);

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);   //retry times when commuication error
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        System.out.println("Sending SNMPV2Trap n" + currentNum + "......");
        return snmp.send(pdu, target);  //send pdu, return response
    }

    /**
     * SnmpV3 不带认证加密协议.
     *
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3TrapNoAuthNoPriv() throws IOException {
        SNMP4JSettings.setExtensibilityEnabled(true);
        SecurityProtocols.getInstance().addDefaultProtocols();

        UserTarget target = new UserTarget();
        target.setVersion(SnmpConstants.version3);

        byte[] enginId = "TEO_ID".getBytes();
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(enginId), 500);
        SecurityModels secModels = SecurityModels.getInstance();
        if (snmp.getUSM() == null) {
            secModels.addSecurityModel(usm);
        }

        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setAddress(targetAddress);
        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(PDU.NOTIFICATION);
        VariableBinding v = new VariableBinding();
        v.setOid(SnmpConstants.sysName);
        v.setVariable(new OctetString("HUAWEI SNMPV3Trap from " + hostIP + " n" + currentNum));
        pdu.add(v);

        snmp.setLocalEngine(enginId, 500, 1);
        return snmp.send(pdu, target);
    }

    /**
     * 目前无法被接收
     *
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3Auth() throws IOException {
        SNMP4JSettings.setExtensibilityEnabled(true);
        SecurityProtocols.getInstance().addDefaultProtocols();

        UserTarget target = new UserTarget();
        target.setSecurityName(new OctetString(username1));
        target.setVersion(SnmpConstants.version3);

        byte[] enginId = "TEO_ID".getBytes();
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(enginId), 500);
        SecurityModels secModels = SecurityModels.getInstance();
        synchronized (secModels) {
            if (snmp.getUSM() == null) {
                secModels.addSecurityModel(usm);
            }
            snmp.getUSM().addUser(new OctetString(username1), new OctetString(enginId), new UsmUser(new OctetString(username1), AuthMD5.ID, new OctetString(authPassword), Priv3DES.ID, new OctetString(privPassword)));
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setAddress(targetAddress);

            ScopedPDU pdu = new ScopedPDU();
            pdu.setType(PDU.NOTIFICATION);
            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString("Snmp Trap V3 Test sendV3Auth"));
            pdu.add(v);

            snmp.setLocalEngine(enginId, 500, 1);
            ResponseEvent send = snmp.send(pdu, target);
            //System.out.println(send.getError());

            return send;
        }
    }

    /**
     * 测试SnmpV3  带认证协议，加密协议
     *
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3() throws IOException {
        OctetString userName = new OctetString(username1);
        OctetString authPass = new OctetString(authPassword);
        OctetString privPass = new OctetString("privPassword");

        TransportMapping<?> transport;
        transport = new DefaultUdpTransportMapping();

        Snmp snmp = new Snmp(transport);
        //MPv3.setEnterpriseID(35904);
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 500);
        SecurityModels.getInstance().addSecurityModel(usm);

        UserTarget target = new UserTarget();

        byte[] enginId = "TEO_ID".getBytes();

        SecurityModels secModels = SecurityModels.getInstance();
        synchronized (secModels) {
            if (snmp.getUSM() == null) {
                secModels.addSecurityModel(usm);
            }
            /*snmp.getUSM().addUser(
                    new OctetString(username),
                    new OctetString(enginId),
                    new UsmUser(new OctetString(username), AuthMD5.ID,
                            new OctetString(authPassword), Priv3DES.ID,
                            new OctetString(privPassword)));*/
            // add user to the USM   
            snmp.getUSM().addUser(userName, new UsmUser(userName, AuthMD5.ID, authPass, PrivDES.ID, privPass));

            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(3000);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
            target.setSecurityName(userName);

            ScopedPDU pdu = new ScopedPDU();
            pdu.setType(PDU.NOTIFICATION);
            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString("Snmp Trap V3 Test sendV3Auth----------"));
            pdu.add(v);

            snmp.setLocalEngine(enginId, 500, 1);
            ResponseEvent send = snmp.send(pdu, target);
            //System.out.println(send.getError());

            return send;
        }
    }

    public void run() {
        try {
            sendV1Trap();
            sendV2cTrap();
            sendV3TrapNoAuthNoPriv();
            sendV3Auth();
            sendV3();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void sendV3() throws IOException {
        snmp.getUSM().addUser(
                new OctetString("MD5DES"),
                new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,
                        new OctetString("MD5DESUserAuthPassword"), PrivDES.ID,
                        new OctetString("MD5DESUserPrivPassword")));
        // create the target
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("MD5DES"));

        // create the PDU
        PDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(new OID("1.3.6")));
        pdu.setType(PDU.GETNEXT);

        // send the PDU
        ResponseEvent response = snmp.send(pdu, target);
        // extract the response PDU (could be null if timed out)
        PDU responsePDU = response.getResponse();
        // extract the address used by the agent to send the response:
        Address peerAddress = response.getPeerAddress();
    }*/
}