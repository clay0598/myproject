package com.lenovo.nfv.syslog;

import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.SyslogServerSessionEventHandlerIF;

import java.net.SocketAddress;

public class SyslogEventHandler implements SyslogServerSessionEventHandlerIF {
    private static final long serialVersionUID = 4911589546629699192L;

//    public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
//        String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
//        String facility = SyslogUtility.getFacilityString(event.getFacility());
//        String level = SyslogUtility.getLevelString(event.getLevel());
//        System.out.println("{" + facility + "} " + date + " " + level + " " + event.getMessage());
//
//        // Simple parsing for Strings in the message
//        /*if (!event.getMessage().contains(" msg=\"")) {
//            System.out.println(event.getHost() + "\n" + event.getDate() + "\n" + event.getLevel()+ "\n" + event.getRaw() + "\n" + event.getMessage());
//        }*/
//    }

/*    @Override
    public void event(SyslogServerIF syslogServerIF, SyslogServerEventIF syslogServerEventIF) {
        syslogServerIF.run();
        System.out.println(syslogServerIF.getThread());
        System.out.println(syslogServerEventIF.getDate());
        System.out.println(syslogServerEventIF.getFacility());
        System.out.println(syslogServerEventIF.getHost());
        System.out.println(syslogServerEventIF.getLevel());
        System.out.println(syslogServerEventIF.getMessage());
    }*/

    @Override
    public Object sessionOpened(SyslogServerIF syslogServerIF, SocketAddress socketAddress) {
        return null;
    }

    @Override
    public void event(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, SyslogServerEventIF syslogServerEventIF) {
        syslogServerIF.run();
        System.out.println(syslogServerIF.getThread());
        System.out.println(syslogServerEventIF.getDate());
        System.out.println(syslogServerEventIF.getFacility());
        System.out.println(syslogServerEventIF.getHost());
        System.out.println(syslogServerEventIF.getLevel());
        System.out.println(syslogServerEventIF.getMessage());
    }

    @Override
    public void exception(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, Exception e) {

    }

    @Override
    public void sessionClosed(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, boolean b) {

    }

    @Override
    public void initialize(SyslogServerIF syslogServerIF) {

    }

    @Override
    public void destroy(SyslogServerIF syslogServerIF) {

    }
}
