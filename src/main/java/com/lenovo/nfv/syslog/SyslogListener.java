package com.lenovo.nfv.syslog;

import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.SyslogServerConfigIF;
import org.graylog2.syslog4j.server.SyslogServerEventHandlerIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.util.SyslogUtility;

public class SyslogListener {

    public static void main(String args[]) {
        SyslogServerIF syslogServerIF = SyslogServer.getInstance("udp");
        SyslogServerConfigIF syslogServerConfigIF = syslogServerIF.getConfig();
        syslogServerConfigIF.setPort(514);

        SyslogServerEventHandlerIF syslogServerEventHandlerIF = new SyslogEventHandler();
        syslogServerConfigIF.addEventHandler(syslogServerEventHandlerIF);

        SyslogServer.getThreadedInstance("udp");

        while(true) {
            SyslogUtility.sleep(1000L);
        }

//        System.out.println("----------------" + syslogServerConfigIF.getEventHandlers() + "----------------");
    }
}