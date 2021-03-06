package com.gcsun;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 11981 on 2017/9/30.
 * 使用监听器检测会话的变化
 */
public class SessionListener implements HttpSessionListener, HttpSessionIdListener{
    private SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    //创建新的会话时调用
    @Override
    public void sessionCreated(HttpSessionEvent e){
        System.out.println(this.date() + ": Session" + e.getSession().getId() + " created.");
        SessionRegistry.addSession(e.getSession());
    }
    //会话结束时调用
    @Override
    public void sessionDestroyed(HttpSessionEvent e){
        System.out.println(this.date() + ": Session " + e.getSession().getId() +
                " destroyed.");
        SessionRegistry.removeSession(e.getSession());
    }
    @Override
    public void sessionIdChanged(HttpSessionEvent e, String oldSessionId){
        System.out.println(this.date() + ": Session ID " + oldSessionId +
                " changed to " + e.getSession().getId());
        SessionRegistry.updateSessionId(e.getSession(), oldSessionId);
    }

    private String date(){
        return this.formatter.format(new Date());
    }


}
