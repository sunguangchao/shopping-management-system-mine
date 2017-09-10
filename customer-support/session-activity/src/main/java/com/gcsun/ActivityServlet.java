package com.gcsun;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by 11981 on 2017/9/10.
 */

@WebServlet(
        name = "storeServlet",
        urlPatterns = "/do/*"
)
public class ActivityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException,ServletException{

        this.recordSessionActivity(request);
        this.viewSessionActivity(request, response);

    }
    private void recordSessionActivity(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("activity") == null)
            session.setAttribute("activity", new Vector<PageVisit>());
        @SuppressWarnings("unchecked")
        Vector<PageVisit> visits = (Vector<PageVisit>) session.getAttribute("activity");
        //用Vector因为它是线程安全的
        if (!visits.isEmpty()){
            PageVisit last = visits.lastElement();
            last.setLeftTimestamp(System.currentTimeMillis());
        }
        PageVisit now = new PageVisit();
        now.setEnteredTimestamp(System.currentTimeMillis());
        if (request.getQueryString() == null)//获取带参数查询的字符串
            now.setRequest(request.getRequestURL().toString());
        else
            now.setRequest(request.getRequestURL()+"?"+request.getQueryString());
        try{
            //getRemoteAddr():获取IP地址
            now.setIpAddress(InetAddress.getByName(request.getRemoteAddr()));
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        visits.add(now);
    }
    private void viewSessionActivity(HttpServletRequest request, HttpServletResponse response)
        throws IOException,ServletException{
        request.getRequestDispatcher("/WEB-INF/jsp/view/viewSessionActivity.jsp").forward(request,response);
    }
}
