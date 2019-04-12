package com.lenovo.nfv.syslog;

import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogIF;

import java.net.URLDecoder;
import java.util.Date;

public class SyslogSender {
    public static void main(String[] args) {
        // Set a Specific Host, then Log to It
        SyslogIF syslog = Syslog.getInstance("udp");
        syslog.getConfig().setHost("10.112.52.65");
        syslog.getConfig().setPort(514);
        StringBuffer buffer = new StringBuffer();
        buffer.append("操作时间：" + new Date().toString().substring(4, 20) + ";");
        buffer.append("操作者ID:" + "张三" + ";");
        buffer.append("操作时间:" + new Date()+ ";");
        buffer.append("日志类别:" + "22"+ ";");
        buffer.append("执行动作:" + "动作" + ";");
        buffer.append("备注:" + "备注");
        /*	发送信息到服务器，2表示日志级别 范围为0~7的数字编码，表示了事件的严重程度。0最高，7最低
         *  syslog为每个事件赋予几个不同的优先级：
            LOG_EMERG：紧急情况，需要立即通知技术人员。
            LOG_ALERT：应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失。
            LOG_CRIT：重要情况，如硬盘错误，备用连接丢失。
            LOG_ERR：错误，不是非常紧急，在一定时间内修复即可。
            LOG_WARNING：警告信息，不是错误，比如系统磁盘使用了85%等。
            LOG_NOTICE：不是错误情况，也不需要立即处理。
            LOG_INFO：情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
            LOG_DEBUG：包含详细的开发情报的信息，通常只在调试一个程序时使用。
         */
        while (true) {
            try {
                syslog.log(0, URLDecoder.decode(buffer.toString(),"utf-8"));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
